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

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class AssertionsTest extends BaseAssertionsTest {

  @Test
  public void should_have_the_same_methods_as_in_bdd_assertions() {
    Class<Assertions> classA = Assertions.class;
    String methodA = "assertThat";
    Class<BDDAssertions> classB = BDDAssertions.class;
    String methodB = "then";
    List<Method> assertThatMethods = findMethodsWithName(classA, methodA);
    List<Method> thenMethods = findMethodsWithName(classB, methodB);

    Comparator<Method> methodComparator = ignoringDeclaringClassAndMethodName();
    Assertions.assertThat(assertThatMethods).usingElementComparator(methodComparator)
              .containsExactlyInAnyOrder(thenMethods.toArray(new Method[thenMethods.size()]));
  }

  @Test
  public void should_have_the_same_methods_as_in_standard_soft_assertions() {
    Class<Assertions> classA = Assertions.class;
    String methodA = "assertThat";
    Class<AbstractStandardSoftAssertions> classB = AbstractStandardSoftAssertions.class;
    String methodB = "assertThat";
    List<Method> assertThatMethods = findMethodsWithName(classA, methodA);
    List<Method> assertThatSoftMethods = findMethodsWithName(classB, methodB);

    Comparator<Method> methodComparator = ignoringDeclaringClassAndMethodName();
    Assertions.assertThat(assertThatMethods).usingElementComparator(methodComparator)
              .containsExactlyInAnyOrder(assertThatSoftMethods.toArray(new Method[assertThatSoftMethods.size()]));

  }
}
