package org.assertj.core.util.introspection;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.CaseFormat;

class NormalizedFieldFindStrategy implements FieldFindStrategy {

  @Override
  public Field findByName(Class<?> acls, String fieldName) throws NoSuchFieldException {
    List<Field> matchingFields = Arrays.stream(acls.getDeclaredFields())
                                       .filter(field -> normalize(field.getName()).equals(fieldName))
                                       .collect(Collectors.toList());
    if (matchingFields.isEmpty()) {
      throw new NoSuchFieldException();
    }
    checkArgument(matchingFields.size() == 1, "Reference to field %s"
                                              + " is ambiguous relative to %s; possible candidates are: %s",
                  fieldName, acls,
                  matchingFields.stream().map(Field::getName).collect(joining(", ")));
    return matchingFields.get(0);
  }

  private String normalize(String name) {
    if (!name.contains("_")) {
      return name;
    }
    if (name.startsWith("_")) {
      return normalize(name.substring(1));  // avoid _last_name -> LastName
    }
    return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
  }
}
