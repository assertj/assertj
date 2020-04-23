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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.directoryShouldContain;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.test.Jedi;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldContain#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContain_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(list("Yoda"), list("Luke", "Yoda"), newLinkedHashSet("Luke"));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting ArrayList:%n" +
                                   " <[\"Yoda\"]>%n" +
                                   "to contain:%n" +
                                   " <[\"Luke\", \"Yoda\"]>%n" +
                                   "but could not find the following element(s):%n" +
                                   " <[\"Luke\"]>%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(list("Yoda"), list("Luke", "Yoda"), newLinkedHashSet("Luke"),
                                                new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting ArrayList:%n" +
                                   " <[\"Yoda\"]>%n" +
                                   "to contain:%n" +
                                   " <[\"Luke\", \"Yoda\"]>%n" +
                                   "but could not find the following element(s):%n" +
                                   " <[\"Luke\"]>%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

  @Test
  public void should_create_error_message_differentiating_long_from_integer_in_arrays() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(list(5L, 7L), list(5, 7), newLinkedHashSet(5, 7));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting ArrayList:%n" +
                                   " <[5L, 7L]>%n" +
                                   "to contain:%n" +
                                   " <[5, 7]>%n" +
                                   "but could not find the following element(s):%n" +
                                   " <[5, 7]>%n"));
  }

  @Test
  public void should_create_error_message_differentiating_double_from_float() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(list(5d, 7d), list(5f, 7f), newLinkedHashSet(5f, 7f));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting ArrayList:%n" +
                                   " <[5.0, 7.0]>%n" +
                                   "to contain:%n" +
                                   " <[5.0f, 7.0f]>%n" +
                                   "but could not find the following element(s):%n" +
                                   " <[5.0f, 7.0f]>%n"));
  }

  @Test
  public void should_create_error_message_for_map() {
    // GIVEN
    Map<String, Double> map = mapOf(MapEntry.entry("1", 2d));
    ErrorMessageFactory factory = shouldContain(map, MapEntry.entry("3", 4d), MapEntry.entry("3", 4d));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting map:%n"
                                   + " <{\"1\"=2.0}>%n"
                                   + "to contain:%n"
                                   + " <MapEntry[key=\"3\", value=4.0]>%n"
                                   + "but could not find the following map entries:%n"
                                   + " <MapEntry[key=\"3\", value=4.0]>%n"));
  }

  @Test
  public void should_create_error_message_for_byte_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new byte[] { 2, 3 }, new byte[] { 4 }, new byte[] { 4 });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting byte[]:%n"
                                   + " <[2, 3]>%n"
                                   + "to contain:%n"
                                   + " <[4]>%n"
                                   + "but could not find the following byte(s):%n"
                                   + " <[4]>%n"));
  }

  @Test
  public void should_create_error_message_for_float_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new float[] { 2f, 3f }, new float[] { 4f }, new float[] { 4f });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting float[]:%n"
                                   + " <[2.0f, 3.0f]>%n"
                                   + "to contain:%n"
                                   + " <[4.0f]>%n"
                                   + "but could not find the following float(s):%n"
                                   + " <[4.0f]>%n"));
  }

  @Test
  public void should_create_error_message_for_int_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new int[] { 2, 3 }, new int[] { 4 }, new int[] { 4 });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting int[]:%n"
                                   + " <[2, 3]>%n"
                                   + "to contain:%n"
                                   + " <[4]>%n"
                                   + "but could not find the following int(s):%n"
                                   + " <[4]>%n"));
  }

  @Test
  public void should_create_error_message_for_char_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new char[] { 'a', 'b' }, new char[] { 'c', 'd' }, new char[] { 'c', 'd' });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting char[]:%n"
                                   + " <['a', 'b']>%n"
                                   + "to contain:%n"
                                   + " <['c', 'd']>%n"
                                   + "but could not find the following char(s):%n"
                                   + " <['c', 'd']>%n"));
  }

  @Test
  public void should_create_error_message_for_long_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new long[] { 6L, 8L }, new long[] { 10L, 9L }, new long[] { 10L, 9L });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting long[]:%n"
                                   + " <[6L, 8L]>%n"
                                   + "to contain:%n"
                                   + " <[10L, 9L]>%n"
                                   + "but could not find the following long(s):%n"
                                   + " <[10L, 9L]>%n"));
  }

  @Test
  public void should_create_error_message_for_double_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new double[] { 6, 8 }, new double[] { 10, 9 }, new double[] { 10, 9 });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting double[]:%n"
                                   + " <[6.0, 8.0]>%n"
                                   + "to contain:%n"
                                   + " <[10.0, 9.0]>%n"
                                   + "but could not find the following double(s):%n"
                                   + " <[10.0, 9.0]>%n"));
  }

  @Test
  public void should_create_error_message_for_boolean_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new boolean[] { true }, new boolean[] { true, false }, new boolean[] { false });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting boolean[]:%n"
                                   + " <[true]>%n"
                                   + "to contain:%n"
                                   + " <[true, false]>%n"
                                   + "but could not find the following boolean(s):%n"
                                   + " <[false]>%n"));
  }

  @Test
  public void should_create_error_message_for_short_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new short[] { 6, 8 }, new short[] { 10, 9 }, new short[] { 10, 9 });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting short[]:%n"
                                   + " <[6, 8]>%n"
                                   + "to contain:%n"
                                   + " <[10, 9]>%n"
                                   + "but could not find the following short(s):%n"
                                   + " <[10, 9]>%n"));
  }

  @Test
  public void should_create_error_message_for_String_array() {
    // GIVEN
    ErrorMessageFactory factory = shouldContain(new String[] { "a" }, new String[] { "b" }, new String[] { "b" });
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting String[]:%n"
                                   + " <[\"a\"]>%n"
                                   + "to contain:%n"
                                   + " <[\"b\"]>%n"
                                   + "but could not find the following string(s):%n"
                                   + " <[\"b\"]>%n"));
  }

  @Test
  public void should_create_error_message_for_custom_class_array() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi expected = new Jedi("Luke", "blue");
    // GIVEN
    ErrorMessageFactory factory = shouldContain(array(actual), array(expected), array(expected));
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting Jedi[]:%n"
                                   + " <[Yoda the Jedi]>%n"
                                   + "to contain:%n"
                                   + " <[Luke the Jedi]>%n"
                                   + "but could not find the following jedi(s):%n"
                                   + " <[Luke the Jedi]>%n"));
  }

  @Test
  public void should_create_error_message_for_file_directory() {
    // GIVEN
    File directory = mock(File.class);
    given(directory.getAbsolutePath()).willReturn("root");
    ErrorMessageFactory factory = directoryShouldContain(directory, list("foo.txt", "bar.txt"), "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting directory:%n" +
                                   "  <root>%n" +
                                   "to contain at least one file matching glob:**.java but there was none.%n" +
                                   "The directory content was:%n" +
                                   "  [foo.txt, bar.txt]"));
  }

  @Test
  void should_create_error_message_for_file_directory_escaping_percent() {
    // GIVEN
    File directory = mock(File.class);
    given(directory.getAbsolutePath()).willReturn("root%dir");
    ErrorMessageFactory factory = directoryShouldContain(directory, list("foo%1.txt", "bar%2.txt"), "glob:**%Test.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting directory:%n" +
                                   "  <root%%dir>%n" +
                                   "to contain at least one file matching glob:**%%Test.java but there was none.%n" +
                                   "The directory content was:%n" +
                                   "  [foo%%1.txt, bar%%2.txt]"));
  }

  @Test
  public void should_create_error_message_for_path_directory() {
    // GIVEN
    Path directory = mock(Path.class);
    given(directory.toString()).willReturn("root");
    ErrorMessageFactory factory = directoryShouldContain(directory, list("foo.txt", "bar.txt"), "glob:**.java");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting directory:%n" +
                                   "  <root>%n" +
                                   "to contain at least one file matching glob:**.java but there was none.%n" +
                                   "The directory content was:%n" +
                                   "  [foo.txt, bar.txt]"));
  }

}
