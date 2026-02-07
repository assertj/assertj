/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.soft;

import org.assertj.core.api.AssertionErrorCollector;

/**
 * {@link DefaultSoftAssertFactory} instances for Java types.
 */
public interface SoftAssertFactories {


//  /**
//   * {@link InstanceOfSoftAssertFactory} for an {@link Optional}, assuming {@code Object} as value type.
//   *
//   * @see #optional(Class)
//   */
//  @SuppressWarnings("rawtypes")
//  InstanceOfSoftAssertFactory<Optional, SoftOptionalAssert<Object>> OPTIONAL = optional(Object.class);


  DefaultSoftAssertFactory<Object, SoftObjectAssert<?>> OBJECT = new DefaultSoftAssertFactory<>(Object.class,
                                                                                                (Object actual, AssertionErrorCollector assertionErrorCollector) -> new SoftObjectAssert(actual, assertionErrorCollector));
  DefaultSoftAssertFactory<String, SoftStringAssert> STRING = new DefaultSoftAssertFactory<>(String.class, SoftStringAssert::new);


//  /**
//   * {@link InstanceOfSoftAssertFactory} for an {@link Optional}.
//   *
//   * @param <VALUE>    the {@code Optional} value type.
//   * @param resultType the value type instance.
//   * @return the factory instance.
//   *
//   * @see #OPTIONAL
//   */
//  @SuppressWarnings("rawtypes")
//  static <VALUE> InstanceOfSoftAssertFactory<Optional, SoftOptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
//    return new InstanceOfSoftAssertFactory<>(Optional.class, new Class[] { resultType }, actual -> GeneratedSoftAssertionsassertThat);
//  }
//
}
