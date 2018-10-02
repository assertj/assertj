package org.assertj.core.error;

import java.util.List;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that an object
 * has null fields failed.
 *
 * @author Vladimir Chernikov
 */
public class ShouldHaveAllNullFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting%n  <%s>%nto only have null properties or fields but these were not null:%n <%s> are null.%n";
  private static final String EXPECTED_SINGLE = "%nExpecting%n  <%s>%nto only have null property or field, but <%s> was not null.%n";
  private static final String COMPARISON = "Check was performed on all fields/properties";
  private static final String EXCLUDING = COMPARISON + " except: <%s>.";
  private static final String DOT = ".";

  public ShouldHaveAllNullFields(Object actual, List<String> nonNullFields, List<String> ignoredFields) {
    super(EXPECTED_MULTIPLE + EXCLUDING, actual, nonNullFields, ignoredFields);
  }

  public ShouldHaveAllNullFields(Object actual, List<String> nonNullFields) {
    super(EXPECTED_MULTIPLE + COMPARISON + DOT, actual, nonNullFields);
  }

  public ShouldHaveAllNullFields(Object actual, String nonNullField) {
    super(EXPECTED_SINGLE + COMPARISON + DOT, actual, nonNullField);
  }

  public ShouldHaveAllNullFields(Object actual, String nonNullField, List<String> ignoredFields) {
    super(EXPECTED_SINGLE + EXCLUDING, actual, nonNullField, ignoredFields);
  }

  public static ShouldHaveAllNullFields shouldHaveAllNullFields(Object actual, List<String> nonNullFields,
                                                                List<String> ignoredFields) {
    if (nonNullFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldHaveAllNullFields(actual, nonNullFields.get(0));
      }
      return new ShouldHaveAllNullFields(actual, nonNullFields.get(0), ignoredFields);
    }
    if (ignoredFields.isEmpty()) {
      return new ShouldHaveAllNullFields(actual, nonNullFields);
    }
    return new ShouldHaveAllNullFields(actual, nonNullFields, ignoredFields);
  }
}
