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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.osgi.soft;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.AbstractMapAssert;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

public class CustomSoftAssertionTest {

  @Test
  void verify_classloaders() {
    // GIVEN
    Class<?> assertClass = TestProxyableMapAssert.class;
    Class<?> superClass = assertClass.getSuperclass();
    // THEN
    then(assertClass.getClassLoader()).as("Custom assertion class must be from a different class loader than it's super class")
                                      .isNotSameAs(superClass.getClassLoader());
    then(superClass.getClassLoader()).as("Custom assertion super class must be from the assertj-core class loader")
                                     .isSameAs(Assertions.class.getClassLoader());
  }

  @Test
  void custom_soft_assertions_success() {
    // GIVEN
    TestSoftAssertions softly = new TestSoftAssertions();
    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    // WHEN
    softly.assertThat(map).containsKeys("key1", "key2").containsValues("value1", "value2");
    // THEN
    softly.assertAll();
  }

  @Test
  void custom_soft_assertions_failure() {
    // GIVEN
    TestSoftAssertions softly = new TestSoftAssertions();
    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    // WHEN
    softly.assertThat(map).containsKeys("key1", "key3").containsValues("value3", "value2");
    // THEN
    then(softly.wasSuccess()).isFalse();
    then(softly.errorsCollected()).hasSize(2);
  }

  public static class TestProxyableMapAssert<KEY, VALUE>
      extends AbstractMapAssert<TestProxyableMapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> {

    public TestProxyableMapAssert(Map<KEY, VALUE> actual) {
      super(actual, TestProxyableMapAssert.class);
    }

    @Override
    protected <ELEMENT> AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> newListAssertInstance(List<? extends ELEMENT> newActual) {
      return new ListAssert<>(newActual);
    }
  }

  public static class TestSoftAssertions extends AbstractSoftAssertions {
    @SuppressWarnings("unchecked")
    public <K, V> TestProxyableMapAssert<K, V> assertThat(Map<K, V> actual) {
      return proxy(TestProxyableMapAssert.class, Map.class, actual);
    }
  }

}
