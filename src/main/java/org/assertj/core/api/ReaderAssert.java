package org.assertj.core.api;

import java.io.Reader;

/**
 * Assertion methods for {@link java.io.Reader}s.
 * <p>
 * To create a new instance of this class, invoke
 * <code>{@link org.assertj.core.api.Assertions#assertThat(java.io.Reader)}</code>.
 * </p>
 * 
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 * @author Bartosz Bierkowski
 */
public class ReaderAssert extends AbstractReaderAssert<ReaderAssert, Reader> {

  public ReaderAssert(Reader actual) {
    super(actual, ReaderAssert.class);
  }
}
