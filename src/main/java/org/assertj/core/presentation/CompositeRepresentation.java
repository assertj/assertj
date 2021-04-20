package org.assertj.core.presentation;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

public class CompositeRepresentation implements Representation {
  protected final Map<String, Representation> representationsByName;

  public CompositeRepresentation(Map<String, Representation> representations) {
    if (representations == null) throw new IllegalArgumentException();
    this.representationsByName = representations;
  }

  @Override
  public String toStringOf(Object object) {
    return (new ArrayList<Representation>(this.representationsByName.values()))
      .stream().map(x -> x.toStringOf(object)).findAny().orElse(STANDARD_REPRESENTATION.toStringOf(object));
  }

  /*Maybe the method is redundant here.*/
  @Override
  public String unambiguousToStringOf(Object object) {
    for (Map.Entry<String, Representation> entry : representationsByName.entrySet()) {
      String value = entry.getValue().unambiguousToStringOf(object);
      if (value != null)
        return value;
    }
    return null;
  }
}
