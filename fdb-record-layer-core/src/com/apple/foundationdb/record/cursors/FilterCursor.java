/*
 * FilterCursor.java
 *
 * This source file is part of the FoundationDB open source project
 *
 * Copyright 2015-2018 Apple Inc. and the FoundationDB project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apple.foundationdb.record.cursors;

import com.apple.foundationdb.async.AsyncUtil;
import com.apple.foundationdb.record.RecordCursor;
import com.apple.foundationdb.record.RecordCursorVisitor;
import com.apple.foundationdb.record.SpotBugsSuppressWarnings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * A cursor that filters elements using a predicate.
 * @param <T> the type of elements of the cursor
 */
public class FilterCursor<T> implements RecordCursor<T> {
    @Nonnull
    private final RecordCursor<T> inner;
    @Nonnull
    private final Function<T, Boolean> pred;
    @Nullable
    private T next;
    private boolean hasNext;
    @Nullable
    private CompletableFuture<Boolean> nextFuture;
    @Nullable
    private byte[] lastContinuation;

    // for detecting incorrect cursor usage
    private boolean mayGetContinuation = false;

    public FilterCursor(@Nonnull RecordCursor<T> inner, @Nonnull Function<T, Boolean> pred) {
        this.inner = inner;
        this.pred = pred;
    }

    @Nonnull
    @Override
    public CompletableFuture<Boolean> onHasNext() {
        if (nextFuture == null) {
            mayGetContinuation = false;
            nextFuture = AsyncUtil.whileTrue(() -> inner.onHasNext()
                    .thenApply(innerHasNext -> {
                        if (!innerHasNext) {
                            hasNext = false;
                            lastContinuation = inner.getContinuation();
                            return false; // Stop waiting because empty.
                        } else {
                            next = inner.next();
                            lastContinuation = inner.getContinuation();
                            hasNext = (Boolean.TRUE.equals(pred.apply(next)));
                            return !hasNext; // Stop when matches.
                        }
                    }), getExecutor()).thenApply(vignore -> {
                        mayGetContinuation = !hasNext;
                        return hasNext;
                    });
        }
        return nextFuture;
    }

    @Nullable
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        nextFuture = null;
        mayGetContinuation = true;
        return next;
    }

    @Nullable
    @Override
    @SpotBugsSuppressWarnings("EI_EXPOSE_REP")
    public byte[] getContinuation() {
        IllegalContinuationAccessChecker.check(mayGetContinuation);
        return lastContinuation;
    }

    @Override
    public NoNextReason getNoNextReason() {
        return inner.getNoNextReason();
    }

    @Override
    public void close() {
        if (nextFuture != null) {
            nextFuture.cancel(false);
            nextFuture = null;
        }
        inner.close();
    }

    @Nonnull
    @Override
    public Executor getExecutor() {
        return inner.getExecutor();
    }

    @Override
    public boolean accept(@Nonnull RecordCursorVisitor visitor) {
        if (visitor.visitEnter(this)) {
            inner.accept(visitor);
        }
        return visitor.visitLeave(this);
    }
}
