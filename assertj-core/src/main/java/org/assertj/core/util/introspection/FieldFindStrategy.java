package org.assertj.core.util.introspection;

import java.lang.reflect.Field;

@FunctionalInterface
interface FieldFindStrategy {
 
  Field findByName(Class<?> acls, String fieldName) throws NoSuchFieldException;
}
