package org.assertj.core.util.introspection;

import java.lang.reflect.Field;

class SimpleFieldFindStrategy implements FieldFindStrategy {

  @Override
  public Field findByName(Class<?> acls, String fieldName) throws NoSuchFieldException {
    // getDeclaredField checks for non-public scopes as well and it returns accurate results
    return acls.getDeclaredField(fieldName);
  }
}