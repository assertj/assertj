package org.assertj.core.error;

import java.util.List;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that an object
 * has null fields failed.
 *
 * @author Vladimir Chernikov
 */
public class ShouldHaveAllNullFields extends BasicErrorMessageFactory {

  private static final String EXPECTED_MULTIPLE = "%nExpecting%n  <%s>%nto have properties or fields named <%s> are null.%n";
  private static final String EXPECTED_SINGLE = "%nExpecting%n  <%s>%nto have null property or field, but <%s> was not null.%n";
  private static final String COMPARISON = "Check was performed on all fields/properties";
  private static final String EXCLUDING = COMPARISON + " except: <%s>.";
  private static final String DOT = ".";

  public ShouldHaveAllNullFields(Object actual, List<String> rejectedFields, List<String> ignoredFields) {
    super(EXPECTED_MULTIPLE + EXCLUDING, actual, rejectedFields, ignoredFields);
  }

  public ShouldHaveAllNullFields(Object actual, List<String> rejectedFields) {
    super(EXPECTED_MULTIPLE + COMPARISON + DOT, actual, rejectedFields);
  }

  public ShouldHaveAllNullFields(Object actual, String rejectedField) {
    super(EXPECTED_SINGLE + COMPARISON + DOT, actual, rejectedField);
  }

  public ShouldHaveAllNullFields(Object actual, String rejectedField, List<String> ignoredFields) {
    super(EXPECTED_SINGLE + EXCLUDING, actual, rejectedField, ignoredFields);
  }

  public static ShouldHaveAllNullFields shouldHaveAllNullFields(Object actual, List<String> rejectedFields,
                                                                List<String> ignoredFields) {
    if (rejectedFields.size() == 1) {
      if (ignoredFields.isEmpty()) {
        return new ShouldHaveAllNullFields(actual, rejectedFields.get(0));
      }
      return new ShouldHaveAllNullFields(actual, rejectedFields.get(0), ignoredFields);
    }
    if (ignoredFields.isEmpty()) {
      return new ShouldHaveAllNullFields(actual, rejectedFields);
    }
    return new ShouldHaveAllNullFields(actual, rejectedFields, ignoredFields);
  }
}
