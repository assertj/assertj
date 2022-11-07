package org.assertj.core.util.introspection;

import java.lang.reflect.Field;

/**
 * Looks up fields by their exact name.
 */
class SimpleFieldLookupStrategy implements FieldLookupStrategy {
  @Override
  public Field findByName(Class<?> acls, String fieldName) throws NoSuchFieldException {
    // getDeclaredField checks for non-public scopes as well and it returns accurate results
    return acls.getDeclaredField(fieldName);
  }

  @Override
  public String getDescription() {
    return "field match exactly by name";
  }
}