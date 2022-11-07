package org.assertj.core.util.introspection;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.introspection.NormalizeStrategy.normalize;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

class NormalizedFieldFindStrategy implements FieldFindStrategy {

  @Override
  public Field findByName(Class<?> acls, String fieldName) throws NoSuchFieldException {
    List<Field> matchingFields = Arrays.stream(acls.getDeclaredFields())
                                       .filter(field -> normalize(field.getName()).equals(fieldName))
                                       .collect(toList());
    if (matchingFields.isEmpty()) {
      throw new NoSuchFieldException(fieldName);
    }
    checkArgument(matchingFields.size() == 1,
                  "Reference to field %s is ambiguous relative to %s; possible candidates are: %s",
                  fieldName, acls, matchingFields.stream().map(Field::getName).collect(joining(", ")));
    return matchingFields.get(0);
  }
}
