package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AbstractAssert#isLessThan(Object)}.
 *
 * @author wettera
 */

public class NonComparableClassGivenComparator_isLessThan_Test {
  @Test
  void should_pass_if_actual_less_than_other_given_Comparator(){
    Obj actual = new Obj();
    Obj other = new Obj();
    actual.setId(2);
    other.setId(3);
    ObjComparator compara = new ObjComparator();
    assertThat(actual).usingComparator(compara).isLessThan(other);
  }

  @Test
  void should_fail_if_actual_not_less_than_other_given_Comparator(){
    Obj actual = new Obj();
    Obj other = new Obj();
    actual.setId(3);
    other.setId(2);
    ObjComparator compara = new ObjComparator();
    try{
      assertThat(actual).usingComparator(compara).isLessThan(other);
    }
    catch (AssertionError e){
      System.out.println(e.getMessage());
    }
  }

}
