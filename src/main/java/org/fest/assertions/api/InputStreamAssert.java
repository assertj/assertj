package org.fest.assertions.api;

import java.io.InputStream;

import org.fest.assertions.internal.InputStreams;
import org.fest.assertions.internal.InputStreamsException;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for <code>{@link InputStream}</code>s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(InputStream)}</code>.
 * </p>
 * @author Matthieu Baechler
 */
public class InputStreamAssert extends AbstractAssert<InputStreamAssert, InputStream> {

  @VisibleForTesting
  InputStreams inputStreams = InputStreams.instance();

  public InputStreamAssert(InputStream actual) {
    super(actual, InputStreamAssert.class);
  }

  /**
   * Verifies that the content of the actual {@code InputStream} is equal to the content of the given one.
   * 
   * @param expected the given {@code InputStream} to compare the actual {@code InputStream} to.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given {@code InputStream} is {@code null}.
   * @throws AssertionError if the actual {@code InputStream} is {@code null}.
   * @throws AssertionError if the content of the actual {@code InputStream} is not equal to the content of the given one.
   * @throws InputStreamsException if an I/O error occurs.
   */
  public InputStreamAssert hasContentEqualTo(InputStream expected) {
    inputStreams.assertEqualContent(info, actual, expected);
    return this;
  }

}
