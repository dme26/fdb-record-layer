/*
 * InterningLayerToInterningLayerReplicaTest.java
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

package com.apple.foundationdb.record.provider.foundationdb.keyspace;

import com.apple.foundationdb.record.provider.foundationdb.FDBRecordContext;
import com.apple.foundationdb.record.provider.foundationdb.layers.interning.ScopedInterningLayer;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test replicating from one interning layer to another.
 */
public class InterningLayerToInterningLayerReplicaTest extends ResolverMappingReplicatorTest {
    @BeforeEach
    public void setup() {
        try (FDBRecordContext context = database.openContext()) {
            primary = new ScopedInterningLayer(keySpace.path(context, "test-path").add("to").add("primary"));
            replica = new ScopedInterningLayer(keySpace.path(context, "test-path").add("to").add("replica"));
        }
        seedWithMetadata = true;
    }
}
