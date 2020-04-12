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
        "C:/Program Files/Git/bin/sh.exe",
        "src/test/java/org/assertj/scripts/Shell_Script_Conversion_Test_Invoker_Buffer.java",
        "src/main/scripts/convert-junit-assertions-to-assertj.sh"
      );
  }

  @Test
  public void Script_Test_Template() throws Exception {

    String input = "assertEquals(0, myList.size())";
    String expected = "assertThat(myList).isEmpty()";
    tester.Start_Test(input, expected);

  }

  @Test
  public void Replace_Type_1(){
    //assertEquals(0, myList.size())
    //assertThat(myList).isEmpty()

  }

}
