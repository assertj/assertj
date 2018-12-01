package org.assertj.core.api;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.annotations.Beta;
import org.assertj.core.internal.FieldComparators;
import org.assertj.core.internal.TypeComparators;

@Beta
public class RecursiveComparisonSpecification {

  private boolean strictTypeCheck = true;

  private boolean ignoreNullFields = false;
  private Set<FieldLocation> ignoredFields = new HashSet<>();

  private boolean ignoreCustomEquals = false;

  private Set<Class> forceRecursiveComparisonForTypes = new HashSet<>();
  private Set<FieldLocation> forceRecursiveComparisonForFields = new HashSet<>();

  private TypeComparators comparatorForTypes = new TypeComparators();
  private FieldComparators comparatorForFields = new FieldComparators();

  public Comparator getComparatorForField(String fieldName) {
    return null;
  }

  public Comparator getComparatorForType(Class fieldType) {
    return null;
  }

  public boolean hasComparatorForField(String fieldName) {
    return false;
  }

  public boolean hasComparatorForType(Class<?> keyType) {
    return false;
  }

  public boolean hasNoCustomComparators() {
    return false;
  }
}
