package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Tests for {@link Throwables#getRootCause(Throwable)}.
 * 
 * @author Jean-Christophe Gay
 */
public class Throwables_getRootCause_Test {

  @Test
  public void should_return_null_if_throwable_has_no_cause() throws Exception {
    assertThat(Throwables.getRootCause(new Throwable())).isNull();
  }

  @Test
  public void should_return_cause_when_throwable_has_cause() throws Exception {
    IllegalArgumentException expectedCause = new IllegalArgumentException();
    assertThat(Throwables.getRootCause(new Throwable(expectedCause))).isSameAs(expectedCause);
  }

  @Test
  public void should_return_root_cause_when_throwable_has_cause_which_has_cause() throws Exception {
    NullPointerException expectedCause = new NullPointerException();
    Throwable error = new Throwable(new IllegalArgumentException(expectedCause));

    assertThat(Throwables.getRootCause(error)).isSameAs(expectedCause);
  }
}
