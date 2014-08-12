package org.assertj.core.extractor;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.util.introspection.MethodSupport;

/**
 * 
 * Extractor for extracting data by a method name.
 * 
 * @author Michał Piotrkowski
 * @author Mateusz Haligowski
 */
class ResultOfExtractor<F> implements Extractor<F, Object> {

  private final String methodName;
  
  ResultOfExtractor(String methodName) {
    this.methodName = methodName;
  }

  /**
   * Behavior is described in {@link MethodSupport#methodResultFor(Object, String)}
   */
  @Override
  public Object extract(F input) {
    return MethodSupport.methodResultFor(input, methodName);
  }

}
