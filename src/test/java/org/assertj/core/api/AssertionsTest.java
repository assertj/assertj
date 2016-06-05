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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class AssertionsTest extends BaseAssertionsTest {

  @Test
  public void should_have_the_same_methods_as_in_bdd_assertions() {
    Method[] assertThatMethods = findMethodsWithName(Assertions.class, "assertThat");
    Method[] thenMethods = findMethodsWithName(BDDAssertions.class, "then");

    assertThat(assertThatMethods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_METHOD_NAME)
                                 .containsExactlyInAnyOrder(thenMethods);
  }

  @Test
  public void should_have_the_same_methods_as_in_standard_soft_assertions() {
    // Until the SpecialIgnoredReturnTypes like AssertProvider, XXXNavigableXXXAssert are implemented for
    // the soft assertions we need to ignore them
    Method[] assertThatMethods = findMethodsWithName(Assertions.class, "assertThat", SPECIAL_IGNORED_RETURN_TYPES);
    Method[] assertThatSoftMethods = findMethodsWithName(AbstractStandardSoftAssertions.class, "assertThat");

    // ignore the return type of soft assertions until they have the same as the Assertions 
    assertThat(assertThatMethods).usingElementComparator(IGNORING_DECLARING_CLASS_AND_RETURN_TYPE)
                                 .containsExactlyInAnyOrder(assertThatSoftMethods);

  }
}
