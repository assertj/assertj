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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import java.util.function.Function;

/**
 * A {@link Function} to produce an {@link Assert} for a given value.
 * This function typically wraps a call to <code>assertThat(value)</code> to produce a concrete assert type {@code ASSERT}
 * for the input value.
 * <p>
 * This function is typically used by navigation assertions on iterable types like {@link AbstractIterableAssert} when calling
 * {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable&lt;E&gt;, AssertFactory&lt;E, ASSERT&gt;)}
 * <p>
 * @param <T> the type of the input to the function.
 * @param <ASSERT> the type of the resulting {@code Assert}.
 * 
 * @since 2.5.0 / 3.5.0
 */
@FunctionalInterface
public interface AssertFactory<T, ASSERT extends Assert<?, ?>> extends Function<T, ASSERT> {
}
