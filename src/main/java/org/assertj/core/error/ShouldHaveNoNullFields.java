package org.assertj.core.error;

import java.util.List;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that an object
 * has no null fields failed.
 *
 * @author Johannes Brodwall
 */
public class ShouldHaveNoNullFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting%n  <%s>%nto have a property or a field named <%s>";
  private static final String EXPECTED_SINGLE = "%nExpecting%n  <%s>%nto have no property or a field with value null, but <%s> was null.%n";
  private static final String COMPARISON = "Comparison was performed on all fields";
  private static final String EXCLUDING = "Ignored fields: <%s>";

  public ShouldHaveNoNullFields(Object actual, List<String> rejectedFields, List<String> ignoredFields) {
      super(EXPECTED_MULTIPLE + EXCLUDING, actual, rejectedFields, ignoredFields);
  }

  public ShouldHaveNoNullFields(Object actual, List<String> rejectedFields) {
      super(EXPECTED_MULTIPLE + COMPARISON, actual, rejectedFields);
  }

  public ShouldHaveNoNullFields(Object actual, String rejectedField) {
      super(EXPECTED_SINGLE + COMPARISON, actual, rejectedField);
  }

  public ShouldHaveNoNullFields(Object actual, String rejectedField, List<String> ignoredFields) {
      super(EXPECTED_SINGLE + EXCLUDING, actual, rejectedField, ignoredFields);
  }

  public static ShouldHaveNoNullFields shouldHaveNoNullFieldsExcept(Object actual, List<String> rejectedFields,
                                                                   List<String> ignoredFields) {
    if (rejectedFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldHaveNoNullFields(actual, rejectedFields.get(0));
      }
      return new ShouldHaveNoNullFields(actual, rejectedFields.get(0), ignoredFields);
    }
    if (ignoredFields.isEmpty()) {
      return new ShouldHaveNoNullFields(actual, rejectedFields);
    }
    return new ShouldHaveNoNullFields(actual, rejectedFields, ignoredFields);
  }

}
