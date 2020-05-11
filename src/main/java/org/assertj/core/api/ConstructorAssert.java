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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.lang.reflect.Constructor;

/**
 * Assertion methods for {@code Constructor}.
 * <p>
 * To create a new instance of this Constructor, invoke <code>{@link
 * org.assertj.core.api.Assertions#assertThat(Constructor)}</code>
 * </p>
 *
 * @author phx
 */
public class ConstructorAssert extends AbstractConstructorAssert<ConstructorAssert, Constructor> {

  public ConstructorAssert(Constructor actual) {
    super(actual, ConstructorAssert.class);
  }

  public final ConstructorAssert isPublic() {
    return super.isPublic();
  }

  public final ConstructorAssert isPrivate() {
    return super.isPrivate();
  }

  public final ConstructorAssert isProtected() {
    return super.isProtected();
  }

  public final ConstructorAssert hasArguments(Class<?>... arguments) {
    return super.hasArguments(arguments);
  }


}
