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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.Assertions#assertThat(Constructor)}</code>.
 *
 * @author phx
 */
//CS304 Issue link: https://github.com/joel-costigliola/assertj-core/issues/1869
public class Assertions_assertThat_with_Constructor_Test {

  @Test
  public void should_create_Assert() throws NoSuchMethodException {
    AbstractConstructorAssert<ConstructorAssert, Constructor> assertions = Assertions
      .assertThat(String.class.getDeclaredConstructor());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() throws NoSuchMethodException {
    AbstractConstructorAssert<ConstructorAssert, Constructor> assertions = Assertions
      .assertThat(String.class.getDeclaredConstructor());
    assertThat(assertions.actual.getName())
      .isSameAs(String.class.getDeclaredConstructor().getName());
  }

  @Test
  public void should_pass_isPublic() throws NoSuchMethodException {
    assertThat(Person.class.getDeclaredConstructor(String.class)).isPublic();
  }

  @Test
  public void should_pass_isPrivate() throws NoSuchMethodException {
    assertThat(Person.class.getDeclaredConstructor(char.class)).isProtected();
  }

  @Test
  public void should_pass_isProtected() throws NoSuchMethodException {
    assertThat(Person.class.getDeclaredConstructor(int.class)).isPrivate();
  }

  @Test
  public void should_pass_hasArguments() throws NoSuchMethodException {
    assertThat(String.class.getDeclaredConstructor()).hasArguments();
  }

}
