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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.lang.reflect.Constructor;

/**
 * Assertion methods for {@code Constructor}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Constructor)}</code>
 * </p>
 * 
 * @author William Bakker
 */
public class ConstructorAssert<CLASS> extends AbstractConstructorAssert<ConstructorAssert<CLASS>, CLASS> {

  public static <CLASS> ConstructorAssert<CLASS> assertThatConstructor(Constructor<CLASS> actual) {
    return new ConstructorAssert<>(actual);
  }

  public ConstructorAssert(Constructor<CLASS> actual) {
    super(actual, ConstructorAssert.class);
  }

}
