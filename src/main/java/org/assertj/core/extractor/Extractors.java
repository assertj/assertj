package org.assertj.core.extractor;

import org.assertj.core.api.iterable.Extractor;

/**
 * Extractors factory, providing a convenient method of creating common extractors.
 * <p>
 * 
 * For example:
 * <pre>
 * assertThat(objectsList).extracting(toStringMethod()).contains("toString 1", "toString 2");
 * </pre>
 * 
 * @author Mateusz Haligowski
 *
 */
public class Extractors {
  /**
   * Provides extractor for extracting {@link java.lang.Object#toString} from any object
   * @return
   */
  public static Extractor<Object, String> toStringMethod() {
    return new ToStringExtractor();
  }
}
