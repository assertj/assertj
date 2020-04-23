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
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.Jedi;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldContainOnly#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContainOnly_create_Test {

  private static final ComparatorBasedComparisonStrategy CASE_INSENSITIVE_COMPARISON_STRATEGY = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance);

  @Test
  public void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(list("Yoda", "Han"), list("Luke", "Yoda"),
                                                    newLinkedHashSet("Luke"), newLinkedHashSet("Han"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting ArrayList:%n"
                                   + "  <[\"Yoda\", \"Han\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"Luke\", \"Yoda\"]>%n"
                                   + "element(s) not found:%n"
                                   + "  <[\"Luke\"]>%n"
                                   + "and element(s) not expected:%n"
                                   + "  <[\"Han\"]>%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(list("Yoda", "Han"),
                                                    list("Luke", "Yoda"),
                                                    newLinkedHashSet("Luke"),
                                                    newLinkedHashSet("Han"),
                                                    CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting ArrayList:%n"
                                   + "  <[\"Yoda\", \"Han\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"Luke\", \"Yoda\"]>%n"
                                   + "element(s) not found:%n"
                                   + "  <[\"Luke\"]>%n"
                                   + "and element(s) not expected:%n"
                                   + "  <[\"Han\"]>%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  public void should_not_display_unexpected_elements_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(list("Yoda"),
                                                    list("Luke", "Yoda"),
                                                    newLinkedHashSet("Luke"),
                                                    emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting ArrayList:%n"
                                   + "  <[\"Yoda\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"Luke\", \"Yoda\"]>%n"
                                   + "but could not find the following element(s):%n"
                                   + "  <[\"Luke\"]>%n"));
  }

  @Test
  public void should_not_display_unexpected_elements_when_there_are_none_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(list("Yoda"),
                                                    list("Luke", "Yoda"),
                                                    newLinkedHashSet("Luke"),
                                                    Collections.emptySet(),
                                                    CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting ArrayList:%n"
                                   + "  <[\"Yoda\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"Luke\", \"Yoda\"]>%n"
                                   + "but could not find the following element(s):%n"
                                   + "  <[\"Luke\"]>%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  public void should_not_display_elements_not_found_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(list("Yoda", "Leia"),
                                                    list("Yoda"),
                                                    emptySet(),
                                                    newLinkedHashSet("Leia"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting ArrayList:%n"
                                   + "  <[\"Yoda\", \"Leia\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"Yoda\"]>%n"
                                   + "but the following element(s) were unexpected:%n"
                                   + "  <[\"Leia\"]>%n"));
  }

  @Test
  public void should_not_display_elements_not_found_when_there_are_none_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(list("Yoda", "Leia"),
                                                    list("Yoda"),
                                                    emptySet(),
                                                    newLinkedHashSet("Leia"),
                                                    CASE_INSENSITIVE_COMPARISON_STRATEGY);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting ArrayList:%n"
                                   + "  <[\"Yoda\", \"Leia\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"Yoda\"]>%n"
                                   + "but the following element(s) were unexpected:%n"
                                   + "  <[\"Leia\"]>%n"
                                   + "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  public void should_create_error_message_unexpected_for_map() {
    // GIVEN
    Map<String, String> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));
    ErrorMessageFactory factory = shouldContainOnly(map,
                                                    expected,
                                                    emptySet(),
                                                    set(entry("color", "green")));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting map:%n"
                                   + "  <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%n"
                                   + "to contain only:%n"
                                   + "  <[MapEntry[key=\"name\", value=\"Yoda\"]]>%n"
                                   + "but the following map entries were unexpected:%n"
                                   + "  <[MapEntry[key=\"color\", value=\"green\"]]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_for_map() {
    // GIVEN
    Map<String, String> map = mapOf(entry("name", "Yoda"));
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainOnly(map,
                                                    expected,
                                                    set(entry("color", "green")),
                                                    emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting map:%n"
                                   + "  <{\"name\"=\"Yoda\"}>%n"
                                   + "to contain only:%n"
                                   + "  <[MapEntry[key=\"name\", value=\"Yoda\"], MapEntry[key=\"color\", value=\"green\"]]>%n"
                                   + "but could not find the following map entries:%n"
                                   + "  <[MapEntry[key=\"color\", value=\"green\"]]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_and_unexpected_for_map() {
    // GIVEN
    Map<String, String> map = mapOf(entry("name", "Yoda"));
    MapEntry<String, String>[] expected = array(entry("color", "green"));
    ErrorMessageFactory factory = shouldContainOnly(map,
                                                    expected,
                                                    set(entry("color", "green")),
                                                    set(entry("name", "Yoda")));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting map:%n"
                                   + "  <{\"name\"=\"Yoda\"}>%n"
                                   + "to contain only:%n"
                                   + "  <[MapEntry[key=\"color\", value=\"green\"]]>%n"
                                   + "map entries not found:%n"
                                   + "  <[MapEntry[key=\"color\", value=\"green\"]]>%n"
                                   + "and map entries not expected:%n"
                                   + "  <[MapEntry[key=\"name\", value=\"Yoda\"]]>%n"));
  }

  @Test
  public void should_create_error_message_unexpected_for_float_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new float[] { 6f, 8f, 7f },
                                                    new float[] { 6f, 8f },
                                                    emptySet(),
                                                    newLinkedHashSet(7f));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting float[]:%n"
                                   + "  <[6.0f, 8.0f, 7.0f]>%n"
                                   + "to contain only:%n"
                                   + "  <[6.0f, 8.0f]>%n"
                                   + "but the following float(s) were unexpected:%n"
                                   + "  <[7.0f]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_for_char_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new char[] { 'a' },
                                                    new char[] { 'a', 'b' },
                                                    newLinkedHashSet('b'),
                                                    emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting char[]:%n"
                                   + "  <['a']>%n"
                                   + "to contain only:%n"
                                   + "  <['a', 'b']>%n"
                                   + "but could not find the following char(s):%n"
                                   + "  <['b']>%n"));
  }

  @Test
  public void should_create_error_message_not_found_and_unexpected_for_long_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new long[] { 5L, 6L },
                                                    new long[] { 3L, 6L },
                                                    newLinkedHashSet(3L),
                                                    newLinkedHashSet(5L));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting long[]:%n"
                                   + "  <[5L, 6L]>%n"
                                   + "to contain only:%n"
                                   + "  <[3L, 6L]>%n"
                                   + "long(s) not found:%n"
                                   + "  <[3L]>%n"
                                   + "and long(s) not expected:%n"
                                   + "  <[5L]>%n"));
  }

  @Test
  public void should_create_error_message_unexpected_for_boolean_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new boolean[] { true, false },
                                                    new boolean[] { false },
                                                    emptySet(),
                                                    newLinkedHashSet(true));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting boolean[]:%n"
                                   + "  <[true, false]>%n"
                                   + "to contain only:%n"
                                   + "  <[false]>%n"
                                   + "but the following boolean(s) were unexpected:%n"
                                   + "  <[true]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_for_double_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new double[] { 1.0 },
                                                    new double[] { 1.0, 2.0 },
                                                    newLinkedHashSet(2.0),
                                                    emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting double[]:%n"
                                   + "  <[1.0]>%n"
                                   + "to contain only:%n"
                                   + "  <[1.0, 2.0]>%n"
                                   + "but could not find the following double(s):%n"
                                   + "  <[2.0]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_and_unexpected_for_short_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new short[] { 5, 6 },
                                                    new short[] { 3, 6 },
                                                    newLinkedHashSet((short) 3),
                                                    newLinkedHashSet((short) 5));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting short[]:%n"
                                   + "  <[5, 6]>%n"
                                   + "to contain only:%n"
                                   + "  <[3, 6]>%n"
                                   + "short(s) not found:%n"
                                   + "  <[3]>%n"
                                   + "and short(s) not expected:%n"
                                   + "  <[5]>%n"));
  }

  @Test
  public void should_create_error_message_unexpected_for_int_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new int[] { 1, 2 },
                                                    new int[] { 2 },
                                                    emptySet(),
                                                    newLinkedHashSet(1));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting int[]:%n"
                                   + "  <[1, 2]>%n"
                                   + "to contain only:%n"
                                   + "  <[2]>%n"
                                   + "but the following int(s) were unexpected:%n"
                                   + "  <[1]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_for_byte_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new byte[] { 1 },
                                                    new byte[] { 1, 2 },
                                                    newLinkedHashSet((byte) 2),
                                                    emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting byte[]:%n"
                                   + "  <[1]>%n"
                                   + "to contain only:%n"
                                   + "  <[1, 2]>%n"
                                   + "but could not find the following byte(s):%n"
                                   + "  <[2]>%n"));
  }

  @Test
  public void should_create_error_message_unexpected_for_String_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(new String[] { "1", "2" },
                                                    new String[] { "2" },
                                                    emptySet(),
                                                    newLinkedHashSet("1"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting String[]:%n"
                                   + "  <[\"1\", \"2\"]>%n"
                                   + "to contain only:%n"
                                   + "  <[\"2\"]>%n"
                                   + "but the following string(s) were unexpected:%n"
                                   + "  <[\"1\"]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_for_set() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnly(newLinkedHashSet(1),
                                                    newLinkedHashSet(1, 2),
                                                    newLinkedHashSet(2),
                                                    emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting LinkedHashSet:%n"
                                   + "  <[1]>%n"
                                   + "to contain only:%n"
                                   + "  <[1, 2]>%n"
                                   + "but could not find the following element(s):%n"
                                   + "  <[2]>%n"));
  }

  @Test
  public void should_create_error_message_not_found_and_unexpected_for_custom_object() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "green");
    Jedi expected = new Jedi("Luke", "blue");
    ErrorMessageFactory factory = shouldContainOnly(array(actual),
                                                    array(expected),
                                                    newLinkedHashSet(expected),
                                                    newLinkedHashSet(actual));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting Jedi[]:%n"
                                   + "  <[Yoda the Jedi]>%n"
                                   + "to contain only:%n"
                                   + "  <[Luke the Jedi]>%n"
                                   + "jedi(s) not found:%n"
                                   + "  <[Luke the Jedi]>%n"
                                   + "and jedi(s) not expected:%n"
                                   + "  <[Yoda the Jedi]>%n"));
  }

  private static <K, V> HashSet<MapEntry<K, V>> set(MapEntry<K, V> entry) {
    HashSet<MapEntry<K, V>> set = new HashSet<>();
    set.add(entry);
    return set;
  }

}
