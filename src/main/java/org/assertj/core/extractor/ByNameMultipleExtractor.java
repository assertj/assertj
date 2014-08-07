package org.assertj.core.extractor;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.groups.Tuple;

class ByNameMultipleExtractor<T> implements Extractor<T, Tuple>{

  private final String[] fieldsOrProperties;

  ByNameMultipleExtractor(String... fieldsOrProperties) {
    this.fieldsOrProperties = fieldsOrProperties;
  }

  @Override
  public Tuple extract(T input) {
    if (fieldsOrProperties == null)
      throw new IllegalArgumentException("The names of the fields/properties to read should not be null");
    if (fieldsOrProperties.length == 0)
      throw new IllegalArgumentException("The names of the fields/properties to read should not be empty");
    if (input == null)
      throw new IllegalArgumentException("The object to extract fields/properties from should not be null");

    List<Extractor<T, Object>> extractors = buildExtractors();
    List<Object> values = extractValues(input, extractors);
    
    return new Tuple(values.toArray());
  }

  private List<Object> extractValues(T input, List<Extractor<T, Object>> singleExtractors) {
    List<Object> values = new ArrayList<Object>();
    
    for (Extractor<T, Object> extractor : singleExtractors) {
      values.add(extractor.extract(input));
    }
    return values;
  }

  private List<Extractor<T, Object>> buildExtractors() {
    List<Extractor<T, Object>> result = new ArrayList<Extractor<T,Object>>();
    
    for (String name : fieldsOrProperties) {
      result.add(new ByNameSingleExtractor<T>(name));
    }
    
    return result;
  }

}
