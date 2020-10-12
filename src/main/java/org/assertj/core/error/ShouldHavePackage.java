package org.assertj.core.error;

import java.util.StringJoiner;

/**
 * Creates an error message indicating that a {@link Class} should have a given package.
 * @author Matteo Mirk
 */
public class ShouldHavePackage extends BasicErrorMessageFactory {
  private static final String SHOULD_HAVE_PACKAGE = new StringJoiner("%n", "%n", "").add("Expecting")
                                                                                    .add("  <%s>")
                                                                                    .add("to have package:")
                                                                                    .add("  <%s>")
                                                                                    .toString();
  private static final String BUT_HAD_NONE = new StringJoiner("%n", "%n", "").add("but had none.")
                                                                             .toString();
  private static final String BUT_HAD = new StringJoiner("%n", "%n", "").add("but had:")
                                                                        .add("  <%s>")
                                                                        .toString();

  /**
   * Creates a new <code>ShouldHavePackage</code>.
   * @param actual the actual value in the failed assertion.
   * @param packageName the expected package name
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHavePackage(Class<?> actual, String packageName) {
    final Package actualPackage = actual.getPackage();
    if (actualPackage == null) {
      return new ShouldHavePackage(actual, packageName);
    }
    return new ShouldHavePackage(actual, packageName, actualPackage.getName());
  }

  private ShouldHavePackage(Class<?> actual, String expectedPackage) {
    super(SHOULD_HAVE_PACKAGE + BUT_HAD_NONE, actual, expectedPackage);
  }

  private ShouldHavePackage(Class<?> actual, String expectedPackage, String actualPackage) {
    super(SHOULD_HAVE_PACKAGE + BUT_HAD, actual, expectedPackage, actualPackage);
  }
}
