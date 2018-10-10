/*
 * LogMessageKeys.java
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

package com.apple.foundationdb.record.logging;

import javax.annotation.Nonnull;

/**
 * Common {@link KeyValueLogMessage} keys logged by the Record Layer core.
 * In general, we try to consolidate all of the keys here, so that it's easy to check for collisions, ensure consistency, etc.
 */
public enum LogMessageKeys {
    // general keys
    TITLE("ttl"),
    SUBSPACE("subspace"),
    SUBSPACE_KEY("subspace_key"),
    // record splitting/unsplitting
    KEY("key"),
    KEY_TUPLE("key_tuple"),
    EXPECTED_INDEX("expected_index"),
    FOUND_INDEX("found_index"),
    SPLIT_NEXT_INDEX("next_index"),
    SPLIT_REVERSE("reverse"),
    // protobuf parsing
    RAW_BYTES("raw_bytes"),
    // key space API keys
    PARENT_DIR("parent_dir"),
    DIR_NAME("dir_name"),
    DIR_TYPE("dir_type"),
    DIR_VALUE("dir_value"),
    PROVIDED_VALUE("provided_value"),
    // key expressions
    EXPECTED_COLUMN_SIZE("expected_column_size"),
    ACTUAL_COLUMN_SIZE("actual_column_size"),
    KEY_EXPRESSION("key_expression"),
    KEY_EVALUATED("key_evaluated"),
    // index-related keys
    INDEX_NAME("index_name"),
    VALUE_KEY("value_key"),
    PRIMARY_KEY("primary_key"),
    VALUE("value"),
    INDEX_OPERATION("operation"),
    INITIAL_PREFIX("initial_prefix"),
    SECOND_PREFIX("second_prefix"),
    // comparisons
    COMPARISON_VALUE("comparison_value"),
    // functional index keys
    FUNCTION("function"),
    INDEX_KEY("index_key"),
    KEY_SPACE_PATH("key_space_path"),
    INDEX_VALUE("index_value"),
    ARGUMENT_INDEX("argument_index"),
    EVALUATED_SIZE("evaluated_size"),
    EVALUATED_INDEX("evaluated_index"),
    EXPECTED_TYPE("expected_type"),
    ACTUAL_TYPE("actual_type"),
    // cursors
    CHILD_COUNT("child_count"),
    EXPECTED_CHILD_COUNT("expected_child_count"),
    READ_CHILD_COUNT("read_child_count"),
    // upgrading
    VERSION("version"),
    OLD_VERSION("old_version"),
    NEW_VERSION("new_version"),
    META_DATA_VERSION("metaDataVersion"),
    FORMAT_VERSION("format_version"),
    // tuple range
    LOW_BYTES("lowBytes"),
    HIGH_BYTES("highBytes"),
    RANGE_BYTES("rangeBytes"),
    RANGE_START("rangeStart"),
    RANGE_END("rangeEnd"),

    // resolver
    RESOLVER_KEY("resolverKey"),
    RESOLVER_PATH("resolverPath"),

    // Log the name of the tokenizer used
    TOKENIZER_NAME("tokenizer_name");

    private final String logKey;

    LogMessageKeys(@Nonnull String key) {
        this.logKey = key;
    }

    @Override
    public String toString() {
        return logKey;
    }
}
