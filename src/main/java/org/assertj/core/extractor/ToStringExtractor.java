/**
 * 
 */
package org.assertj.core.extractor;

import org.assertj.core.api.iterable.Extractor;

/**
 * Extracts {@link Object#toString()} from any object
 * 
 * @author Mateusz Haligowski
 *
 */
public class ToStringExtractor implements Extractor<Object, String> {
  @Override
  public String extract(Object input) {
    return input.toString();
  }
  
  ToStringExtractor() {
    
  }

}
