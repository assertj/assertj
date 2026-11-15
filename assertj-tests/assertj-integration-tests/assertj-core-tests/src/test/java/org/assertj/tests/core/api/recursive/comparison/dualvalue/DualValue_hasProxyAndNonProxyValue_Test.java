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
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.api.recursive.data.DualValueUtil.dualValue;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_hasProxyAndNonProxyValue_Test {

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource
  void should_return_true_only_when_one_value_is_a_proxy_and_the_other_is_not(Object actual, Object expected,
                                                                              boolean expectedResult) {
    // GIVEN
    var dualValue = dualValue(actual, expected);
    // WHEN
    boolean hasProxyAndNonProxyValue = dualValue.hasProxyAndNonProxyValue();
    // THEN
    then(hasProxyAndNonProxyValue).isEqualTo(expectedResult);
  }

  static Stream<Arguments> should_return_true_only_when_one_value_is_a_proxy_and_the_other_is_not() {
    Person proxy = proxyPerson("alice");
    Person otherProxy = proxyPerson("bob");
    Person person = new PersonImpl("bob");
    return Stream.of(Arguments.of(proxy, person, true),
                     Arguments.of(person, proxy, true),
                     Arguments.of(proxy, null, true),
                     Arguments.of(null, proxy, true),
                     Arguments.of(proxy, otherProxy, false),
                     Arguments.of(proxy, proxy, false),
                     Arguments.of(person, new PersonImpl("alice"), false),
                     Arguments.of(person, null, false),
                     Arguments.of(null, person, false),
                     Arguments.of(null, null, false));
  }

  private static Person proxyPerson(String name) {
    return (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class<?>[] { Person.class },
                                           (proxy, method, args) -> switch (method.getName()) {
                                           case "getName" -> name;
                                           case "toString" -> "Proxy(" + name + ")";
                                           case "hashCode" -> System.identityHashCode(proxy);
                                           case "equals" -> proxy == args[0];
                                           default -> null;
                                           });
  }

  interface Person {
    String getName();
  }

  record PersonImpl(String name) implements Person {
    @Override
    public String getName() {
      return name;
    }
  }
}
