package org.assertj.core.extractor;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;

/**
 * Extractors factory, providing convenient methods of creating common extractors.
 * <p>
 * 
 * For example:
 * <pre><code class='java'>
 * assertThat(objectsList).extracting(toStringMethod()).contains("toString 1", "toString 2");
 * assertThat(objectsList).extracting(byName("field")).contains("someResult1", "someResult2");
 * </code></pre>
 * 
 * @author Mateusz Haligowski
 *
 */
public class Extractors {
  /**
   * Provides extractor for extracting {@link java.lang.Object#toString} from any object
   */
  public static Extractor<?, String> toStringMethod() {
    return new ToStringExtractor();
  }
  
  /**
   * Provides extractor for extracting single field or property from any object using reflection
   */
  public static <F> Extractor<F, Object> byName(String fieldOrProperty) {
    return new ByNameSingleExtractor<F>(fieldOrProperty);
  }
  
  /**
   * Provides extractor for extracting multiple fields or properties from any object using reflection
   */
  public static <F> Extractor<F, Tuple> byName(String... fieldsOrProperties) {
    return new ByNameMultipleExtractor<F>(fieldsOrProperties);
  }

  /**
   * Provides extractor for extracting values by method name from any object using reflection
   */
  public static <F> Extractor<F, Object> resultOf(String methodName) {
    return new ResultOfExtractor<F>(methodName);
  }
  
}
