/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.scripts;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.writeString;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.scripts.ShellScriptExecutor.execute;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author XiaoMingZHM, Eveneko
 */
class Convert_JUnit_Assertions_To_AssertJ_Test {

  @TempDir
  private static Path tempDir;

  private static Path script;

  @BeforeAll
  static void setUp() throws IOException {
    script = copy(ClasspathResources.resourcePath("convert-junit-assertions-to-assertj.sh"), tempDir.resolve("script.sh"));
  }

  @ParameterizedTest
  @MethodSource({
      "replace_assertEquals_with_isEmpty",
      "replace_assertEquals_with_hasSize",
      "replace_assertEquals_with_isCloseTo",
      "replace_assertEquals_with_isEqualTo",
      "replace_assertArrayEquals_with_isEqualTo",
      "replace_assertNull_with_isNull",
      "replace_assertSame_with_isSameAs",
      "replace_imports"
  })
  void test(String input, String expected) throws Exception {
    // GIVEN
    Path inputFile = writeString(tempDir.resolve("input.txt"), input);
    // WHEN
    execute(script, inputFile);
    // THEN
    then(inputFile).hasContent(expected);
  }

  static Stream<Object> replace_assertEquals_with_isEmpty() {
    return Stream.of(arguments("assertEquals(0, myList.size());\nassertEquals(  0,  myList.size());\n",
                               "assertThat(myList).isEmpty();\nassertThat(myList).isEmpty();\n"),
                     arguments("assertEquals(  0,  (new String(\"\")).size());\nassertEquals(  0,  multiParam( 1.1, param2).size());\n",
                               "assertThat((new String(\"\"))).isEmpty();\nassertThat(multiParam( 1.1, param2)).isEmpty();\n"),
                     arguments("assertEquals(  0,  (new String(\"  ,  \")).size());\nassertEquals(  0,  \"  ,  \".size());\n",
                               "assertThat((new String(\"  ,  \"))).isEmpty();\nassertThat(\"  ,  \").isEmpty();\n"));
  }

  static Stream<Object> replace_assertEquals_with_hasSize() {
    return Stream.of(arguments("assertEquals(1234, myList.size());\nassertEquals(1234, (new int[1234]).size());\n",
                               "assertThat(myList).hasSize(1234);\nassertThat((new int[1234])).hasSize(1234);\n"),
                     arguments("assertEquals( 1234, myList(123).size());\nassertEquals( 1234, (\"12.\" + \",123\").size());\n",
                               "assertThat(myList(123)).hasSize(1234);\nassertThat((\"12.\" + \",123\")).hasSize(1234);\n"),
                     arguments("assertEquals(12, multiParam(1.1,param2).size());\nassertEquals( 123, multiParam(1.1, param2, hello[i]).size());\n",
                               "assertThat(multiParam(1.1,param2)).hasSize(12);\nassertThat(multiParam(1.1, param2, hello[i])).hasSize(123);\n"));
  }

  static Stream<Object> replace_assertEquals_with_isCloseTo() {
    return Stream.of(arguments("assertEquals(12.34, 13.45, 0.1);\n",
                               "assertThat(13.45).isCloseTo(12.34, within(0.1));\n"),
                     arguments("assertEquals(expected.size(), value, EPSILON);\nassertEquals( 4, (new Array(3)).size(), EPSILON);\n",
                               "assertThat(value).isCloseTo(expected.size(), within(EPSILON));\nassertThat((new Array(3)).size()).isCloseTo(4, within(EPSILON));\n"));
  }

  static Stream<Object> replace_assertEquals_with_isEqualTo() {
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

  static Stream<Object> replace_assertArrayEquals_with_isEqualTo() {
    return Stream.of(arguments("assertArrayEquals(expectedArray, actual);\n",
                               "assertThat(actual).isEqualTo(expectedArray);\n"),
                     arguments("assertArrayEquals(\"123,4,56\".getBytes(), actual);\n",
                               "assertThat(actual).isEqualTo(\"123,4,56\".getBytes());\n"),
                     arguments("assertArrayEquals(houses.getList(\"house_name\"), actual);\n",
                               "assertThat(actual).isEqualTo(houses.getList(\"house_name\"));\n"),
                     arguments("assertArrayEquals(houses.getList(\" \\\",123 \", \"house_name\"), actual);\n",
                               "assertThat(actual).isEqualTo(houses.getList(\" \\\",123 \", \"house_name\"));\n"));
  }

  static Stream<Object> replace_assertNull_with_isNull() {
    return Stream.of(arguments("assertNull(actual);\n",
                               "assertThat(actual).isNull();\n"),
                     arguments("assertNull(Invoker.invoke(clazz, args));\n",
                               "assertThat(Invoker.invoke(clazz, args)).isNull();\n"),
                     arguments("assertNull(\"12,41\".getBytes());\n",
                               "assertThat(\"12,41\".getBytes()).isNull();\n"),
                     arguments("assertNull(calculate(abcd, \",\\\",123\\\",\", 1234));\n",
                               "assertThat(calculate(abcd, \",\\\",123\\\",\", 1234)).isNull();\n"));
  }

  static Stream<Object> replace_assertSame_with_isSameAs() {
    // similar to the test source in assertEquals by default
    return Stream.of(
                     arguments("assertSame(2.14, actual);\n",
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

  static Stream<Object> replace_imports() {
    return Stream.of(
                     arguments("import static org.junit.Assert.assertEquals;\n",
                               "import static org.assertj.core.api.Assertions.assertThat;\n"),
                     arguments("import static org.junit.Assert.fail;\n",
                               "import static org.assertj.core.api.Assertions.fail;\n"),
                     arguments("import static org.junit.Assert.*;\n",
                               "import static org.assertj.core.api.Assertions.*;\n"),
                     arguments("import static org!junit.Assert.assertEquals;\n",
                               "import static org!junit.Assert.assertEquals;\n"),
                     arguments("import static org.junit.Assert...fail;\n",
                               "import static org.junit.Assert...fail;\n"),
                     arguments("import static org.junit.Assert.....Test;\n",
                               "import static org.junit.Assert.....Test;\n"));
  }

}
