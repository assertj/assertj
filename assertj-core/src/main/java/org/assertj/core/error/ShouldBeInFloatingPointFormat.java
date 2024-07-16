package org.assertj.core.error;

public class ShouldBeInFloatingPointFormat extends BasicErrorMessageFactory {
  /**
   * Creates a new <code>{@link ShouldBeInFloatingPointFormat}</code>.
   *
   */
  public static ErrorMessageFactory shouldBeInFloatingPointFormat(Object actual) {
    return new ShouldBeInFloatingPointFormat(actual);
  }

  private ShouldBeInFloatingPointFormat(Object actual) {
    super("%nExpecting:%n  <%s>%nto be in floating point format but it was not.", actual);
  }
}
