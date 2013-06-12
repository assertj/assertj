package org.assertj.core.api;

import java.io.InputStream;


/**
 * Assertion methods for {@link InputStream}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(InputStream)}</code>.
 * </p>
 * @author Matthieu Baechler
 * @author Mikhail Mazursky
 */
public class InputStreamAssert extends AbstractInputStreamAssert<InputStreamAssert, InputStream> {

  public InputStreamAssert(InputStream actual) {
    super(actual, InputStreamAssert.class);
  }
}
