package org.assertj.scripts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static java.lang.String.format;

public class Convert_Junit_Assertions_To_Assertj_Test {
  private Shell_Script_Conversion_Test_Invoker tester;

  @BeforeEach
  public void init() {
    tester = new Shell_Script_Conversion_Test_Invoker(
      "sh",
      "src/test/java/org/assertj/scripts/Shell_Script_Conversion_Test_Invoker_Buffer.java",
      "src/main/scripts/convert-junit-assertions-to-assertj.sh"
    );

  }

  @Test
  public void Script_Test_Template() throws Exception {

    String input = format("assertEquals(0, myList.size());%n");
    String expected = format("assertThat(myList).isEmpty();%n");
    tester.Start_Test(input, expected);

  }

  @Test
  //Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
  public void Replace_Type_1() throws Exception {
    String input = format("assertEquals(0, myList.size());%n");
    String expected = format("assertThat(myList).isEmpty();%n");

    input += format("assertEquals(  0,  myList.size());%n");
    expected += format("assertThat(myList).isEmpty();%n");

    input += format("assertEquals(  0,  (new String(\"\")).size());%n");
    expected += format("assertThat((new String(\"\"))).isEmpty();%n");
    tester.Start_Test(input, expected);

    input = format("assertEquals(  0,  multiParam( 1.1, param2).size());%n");
    expected = format("assertThat(multiParam( 1.1, param2)).isEmpty();%n");

    input += format("assertEquals(  0,  (new String(\"  ,  \")).size());%n");
    expected += format("assertThat((new String(\"  ,  \"))).isEmpty();%n");

    input += format("assertEquals(  0,  \"  ,  \".size());%n");
    expected += format("assertThat(\"  ,  \").isEmpty();%n");
    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
  public void Replace_Type_2() throws Exception {
    String input = format("assertEquals(1234, myList.size());%n");
    String expected = format("assertThat(myList).hasSize(1234);%n");

    input += format("assertEquals(1234, (new int[1234]).size());%n");
    expected += format("assertThat((new int[1234])).hasSize(1234);%n");

    input += format("assertEquals( 1234, myList(123).size());%n");
    expected += format("assertThat(myList(123)).hasSize(1234);%n");

    input += format("assertEquals( 1234, (\"12.\" + \",123\").size());%n");
    expected += format("assertThat((\"12.\" + \",123\")).hasSize(1234);%n");

    input += format("assertEquals(12, multiParam(1.1,param2).size());%n");
    expected += format("assertThat(multiParam(1.1,param2)).hasSize(12);%n");

    input += format("assertEquals( 123, multiParam(1.1, param2, hello[i]).size());%n");
    expected += format("assertThat(multiParam(1.1, param2, hello[i])).hasSize(123);%n");

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void Replace_Type_3() throws Exception {
    String input = format("assertEquals(12.34, 13.45, 0.1);%n");
    String expected = format("assertThat(13.45).isCloseTo(12.34, within(0.1));%n");

    input += format("assertEquals(expected.size(), value, EPSILON);%n");
    expected += format("assertThat(value).isCloseTo(expected.size(), within(EPSILON));%n");

    input += format("assertEquals( 4, (new Array(3)).size(), EPSILON);%n");
    expected += format("assertThat((new Array(3)).size()).isCloseTo(4, within(EPSILON));%n");

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)
  public void Replace_Type_4() throws Exception {
    String input = format("assertEquals(expected, actual);%n");
    String expected = format("assertThat(actual).isEqualTo(expected);%n");

    input += format("assertEquals(2.14, actual);%n");
    expected += format("assertThat(actual).isEqualTo(2.14);%n");

    input += format("assertEquals(2.14, actual);%n");
    expected += format("assertThat(actual).isEqualTo(2.14);%n");

    tester.Start_Test(input, expected);

    input = format("assertEquals(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));%n");
    expected = format("assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isEqualTo(\"12.34\");%n");

    input += format("assertEquals(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isEqualTo(\"34.34\");%n");

    input += format("assertEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isEqualTo(\"1234.34\");%n");

    input += format("assertEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isEqualTo(\"1234.34\");%n");

    input += format("assertEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isEqualTo(\"1234567.34\");%n");

    input += format("assertEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isEqualTo(\"1234567.34\");%n");

    tester.Start_Test(input, expected);

    input = format("assertEquals(\"123,567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));%n");
    expected = format("assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isEqualTo(\"123,567.34\");%n");

    input += format("assertEquals(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isEqualTo(\"123\\\",5\\\"67.34\");%n");

    input += format("assertEquals(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\")).isEqualTo(\"123\\\",5\\\"67.34\");%n");
    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)
  public void Replace_Type_5() throws Exception {
    String input = format("assertArrayEquals(expectedArray, actual);%n");
    String expected = format("assertThat(actual).isEqualTo(expectedArray);%n");

    input += format("assertArrayEquals(\"123,4,56\".getBytes(), actual);%n");
    expected += format("assertThat(actual).isEqualTo(\"123,4,56\".getBytes());%n");

    input += format("assertArrayEquals(houses.getList(\"house_name\"), actual);%n");
    expected += format("assertThat(actual).isEqualTo(houses.getList(\"house_name\"));%n");

    input += format("assertArrayEquals(houses.getList(\" \\\",123 \", \"house_name\"), actual);%n");
    expected += format("assertThat(actual).isEqualTo(houses.getList(\" \\\",123 \", \"house_name\"));%n");

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()
  public void Replace_Type_6() throws Exception {
    String input = format("assertNull(actual);%n");
    String expected = format("assertThat(actual).isNull();%n");

    input += format("assertNull(Invoker.invoke(clazz, args));%n");
    expected += format("assertThat(Invoker.invoke(clazz, args)).isNull();%n");

    input += format("assertNull(\"12,41\".getBytes());%n");
    expected += format("assertThat(\"12,41\".getBytes()).isNull();%n");

    input += format("assertNull(calculate(abcd, \",\\\",123\\\",\", 1234));%n");
    expected += format("assertThat(calculate(abcd, \",\\\",123\\\",\", 1234)).isNull();%n");

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)
  public void Replace_Type_10() throws Exception {
    // same as the testcases in Type 4
    String input = format("assertSame(expected, actual);%n");
    String expected = format("assertThat(actual).isSameAs(expected);%n");

    input += format("assertSame(2.14, actual);%n");
    expected += format("assertThat(actual).isSameAs(2.14);%n");

    input += format("assertSame(2.14, actual);%n");
    expected += format("assertThat(actual).isSameAs(2.14);%n");

    tester.Start_Test(input, expected);

    input = format("assertSame(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));%n");
    expected = format("assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isSameAs(\"12.34\");%n");

    input += format("assertSame(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isSameAs(\"34.34\");%n");

    input += format("assertSame(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isSameAs(\"1234.34\");%n");

    input += format("assertSame(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isSameAs(\"1234.34\");%n");

    input += format("assertSame(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isSameAs(\"1234567.34\");%n");

    input += format("assertSame(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isSameAs(\"1234567.34\");%n");

    tester.Start_Test(input, expected);

    input = format("assertSame(\"123,567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));%n");
    expected = format("assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isSameAs(\"123,567.34\");%n");

    input += format("assertSame(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isSameAs(\"123\\\",5\\\"67.34\");%n");

    input += format("assertSame(\"123\\\",5\\\"67.34\", StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\"));%n");
    expected += format("assertThat(StringHandling.fixFPNumberFormat(\"1.234\\.567\\,34\")).isSameAs(\"123\\\",5\\\"67.34\");%n");
    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing JUnit static imports by AssertJ ones, at this point you will probably need to
  public void Replace_Type_12() throws Exception {
    String input = format("import static org.junit.Assert.assertEquals;%n");
    String expected = format("import static org.assertj.core.api.Assertions.assertThat;%n");

    input += format("import static org.junit.Assert.fail;%n");
    expected += format("import static org.assertj.core.api.Assertions.fail;%n");

    input += format("import static org.junit.Assert.*;%n");
    expected += format("import static org.assertj.core.api.Assertions.*;%n");

    tester.Start_Test(input, expected);
    input = format("import static org!junit.Assert.assertEquals;%n");
    expected = format("import static org!junit.Assert.assertEquals;%n");

    input += format("import static org.junit.Assert...fail;%n");
    expected += format("import static org.junit.Assert...fail;%n");

    input += format("import static org.junit.Assert.....Test;%n");
    expected += format("import static org.junit.Assert.....Test;%n");
    tester.Start_Test(input, expected);
  }

}
