package org.assertj.core.extractor;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;

/**
 * Extractors factory, providing a convenient method of creating common extractors.
 * <p>
 * 
 * For example:
 * <pre>
 * assertThat(objectsList).extracting(toStringMethod()).contains("toString 1", "toString 2");
 * assertThat(objectsList).extracting(byName("field")).contains("someResult1", "someResult2");
 * </pre>
 * 
 * @author Mateusz Haligowski
 *
 */
public class Extractors {
  /**
   * Provides extractor for extracting {@link java.lang.Object#toString} from any object
   * @return extractor
   */
  public static Extractor<? extends Object, String> toStringMethod() {
    return new ToStringExtractor();
  }
  
  /**
   * Provides extractor for extracting fields or properties from any object using reflection
   */
  public static <T> Extractor<T, Object> byName(String fieldOrProperty) {
    return new ByNameSingleExtractor<T>(fieldOrProperty);
  }
  
  /**
   * Provides extractor for 
   * @param fieldsOrProperties
   * @return
   */
  public static <T> Extractor<T, Tuple> byName(String... fieldsOrProperties) {
    return new ByNameMultipleExtractor<T>(fieldsOrProperties);
  }
}
