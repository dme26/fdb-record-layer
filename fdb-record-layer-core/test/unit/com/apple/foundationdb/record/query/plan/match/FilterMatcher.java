/*
 * FilterMatcher.java
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

package com.apple.foundationdb.record.query.plan.match;

import com.apple.foundationdb.record.query.expressions.QueryComponent;
import com.apple.foundationdb.record.query.plan.plans.RecordQueryFilterPlan;
import com.apple.foundationdb.record.query.plan.plans.RecordQueryPlan;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

/**
 * Plan matcher for a filter applied to a child plan matcher.
 */
public class FilterMatcher extends PlanMatcherWithChild {
    @Nonnull
    private final Matcher<QueryComponent> filterMatcher;

    public FilterMatcher(@Nonnull Matcher<QueryComponent> filterMatcher, @Nonnull Matcher<RecordQueryPlan> childMatcher) {
        super(childMatcher);
        this.filterMatcher = filterMatcher;
    }

    @Override
    public boolean matchesSafely(@Nonnull RecordQueryPlan plan) {
        return plan instanceof RecordQueryFilterPlan &&
                filterMatcher.matches(((RecordQueryFilterPlan) plan).getFilter()) &&
                super.matchesSafely(plan);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Filter(");
        filterMatcher.describeTo(description);
        description.appendText("; ");
        super.describeTo(description);
        description.appendText(")");
    }
}
