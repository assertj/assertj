package org.example.test;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AssertFloatArrayWithInRange_Test {
  @Test
  public void testOnSimpleArray(){
    /*assertThat(0.2f).isEqualTo(0.7f, within(0.5f)); // can pass
    assertThat(new float[]{0.2f}).containsExactly(new float[]{0.7f}, within(0.5f)); // can pass as well!*/

    //assertThat(0.75f).isEqualTo(0.65f, within(0.1f));
    //(new double[]{0.75}).containsExactly(new double[]{0.65}, within(0.1));
    
  }

  @org.junit.Test
  public void t1() {
    assertThat(1.0).isEqualTo(1.1, within(0.1)); // pass
    //assertThat(new double[]{5}).containsExactly(new double[]{4.5}, within(0.5));
    assertThat(new float[]{0.2f}).containsExactly(new float[]{0.7f}, within(0.5f)); // fail but should pass
  }
  @org.junit.Test
  public void t2(){
    assertThat(8.1).isCloseTo(8.0, offset(0.1));
  }
}
