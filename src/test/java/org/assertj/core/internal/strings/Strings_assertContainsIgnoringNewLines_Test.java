package org.assertj.core.internal.strings;

import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringNewLines(AssertionInfo,
 * CharSequence, CharSequence...)} </code>.
 */
class StringsassertContainsIgnoringNewLinesTest extends StringsBaseTest {
  /**
   * info
   */
  private static final WritableAssertionInfo INFO = someInfo();
  /**
   * test string
   */
  private static final String TESTSTRINGAB = "Alice\nand\nBob";

  /**
   * Constructor
   */
  public StringsassertContainsIgnoringNewLinesTest() {
    super();
  }

  /**
   * Test containsIgnoringNewLines method
   * @param value the test string
   */
  @ParameterizedTest
  @ValueSource(strings = { "Al", "ice\nandBob", "Alice\nandBob", TESTSTRINGAB })
  /* default */ void shouldPassIfActualContainsValueWhenNewLinesAreIgnored(final String value) {
    // GIVEN / WHEN / THEN
    strings.assertContainsIgnoringNewLines(INFO, TESTSTRINGAB, value);
  }
}
