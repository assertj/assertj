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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.Integer.toHexString;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursive;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.DeepDifference;
import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.internal.objects.Objects_assertIsEqualToComparingFieldByFieldRecursive_Test.WithCollection;
import org.assertj.core.internal.objects.Objects_assertIsEqualToComparingFieldByFieldRecursive_Test.WithMap;
import org.assertj.core.test.Jedi;
import org.junit.Test;

public class ShouldBeEqualByComparingFieldByFieldRecursively_create_Test {

  @Test
  public void should_throw_assertion_error_rather_than_null_pointer_when_one_nested_member_is_null() {
    // GIVEN
    Jedi yoda = new Jedi("Yoda", "Green");
    Jedi noname = new Jedi(null, "Green");
    // WHEN
    Throwable throwable1 = catchThrowable(() -> assertThat(yoda).isEqualToComparingFieldByFieldRecursively(noname));
    Throwable throwable2 = catchThrowable(() -> assertThat(noname).isEqualToComparingFieldByFieldRecursively(yoda));
    // THEN
    assertThat(throwable1).isInstanceOf(AssertionError.class)
                         .isNotInstanceOf(NullPointerException.class);
    assertThat(throwable2).isInstanceOf(AssertionError.class)
                          .isNotInstanceOf(NullPointerException.class);
  }

  @Test
  public void should_use_unambiguous_fields_description_when_standard_description_of_actual_and_expected_collection_fields_values_are_identical() {
    // GIVEN
    WithCollection<String> withHashSet = new WithCollection<>(new LinkedHashSet<String>());
    WithCollection<String> withSortedSet = new WithCollection<>(new TreeSet<String>());
    withHashSet.collection.add("bar");
    withHashSet.collection.add("foo");
    withSortedSet.collection.addAll(withHashSet.collection);
    List<Difference> differences = DeepDifference.determineDifferences(withHashSet, withSortedSet, null, null);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursive(withSortedSet,
                                                                   withHashSet,
                                                                   differences,
                                                                   CONFIGURATION_PROVIDER.representation())
        .create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <WithCollection [collection=[bar, foo]]>%n" +
                                         "to be equal to:%n" +
                                         "  <WithCollection [collection=[bar, foo]]>%n" +
                                         "when recursively comparing field by field, but found the following difference(s):%n"
                                         + "%n" +
                                         "Path to difference: <collection>%n" +
                                         "- expected: <[\"bar\", \"foo\"] (TreeSet@%s)>%n" +
                                         "- actual  : <[\"bar\", \"foo\"] (LinkedHashSet@%s)>",
                                         toHexString(withSortedSet.collection.hashCode()),
                                         toHexString(withHashSet.collection.hashCode())));
  }

  @Test
  public void should_use_unambiguous_fields_description_when_standard_description_of_actual_and_expected_map_fields_values_are_identical() {
    // GIVEN
    WithMap<Long, Boolean> withLinkedHashMap = new WithMap<>(new LinkedHashMap<Long, Boolean>());
    WithMap<Long, Boolean> withTreeMap = new WithMap<>(new TreeMap<Long, Boolean>());
    withLinkedHashMap.map.put(1L, true);
    withLinkedHashMap.map.put(2L, false);
    withTreeMap.map.putAll(withLinkedHashMap.map);
    List<Difference> differences = DeepDifference.determineDifferences(withLinkedHashMap, withTreeMap, null, null);
    // WHEN
    // @format:off
    String message = shouldBeEqualByComparingFieldByFieldRecursive(withTreeMap,
                                                                   withLinkedHashMap,
                                                                   differences,
                                                                   CONFIGURATION_PROVIDER.representation())
                                                          .create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // @format:on
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <WithMap [map={1=true, 2=false}]>%n" +
                                         "to be equal to:%n" +
                                         "  <WithMap [map={1=true, 2=false}]>%n" +
                                         "when recursively comparing field by field, but found the following difference(s):%n"
                                         + "%n" +
                                         "Path to difference: <map>%n" +
                                         "- expected: <{1L=true, 2L=false} (TreeMap@%s)>%n" +
                                         "- actual  : <{1L=true, 2L=false} (LinkedHashMap@%s)>",
                                         toHexString(withTreeMap.map.hashCode()),
                                         toHexString(withLinkedHashMap.map.hashCode())));
  }

}
