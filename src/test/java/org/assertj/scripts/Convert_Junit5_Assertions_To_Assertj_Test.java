package org.assertj.scripts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Convert_Junit5_Assertions_To_Assertj_Test {
  private Shell_Script_Conversion_Test_Invoker tester;
  @BeforeEach
  public void init() {
    tester = new Shell_Script_Conversion_Test_Invoker(
        "sh",
        "src/test/java/org/assertj/scripts/Shell_Script_Conversion_Test_Invoker_Buffer.java",
        "src/main/scripts/convert-junit5-assertions-to-assertj.sh"
      );
  }

  @Test
  public void Script_Test_Template() throws Exception {

    String input = "assertEquals(0, myList.size());";
    String expected = "assertThat(myList).isEmpty();";
    tester.Start_Test(input, expected);

  }

  @Test
  //Replacing : assertEquals(0, myList.size()) ................. by : assertThat(myList).isEmpty()'
  public void Replace_Type_1() throws Exception {


  }

  @Test
  //Replacing : assertEquals(expectedSize, myList.size()) ...... by : assertThat(myList).hasSize(expectedSize)'
  public void Replace_Type_2() throws Exception {

  }

  @Test
  //Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void Replace_Type_3() throws Exception {

  }

  @Test
  //Replacing : assertEquals(expected, actual) ................. by : assertThat(actual).isEqualTo(expected)
  public void Replace_Type_4() throws Exception {

  }

  @Test
  //Replacing : assertNotEquals(expected, actual) ................. by : assertThat(actual).isNotEqualTo(expected)
  public void Replace_Type_4B() throws Exception {

  }

  @Test
  //Replacing : assertArrayEquals(expectedArray, actual) ....... by : assertThat(actual).isEqualTo(expectedArray)
  public void Replace_Type_5() throws Exception {

  }

  @Test
  //Replacing : assertNull(actual) ............................. by : assertThat(actual).isNull()
  public void Replace_Type_6() throws Exception {

  }

  @Test
  //Replacing : assertSame(expected, actual) ................... by : assertThat(actual).isSameAs(expected)
  public void Replace_Type_10() throws Exception {

  }



  @Test
  //Replacing : assertEquals(expectedDouble, actual, delta) .... by : assertThat(actual).isCloseTo(expectedDouble, within(delta))
  public void Replace_Type_12() throws Exception {

  }

}
