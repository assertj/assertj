package org.assertj.scripts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

    String input = "assertEquals(0, myList.size());\n";
    String expected = "assertThat(myList).isEmpty();\n";
    tester.Start_Test(input, expected);

  }

  @Test
  //Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
  public void Replace_Type_1() throws Exception {
    String input = "assertEquals(0, myList.size());\n";
    String expected = "assertThat(myList).isEmpty();\n";

    input += "assertEquals(  0,  myList.size());\n";
    expected += "assertThat(myList).isEmpty();\n";

    input += "assertEquals(  0,  (new String(\"\")).size());\n";
    expected += "assertThat((new String(\"\"))).isEmpty();\n";
    tester.Start_Test(input, expected);

    input = "assertEquals(  0,  (new String(\"  ,  \")).size());\n";
    expected = "assertThat((new String(\"  ,  \"))).isEmpty();\n";

    input += "assertEquals(  0,  \"  ,  \".size());\n";
    expected += "assertThat(\"  ,  \").isEmpty();\n";
    tester.Start_Test(input, expected);

  }

  @Test
  //Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
  public void Replace_Type_2() throws Exception {
    String input = "assertEquals(1234, myList.size());\n";
    String expected = "assertThat(myList).hasSize(1234);\n";

    input += "assertEquals(1234, (new int[1234]).size());\n";
    expected += "assertThat((new int[1234])).hasSize(1234);\n";

    input += "assertEquals( 1234, myList(123).size());\n";
    expected += "assertThat(myList(123)).hasSize(1234);\n";

    input += "assertEquals( 1234, (\"12.\" + \",123\").size());\n";
    expected += "assertThat((\"12.\" + \",123\")).hasSize(1234);\n";

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void Replace_Type_3() throws Exception {
    String input = "assertEquals(12.34, 13.45, 0.1);\n";
    String expected = "assertThat(13.45).isCloseTo(12.34, within(0.1));\n";

    input += "assertEquals(expected.size(), value, EPSILON);\n";
    expected += "assertThat(value).isCloseTo(expected.size(), within(EPSILON));\n";

    input += "assertEquals( 4, (new Array(3)).size(), EPSILON);\n";
    expected += "assertThat((new Array(3)).size()).isCloseTo(4, within(EPSILON));\n";

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)
  public void Replace_Type_4() throws Exception {
    String input = "assertEquals(expected, actual);\n";
    String expected = "assertThat(actual).isEqualTo(expected);\n";

    input += "assertEquals(2.14, actual);\n";
    expected += "assertThat(actual).isEqualTo(2.14);\n";

    input += "assertEquals(2.14, actual);\n";
    expected += "assertThat(actual).isEqualTo(2.14);\n";

    tester.Start_Test(input, expected);

    input = "assertEquals(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));\n";
    expected = "assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isEqualTo(\"12.34\");\n";

    input += "assertEquals(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isEqualTo(\"34.34\");\n";

    input += "assertEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isEqualTo(\"1234.34\");\n";

    input += "assertEquals(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isEqualTo(\"1234.34\");\n";

    input += "assertEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isEqualTo(\"1234567.34\");\n";

    input += "assertEquals(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isEqualTo(\"1234567.34\");\n";

    tester.Start_Test(input, expected);

  }

  @Test
  //Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)
  public void Replace_Type_5() throws Exception {
    String input = "assertArrayEquals(expectedArray, actual);\n";
    String expected = "assertThat(actual).isEqualTo(expectedArray);\n";

    input += "assertArrayEquals(\"123,4,56\".getBytes(), actual);\n";
    expected += "assertThat(actual).isEqualTo(\"123,4,56\".getBytes());\n";

    input += "assertArrayEquals(houses.getList(\"house_name\"), actual);\n";
    expected += "assertThat(actual).isEqualTo(houses.getList(\"house_name\"));\n";

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()
  public void Replace_Type_6() throws Exception {
    String input = "assertNull(actual);\n";
    String expected = "assertThat(actual).isNull();\n";

    input += "assertNull(Invoker.invoke(clazz, args));\n";
    expected += "assertThat(Invoker.invoke(clazz, args)).isNull();\n";

    input += "assertNull(\"12,41\".getBytes());\n";
    expected += "assertThat(\"12,41\".getBytes()).isNull();\n";

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)
  public void Replace_Type_10() throws Exception {
    //TODO: Add more test
    String input = "assertSame(expected, actual);\n";
    String expected = "assertThat(actual).isSameAs(expected);\n";

    input += "assertSame(2.14, actual);\n";
    expected += "assertThat(actual).isSameAs(2.14);\n";

    input += "assertSame(2.14, actual);\n";
    expected += "assertThat(actual).isSameAs(2.14);\n";

    tester.Start_Test(input, expected);

    input = "assertSame(\"12.34\", StringHandling.fixFPNumberFormat(\"12.34\"));\n";
    expected = "assertThat(StringHandling.fixFPNumberFormat(\"12.34\")).isSameAs(\"12.34\");\n";

    input += "assertSame(\"34.34\", StringHandling.fixFPNumberFormat(\"34,34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"34,34\")).isSameAs(\"34.34\");\n";

    input += "assertSame(\"1234.34\", StringHandling.fixFPNumberFormat(\"1,234.34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1,234.34\")).isSameAs(\"1234.34\");\n";

    input += "assertSame(\"1234.34\", StringHandling.fixFPNumberFormat(\"1.234,34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1.234,34\")).isSameAs(\"1234.34\");\n";

    input += "assertSame(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1,234,567.34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1,234,567.34\")).isSameAs(\"1234567.34\");\n";

    input += "assertSame(\"1234567.34\", StringHandling.fixFPNumberFormat(\"1.234.567,34\"));\n";
    expected += "assertThat(StringHandling.fixFPNumberFormat(\"1.234.567,34\")).isSameAs(\"1234567.34\");\n";

    tester.Start_Test(input, expected);
  }

  @Test
  //Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void Replace_Type_12() throws Exception {
    //TODO: Add more test
    String input = "import static org.junit.Assert.assertEquals;\n";
    String expected = "import static org.assertj.core.api.Assertions.assertThat;\n";

    input += "import static org.junit.Assert.fail;\n";
    expected += "import static org.assertj.core.api.Assertions.fail;\n";

    input += "import static org.junit.Assert.*;\n";
    expected += "import static org.assertj.core.api.Assertions.*;\n";

    tester.Start_Test(input, expected);
    input = "import static org!junit.Assert.assertEquals;\n";
    expected = "import static org!junit.Assert.assertEquals;\n";

    input += "import static org.junit.Assert...fail;\n";
    expected += "import static org.junit.Assert...fail;\n";

    input += "import static org.junit.Assert.....Test;\n";
    expected += "import static org.junit.Assert.....Test;\n";
    tester.Start_Test(input, expected);
  }

}
