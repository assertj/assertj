/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Person;
import org.assertj.core.util.introspection.beans.SuperHero;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link MethodSupport#methodResultFor(Object, String)}</code>.
 *
 * @author Michał Piotrkowski
 */
public class MethodSupport_methodResultFor_Test {

  @Rule
  public ExpectedException thrown = none();
  private Person bruceWayne;
  private Person joker;
  private SuperHero batman;

  @Before
  public void setUp() {
    bruceWayne = new Person("Bruce Wayne");
    joker = new Person("Joker");
    batman = new SuperHero("Batman", bruceWayne, joker);
  }

  @Test
  public void should_invoke_methods_without_arguments() {
    Object result = MethodSupport.methodResultFor(batman, "archenemy");
    assertThat(result).isEqualTo(joker);
  }

  @Test
  public void should_invoke_methods_from_superclass() {
    Object result = MethodSupport.methodResultFor(batman, "getName");
    assertThat(result).isEqualTo("Batman");
  }

  @Test
  public void should_fail_meaningfully_if_object_instance_not_provided() {
    thrown.expectNullPointerException("Object instance can not be null!");
    MethodSupport.methodResultFor(null, "methodName");
  }

  @Test
  public void should_fail_meaningfully_if_method_name_not_provided() {
    thrown.expectNullPointerException("Method name can not be empty!");
    MethodSupport.methodResultFor(batman, null);
  }

  @Test
  public void should_fail_meaningfully_if_method_name_is_empty() {
    thrown.expectIllegalArgumentException("Method name can not be empty!");
    MethodSupport.methodResultFor(batman, "");
  }

  @Test
  public void should_fail_meaningfully_if_method_not_found() {
    thrown.expectIllegalArgumentException("Can't find method 'commitCrime' in class SuperHero.class. Make sure public" +
                                            " method exists and accepts no arguments!");
    MethodSupport.methodResultFor(batman, "commitCrime");
  }

  @Test
  public void should_fail_meaningfully_if_method_does_not_return_value() {
    thrown.expectIllegalArgumentException("Method 'saveTheDay' in class SuperHero.class has to return a value!");
    MethodSupport.methodResultFor(batman, "saveTheDay");
  }

  @Test
  public void should_fail_meaningfully_if_method_is_not_public() {
    thrown.expectIllegalArgumentException("Can't find method 'trueIdentity' in class SuperHero.class. Make sure " +
                                            "public method exists and accepts no arguments!");
    MethodSupport.methodResultFor(batman, "trueIdentity");
  }

}
