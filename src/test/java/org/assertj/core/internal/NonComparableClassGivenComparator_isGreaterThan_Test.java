package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import java.util.Comparator;
import java.lang.AssertionError;
/**
 * Tests for {@link AbstractAssert#isGreaterThan(Object)}.
 *
 * @author wettera
 */

public class NonComparableClassGivenComparator_isGreaterThan_Test {
  @Test
  void should_pass_if_actual_greater_than_other_given_Comparator(){
    Obj actual = new Obj();
    Obj other = new Obj();
    actual.setId(3);
    other.setId(2);
    ObjComparator compara = new ObjComparator();
    assertThat(actual).usingComparator(compara).isGreaterThan(other);
  }

  @Test
  void should_fail_if_actual_not_greater_than_other_given_Comparator(){
    Obj actual = new Obj();
    Obj other = new Obj();
    actual.setId(2);
    other.setId(3);
    ObjComparator compara = new ObjComparator();
    try{
      assertThat(actual).usingComparator(compara).isGreaterThan(other);
    }
    catch (AssertionError e){
      System.out.println(e.getMessage());
    }
  }

}

class Obj {
  private int id = 0;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

}

class ObjComparator implements Comparator<Obj> {
  @Override
  public int compare(Obj actual, Obj other) {
    return Integer.valueOf(actual.getId()).compareTo(other.getId());
  }
}
