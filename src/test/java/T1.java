import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class T1 {
  @Test
  public void t1() {
    assertThat(0.2f).isEqualTo(0.7f, within(0.5f)); // pass
    //assertThat(new double[]{5}).containsExactly(new double[]{4.5}, within(0.5));
    assertThat(new float[]{0.2f}).containsExactly(new float[]{0.7f}, within(0.5f)); // fail but should pass
  }
  @Test
  public void t2(){
    assertThat(8.1).isCloseTo(8.0, offset(0.1));
  }
}
