package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.FieldLocation;
import org.assertj.core.internal.FieldComparators;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.Representation;

@Beta
public class RecursiveComparisonConfiguration {

  private boolean strictTypeCheck = true;

  private boolean ignoreAllActualNullFields = false;
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

  public boolean shouldIgnoreAllActualNullFields() {
    return ignoreAllActualNullFields;
  }

  /**
   * Sets whether actual null fields are ignored in the recursive comparison.
   * <p>
   * TODO add a code example.
   * 
   * @param ignoreAllActualNullFields
   */
  public void setIgnoreAllActualNullFields(boolean ignoreAllActualNullFields) {
    this.ignoreAllActualNullFields = ignoreAllActualNullFields;
  }

  @Override
  public String toString() {
    return multiLineDescription(CONFIGURATION_PROVIDER.representation());
  }

  public String multiLineDescription(Representation representation) {
    StringBuilder description = new StringBuilder();
    if (ignoreAllActualNullFields) description.append("- all actual null fields were ignored in the comparison");
    return format(description.toString());
  }
}
