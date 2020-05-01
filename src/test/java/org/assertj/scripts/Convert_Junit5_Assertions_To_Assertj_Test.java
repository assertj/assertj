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
package org.assertj.scripts;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for convert script {@code src/main/scripts/convert-junit5-assertions-to-assertj.sh}.
 *
 * @author XiaoMingZHM, Eveneko
 */
@DisplayName("Convert JUnit5 assertions to AssertJ")
public class Convert_Junit5_Assertions_To_Assertj_Test {
  private ShellScriptInvoker tester;

  @BeforeEach
  public void init() {
    tester = new ShellScriptInvoker("src/main/scripts/convert-junit5-assertions-to-assertj.sh");
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("script_test_template_source")
  public void script_test_template(String input, String expected) throws Exception {
    tester.startTest(input, expected);
  }

  static Stream<Object> script_test_template_source() {
    return Stream.of(arguments("assertEquals(0, myList.size());\n",
                               "assertThat(myList).isEmpty();\n"),
                     arguments("assertEquals(0, myList.size());\n",
                               "assertThat(myList).isEmpty();\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertEquals_checking_0_size_source")
  // Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
  public void replace_assertEquals_checking_0_size(String input, String expected) throws Exception {
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertEquals_checking_0_size_source() {
    return Stream.of(arguments("assertEquals(0, myList.size());\n" + "assertEquals(  0,  myList.size());\n",
                               "assertThat(myList).isEmpty();\n" + "assertThat(myList).isEmpty();\n"),
                     arguments("assertEquals(  0,  (new String(\"\")).size());\n"
                               + "assertEquals(  0,  multiParam( 1.1, param2).size());\n",
                               "assertThat((new String(\"\"))).isEmpty();\n" + "assertThat(multiParam( 1.1, param2)).isEmpty();\n"),
                     arguments("assertEquals(  0,  (new String(\"  ,  \")).size());\n"
                               + "assertEquals(  0,  \"  ,  \".size());\n",
                               "assertThat((new String(\"  ,  \"))).isEmpty();\n" + "assertThat(\"  ,  \").isEmpty();\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertEquals_checking_size_source")
  // Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
  public void replace_assertEquals_checking_size(String input, String expected) throws Exception {
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertEquals_checking_size_source() {
    return Stream.of(arguments("assertEquals(1234, myList.size());\n" +
                               "assertEquals(1234, (new int[1234]).size());\n",
                               "assertThat(myList).hasSize(1234);\n" +
                                                                                "assertThat((new int[1234])).hasSize(1234);\n"),
                     arguments("assertEquals( 1234, myList(123).size());\n" +
                               "assertEquals( 1234, (\"12.\" + \",123\").size());\n",
                               "assertThat(myList(123)).hasSize(1234);\n" +
                                                                                      "assertThat((\"12.\" + \",123\")).hasSize(1234);\n"),
                     arguments("assertEquals(12, multiParam(1.1,param2).size());\n" +
                               "assertEquals( 123, multiParam(1.1, param2, hello[i]).size());\n",
                               "assertThat(multiParam(1.1,param2)).hasSize(12);\n" +
                                                                                                  "assertThat(multiParam(1.1, param2, hello[i])).hasSize(123);\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertEquals_checking_isCloseTo_source")
  // Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void replace_assertEquals_checking_isCloseTo(String input, String expected) throws Exception {
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertEquals_checking_isCloseTo_source() {
    return Stream.of(arguments("assertEquals(12.34, 13.45, 0.1);\n",
                               "assertThat(13.45).isCloseTo(12.34, within(0.1));\n"),
                     arguments("assertEquals(expected.size(), value, EPSILON);\n" +
                               "assertEquals( 4, (new Array(3)).size(), EPSILON);\n",
                               "assertThat(value).isCloseTo(expected.size(), within(EPSILON));\n" +
                                                                                      "assertThat((new Array(3)).size()).isCloseTo(4, within(EPSILON));\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertEquals_by_default_source")
  // Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)
  public void replace_assertEquals_by_default(String input, String expected) throws Exception {
    // multi lines for one test should be considered.
    input += "assertEquals(expected, actual);\n";
    expected += "assertThat(actual).isEqualTo(expected);\n";
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertEquals_by_default_source() {
    return Stream.of(arguments("assertEquals(2.14, actual);\n",
                               "assertThat(actual).isEqualTo(2.14);\n"),
                     arguments("assertEquals(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isEqualTo(\"12.34\");\n"),
                     arguments("assertEquals(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isEqualTo(\"34.34\");\n"),
                     arguments("assertEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isEqualTo(\"1234.34\");\n"),
                     arguments("assertEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isEqualTo(\"1234.34\");\n"),
                     arguments("assertEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isEqualTo(\"1234567.34\");\n"),
                     arguments("assertEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isEqualTo(\"1234567.34\");\n"),
                     arguments("assertEquals(\"123,567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isEqualTo(\"123,567.34\");\n"),
                     arguments("assertEquals(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isEqualTo(\"123\\\",5\\\"67.34\");\n"),
                     arguments("assertEquals(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\")).isEqualTo(\"123\\\",5\\\"67.34\");\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertNotEquals_source")
  // Replacing : assertNotEquals(expected, actual) ................. by : assertThat(actual).isNotEqualTo(expected)
  public void replace_assertNotEquals(String input, String expected) throws Exception {
    // multi lines for one test should be considered.
    input += "assertNotEquals(expected, actual);\n";
    expected += "assertThat(actual).isNotEqualTo(expected);\n";
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertNotEquals_source() {
    return Stream.of(arguments("assertNotEquals(2.14, actual);\n",
                               "assertThat(actual).isNotEqualTo(2.14);\n"),
                     arguments("assertNotEquals(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isNotEqualTo(\"12.34\");\n"),
                     arguments("assertNotEquals(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isNotEqualTo(\"34.34\");\n"),
                     arguments("assertNotEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isNotEqualTo(\"1234.34\");\n"),
                     arguments("assertNotEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isNotEqualTo(\"1234.34\");\n"),
                     arguments("assertNotEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isNotEqualTo(\"1234567.34\");\n"),
                     arguments("assertNotEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isNotEqualTo(\"1234567.34\");\n"),
                     arguments("assertNotEquals(\"123,567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isNotEqualTo(\"123,567.34\");\n"),
                     arguments("assertNotEquals(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isNotEqualTo(\"123\\\",5\\\"67.34\");\n"),
                     arguments("assertNotEquals(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\")).isNotEqualTo(\"123\\\",5\\\"67.34\");\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertArrayEquals_source")
  // Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)
  public void replace_assertArrayEquals(String input, String expected) throws Exception {
    // multi lines for one test should be considered.
    input += "assertArrayEquals(expectedArray, actual);\n";
    expected += "assertThat(actual).isEqualTo(expectedArray);\n";
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertArrayEquals_source() {
    return Stream.of(arguments("assertArrayEquals(expectedArray, actual);\n",
                               "assertThat(actual).isEqualTo(expectedArray);\n"),
                     arguments("assertArrayEquals(\"123,4,56\".getBytes(), actual);\n",
                               "assertThat(actual).isEqualTo(\"123,4,56\".getBytes());\n"),
                     arguments("assertArrayEquals(houses.getList(\"house_name\"), actual);\n",
                               "assertThat(actual).isEqualTo(houses.getList(\"house_name\"));\n"),
                     arguments("assertArrayEquals(houses.getList(\" \\\",123 \", \"house_name\"), actual);\n",
                               "assertThat(actual).isEqualTo(houses.getList(\" \\\",123 \", \"house_name\"));\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertNull_source")
  // Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()
  public void replace_assertNull(String input, String expected) throws Exception {
    input += "assertNull(actual);\n";
    expected += "assertThat(actual).isNull();\n";
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertNull_source() {
    return Stream.of(arguments("assertNull(actual);\n",
                               "assertThat(actual).isNull();\n"),
                     arguments("assertNull(Invoker.invoke(clazz, args));\n",
                               "assertThat(Invoker.invoke(clazz, args)).isNull();\n"),
                     arguments("assertNull(\"12,41\".getBytes());\n",
                               "assertThat(\"12,41\".getBytes()).isNull();\n"),
                     arguments("assertNull(calculate(abcd, \",\\\",123\\\",\", 1234));\n",
                               "assertThat(calculate(abcd, \",\\\",123\\\",\", 1234)).isNull();\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_assertSame_source")
  // Replacing : assertSame(expected, actual) ................. by : assertThat(actual).isSameAs(expected)
  public void replace_assertSame(String input, String expected) throws Exception {
    // multi lines for one test should be considered.
    input += "assertSame(expected, actual);\n";
    expected += "assertThat(actual).isSameAs(expected);\n";
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_assertSame_source() {
    // similar to the test source in assertEquals by default
    return Stream.of(arguments("assertSame(2.14, actual);\n",
                               "assertThat(actual).isSameAs(2.14);\n"),
                     arguments("assertSame(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isSameAs(\"12.34\");\n"),
                     arguments("assertSame(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isSameAs(\"34.34\");\n"),
                     arguments("assertSame(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isSameAs(\"1234.34\");\n"),
                     arguments("assertSame(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isSameAs(\"1234.34\");\n"),
                     arguments("assertSame(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isSameAs(\"1234567.34\");\n"),
                     arguments("assertSame(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isSameAs(\"1234567.34\");\n"),
                     arguments("assertSame(\"123,567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isSameAs(\"123,567.34\");\n"),
                     arguments("assertSame(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isSameAs(\"123\\\",5\\\"67.34\");\n"),
                     arguments("assertSame(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\"));\n",
                               "assertThat(StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\")).isSameAs(\"123\\\",5\\\"67.34\");\n"));
  }

  @ParameterizedTest(name = "{0} should be converted to {1}")
  @MethodSource("replace_imports_source")
  // Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void replace_imports(String input, String expected) throws Exception {
    input += "import static org.junit.jupiter.api.Assertions.fail;\n";
    expected += "import static org.assertj.core.api.Assertions.fail;\n";
    tester.startTest(input, expected);
  }

  static Stream<Object> replace_imports_source() {
    return Stream.of(arguments("import static org.junit.jupiter.api.Assertions.fail;\n",
                               "import static org.assertj.core.api.Assertions.fail;\n"),
                     arguments("import static org.junit.jupiter.api.Assertions.*;\n",
                               "import static org.assertj.core.api.Assertions.*;\n"),
                     arguments("import static org.junit.jupiter.api.Assertions...*;\n",
                               "import static org.junit.jupiter.api.Assertions...*;\n"),
                     arguments("import static org.junit!jupiter.api.Assertions.*;\n",
                               "import static org.junit!jupiter.api.Assertions.*;\n"));
  }

}
