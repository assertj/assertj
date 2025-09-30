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

import com.google.protobuf.Message;

/**
 * Entry point for assertions for Protocol Buffers.
 * <p>
 * Example:
 * <pre><code class='java'> import static org.assertj.protobuf.api.Assertions.assertThat;
 *
 * MyProto expected = MyProto.newBuilder().setFoo("bar").build();
 * MyProto actual = MyProto.newBuilder().setFoo("bar").build();
 *
 * assertThat(actual).isEqualTo(expected);</code></pre>
 *
 * @author JongJun Kim
 */
public class Assertions {

  /**
   * Creates a new instance of {@link MessageAssert}.
   *
   * @param <T> the type of the actual value
   * @param actual the actual Protobuf message
   * @return the created assertion object
   */
  public static <T extends Message> MessageAssert<T> assertThat(T actual) {
    return new MessageAssert<>(actual);
  }

  /**
   * Protected constructor to prevent direct instantiation but allow subclassing.
   */
  protected Assertions() {
    // empty
  }
}
