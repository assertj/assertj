package org.assertj.guava.api;

import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Test;

import com.google.common.collect.Range;

public class RangeAssert_hasUpperEndpointEqualTo_Test extends BaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // given
    Range<Integer> actual = null;
    // expect
    expectException(AssertionError.class, actualIsNull());
    // when
    assertThat(actual).hasUpperEndpointEqualTo(1);
  }

  @Test
  public void should_fail_when_range_has_upper_endpoint_not_equal_to() {
    // given
    final Range<Integer> actual = Range.closed(1, 10);
    // expect
    expectException(AssertionError.class, "\nExpecting:\n<[1‥10]>\nto have upper endpoint equal to:\n<2>\nbut was\n<1>");
    // when
    assertThat(actual).hasUpperEndpointEqualTo(2);
  }

  @Test
  public void should_pass_if_range_has_upper_endpoint_equal_to() throws Exception {
    // given
    final Range<Integer> actual = Range.closedOpen(1, 10);

    // when
    assertThat(actual).hasUpperEndpointEqualTo(10);

    // then
    // pass
  }
}
