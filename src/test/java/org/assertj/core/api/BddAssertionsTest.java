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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Filip Hrisafov
 */
public class BddAssertionsTest extends BaseAssertionsTest {

  @Test
  public void should_have_the_same_methods_as_in_assertions() {
    Class<BDDAssertions> classA = BDDAssertions.class;
    String methodA = "then";
    Class<Assertions> classB = Assertions.class;
    String methodB = "assertThat";
    Method[] thenMethods = findMethodsWithName(classA, methodA);
    Method[] assertThatMethods = findMethodsWithName(classB, methodB);

    Comparator<Method> methodComparator = ignoringDeclaringClassAndMethodName();
    assertThat(thenMethods).usingElementComparator(methodComparator).containsExactlyInAnyOrder(assertThatMethods);
  }

  @Test
  public void should_have_the_same_methods_as_in_bdd_soft_assertions() {
    Class<BDDAssertions> classA = BDDAssertions.class;
    String methodA = "then";
    Class<AbstractBDDSoftAssertions> classB = AbstractBDDSoftAssertions.class;
    String methodB = "then";
    Method[] thenMethods = findMethodsWithName(classA, methodA);
    Method[] thenSoftMethods = findMethodsWithName(classB, methodB);

    Comparator<Method> methodComparator = ignoringDeclaringClassAndMethodName();
    assertThat(thenMethods).usingElementComparator(methodComparator).containsExactlyInAnyOrder(thenSoftMethods);
  }
}
