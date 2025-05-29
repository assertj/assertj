package org.assertj.core.api.recursive;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.util.introspection.ClassUtils.isInJavaLangPackage;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

public class FieldsIntrospectionHelper {

  private final FieldsIntrospectionConfiguration fieldsIntrospectionConfiguration;

  public FieldsIntrospectionHelper(FieldsIntrospectionConfiguration fieldsIntrospectionConfiguration) {
    this.fieldsIntrospectionConfiguration = fieldsIntrospectionConfiguration;
  }

  public Set<Field> getDeclaredFieldsIncludingInherited(Class<?> clazz) {
    requireNonNull(clazz, "expecting Class parameter not to be null");
    Set<Field> declaredFields = getDeclaredFields(clazz);
    // get fields declared in superClass
    Class<?> superClass = clazz.getSuperclass();
    while (!isInJavaLangPackage(superClass)) {
      declaredFields.addAll(getDeclaredFields(superClass));
      superClass = superClass.getSuperclass();
    }
    return declaredFields;
  }

  public Set<String> getFieldsNames(Class<?> clazz) {
    return getDeclaredFieldsIncludingInherited(clazz).stream()
                                                     .map(Field::getName)
                                                     .collect(toSet());
  }

  private Set<Field> getDeclaredFields(Class<?> clazz) {
    Field[] declaredFields = clazz.getDeclaredFields();
    Predicate<Field> fieldPredicate = field -> !(field.isSynthetic() || Modifier.isStatic(field.getModifiers()));
    if (fieldsIntrospectionConfiguration.shouldIgnoreTransientFields()) {
      fieldPredicate = fieldPredicate.and(field -> !Modifier.isTransient(field.getModifiers()));
    }
    return stream(declaredFields).filter(fieldPredicate)
                                 .collect(toCollection(LinkedHashSet::new));
  }


}
