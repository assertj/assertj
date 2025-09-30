/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.protobuf.api;

import org.assertj.core.api.AbstractAssert;
import org.assertj.protobuf.internal.MessageDifferencer;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;

/**
 * Assertions for Protocol Buffer {@link Message}.
 * <p>
 * To create a new instance of this class, invoke {@link Assertions#assertThat(Message)}.
 * <p>
 * Example:
 * <pre><code class='java'> import static org.assertj.protobuf.api.Assertions.assertThat;
 *
 * MyProto expected = MyProto.newBuilder()
 *     .setFoo("bar")
 *     .addRepeatedField(1)
 *     .addRepeatedField(2)
 *     .build();
 *
 * MyProto actual = MyProto.newBuilder()
 *     .setFoo("bar")
 *     .addRepeatedField(2)
 *     .addRepeatedField(1)
 *     .build();
 *
 * // This assertion will fail because repeated fields are in different order
 * assertThat(actual).isEqualTo(expected);
 *
 * // This assertion will pass because we ignore repeated field order
 * assertThat(actual).ignoringRepeatedFieldOrder().isEqualTo(expected);</code></pre>
 *
 * @param <T> the type of the actual Protobuf message
 *
 * @author JongJun Kim
 */
public class MessageAssert<T extends Message> extends AbstractAssert<MessageAssert<T>, T> {

  private final MessageDifferencer differencer;

  /**
   * Creates a new {@link MessageAssert}.
   *
   * @param actual the actual Protobuf message
   */
  public MessageAssert(T actual) {
    super(actual, MessageAssert.class);
    this.differencer = new MessageDifferencer();
  }

  /**
   * Verifies that the actual Protobuf message is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> MyProto expected = MyProto.newBuilder().setFoo("bar").build();
   * MyProto actual = MyProto.newBuilder().setFoo("bar").build();
   *
   * assertThat(actual).isEqualTo(expected);</code></pre>
   *
   * @param expected the expected Protobuf message
   * @return {@code this} assertion object
   * @throws AssertionError if the actual message is not equal to the expected one
   */
  @Override
  public MessageAssert<T> isEqualTo(Object expected) {
    if (actual == expected) return this;

    if (expected == null) {
      failWithMessage("Expected message to be null but was:<%s>", actual);
    }

    if (!(expected instanceof Message)) {
      failWithMessage("Expected a Protobuf Message but was:<%s>", expected.getClass());
    }

    Message expectedMessage = (Message) expected;

    if (!differencer.compare(actual, expectedMessage)) {
      String differences = differencer.getLastDifferences();
      failWithMessage("Protobuf messages are not equal:%n%s", differences);
    }

    return this;
  }

  /**
   * Configures the assertion to ignore the order of repeated fields when comparing messages.
   * <p>
   * Example:
   * <pre><code class='java'> MyProto expected = MyProto.newBuilder()
   *     .addRepeatedField(1)
   *     .addRepeatedField(2)
   *     .build();
   *
   * MyProto actual = MyProto.newBuilder()
   *     .addRepeatedField(2)
   *     .addRepeatedField(1)
   *     .build();
   *
   * // This will pass
   * assertThat(actual).ignoringRepeatedFieldOrder().isEqualTo(expected);</code></pre>
   *
   * @return {@code this} assertion object
   */
  public MessageAssert<T> ignoringRepeatedFieldOrder() {
    differencer.setRepeatedFieldComparison(MessageDifferencer.RepeatedFieldComparison.AS_SET);
    return this;
  }

  /**
   * Configures the assertion to ignore specific fields when comparing messages.
   * <p>
   * Example:
   * <pre><code class='java'> MyProto expected = MyProto.newBuilder()
   *     .setFoo("bar")
   *     .setTimestamp(12345L)
   *     .build();
   *
   * MyProto actual = MyProto.newBuilder()
   *     .setFoo("bar")
   *     .setTimestamp(67890L)
   *     .build();
   *
   * // This will pass because we ignore the timestamp field
   * assertThat(actual).ignoringFields("timestamp").isEqualTo(expected);</code></pre>
   *
   * @param fieldPaths the field paths to ignore (e.g., "field_name", "nested.field")
   * @return {@code this} assertion object
   */
  public MessageAssert<T> ignoringFields(String... fieldPaths) {
    for (String fieldPath : fieldPaths) {
      differencer.ignoreField(fieldPath);
    }
    return this;
  }

  /**
   * Configures the assertion to only compare fields that are set in the expected message.
   * <p>
   * Example:
   * <pre><code class='java'> MyProto expected = MyProto.newBuilder()
   *     .setFoo("bar")
   *     .build();
   *
   * MyProto actual = MyProto.newBuilder()
   *     .setFoo("bar")
   *     .setExtraField("extra")
   *     .build();
   *
   * // This will pass because we only compare fields set in expected
   * assertThat(actual).comparingExpectedFieldsOnly().isEqualTo(expected);</code></pre>
   *
   * @return {@code this} assertion object
   */
  public MessageAssert<T> comparingExpectedFieldsOnly() {
    differencer.setScope(MessageDifferencer.Scope.PARTIAL);
    return this;
  }

  /**
   * Verifies that the actual Protobuf message has the specified field set.
   * <p>
   * Example:
   * <pre><code class='java'> MyProto actual = MyProto.newBuilder().setFoo("bar").build();
   *
   * assertThat(actual).hasField("foo");</code></pre>
   *
   * @param fieldName the name of the field
   * @return {@code this} assertion object
   * @throws AssertionError if the field is not set
   */
  public MessageAssert<T> hasField(String fieldName) {
    isNotNull();

    FieldDescriptor field = actual.getDescriptorForType().findFieldByName(fieldName);
    if (field == null) {
      failWithMessage("Field <%s> does not exist in message type <%s>",
                      fieldName, actual.getDescriptorForType().getFullName());
    }

    if (!actual.hasField(field)) {
      failWithMessage("Expected message to have field <%s> but it was not set", fieldName);
    }

    return this;
  }

  /**
   * Verifies that the actual Protobuf message does not have the specified field set.
   * <p>
   * Example:
   * <pre><code class='java'> MyProto actual = MyProto.newBuilder().build();
   *
   * assertThat(actual).doesNotHaveField("foo");</code></pre>
   *
   * @param fieldName the name of the field
   * @return {@code this} assertion object
   * @throws AssertionError if the field is set
   */
  public MessageAssert<T> doesNotHaveField(String fieldName) {
    isNotNull();

    FieldDescriptor field = actual.getDescriptorForType().findFieldByName(fieldName);
    if (field == null) {
      failWithMessage("Field <%s> does not exist in message type <%s>",
                      fieldName, actual.getDescriptorForType().getFullName());
    }

    if (actual.hasField(field)) {
      failWithMessage("Expected message not to have field <%s> but it was set to <%s>",
                      fieldName, actual.getField(field));
    }

    return this;
  }
}
