package org.assertj.core.presentation;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

public class CompositeRepresentation implements Representation {
  protected Map<String, Representation> representations;

  /*Initialize the CompositeRepresentation according to a certain correspondence of the "name of representation" and "representation" itself.*/
  public CompositeRepresentation(Map<String, Representation> representations) {
    this.representations = representations;
  }

  /*Initialize the CompositeRepresentation when the user does not have the correspondence of the "name of representation" and "representation" itself yet.*/
  public CompositeRepresentation() {
    representations = new HashMap<>();
  }

  /*Implementation of the `toString` method.*/
  @Override
  public String toStringOf(Object object) {
    for (Map.Entry<String, Representation> entry : representations.entrySet()) {
      String value = entry.getValue().toStringOf(object);
      if (value != null)
        return value;
    }
    return STANDARD_REPRESENTATION.toStringOf(object);
  }

  /*Implementation of the `toString` method when a specific representation is desired.*/
  public String toStringOf(Object object, String representationName) {
    if (representations.containsKey(representationName))
      return representations.get(representationName).toStringOf(object);
    return null;
  }

  /*Change the correspondence of the "name of representation" and "representation"*/
  public void setRepresentations(Map<String, Representation> representations) {
    this.representations = representations;
  }

  /*Get the "representation"*/
  public Map<String, Representation> getRepresentations() {
    return representations;
  }

  /*Add an entry of the correspondence of the "name of representation" and "representation"*/
  public boolean addRepresentation(String representationName, Representation representation) {
    if (representations.containsKey(representationName))
      return false;
    representations.put(representationName, representation);
    return true;
  }

  /*Delete an entry of the correspondence of the "name of representation" and "representation"*/
  public boolean deleteRepresentation(String representationName) {
    if (representations.containsKey(representationName)) {
      representations.remove(representationName);
      return true;
    }
    return false;
  }

  /*Maybe the method is redundant here.*/
  @Override
  public String unambiguousToStringOf(Object object) {
    for (Map.Entry<String, Representation> entry : representations.entrySet()) {
      String value = entry.getValue().unambiguousToStringOf(object);
      if (value != null)
        return value;
    }
    return null;
  }
}
