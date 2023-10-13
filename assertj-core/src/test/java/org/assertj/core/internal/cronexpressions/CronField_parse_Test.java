package org.assertj.core.internal.cronexpressions;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.CronField;
import org.assertj.core.internal.CronUnit;
import org.assertj.core.internal.Failures;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someInfo;

/**
 * Tests for <code>{@link CronField#parse(AssertionInfo, Failures)}</code>.
 * @author Neil Wang
 */
class CronField_parse_Test {

  @ParameterizedTest
  @MethodSource("source")
  void should_parse_as_expect(CronUnit unit, String actual, String[] expect) {
    AssertionInfo info = someInfo();
    CronField cronField = new CronField(unit, actual);
    cronField.parse(info, Failures.instance());
    assertThat(cronField.parsedValues()).containsExactly(expect);
  }

  public static Stream<Arguments> source() {
    return Stream.of(
      Arguments.of(CronUnit.SECONDS, "*", new String[]{"*"}),
      Arguments.of(CronUnit.SECONDS, "1", new String[]{"1"}),
      Arguments.of(CronUnit.SECONDS, "1,2", new String[]{"1", "2"}),
      Arguments.of(CronUnit.SECONDS, "1,1-5", new String[]{"1", "2", "3", "4", "5"}),
      Arguments.of(CronUnit.SECONDS, "1,1-7/2", new String[]{"1", "3", "5", "7"}),
      Arguments.of(CronUnit.SECONDS, "1,0/10", new String[]{"0", "1", "10", "20", "30", "40", "50"}),
      Arguments.of(CronUnit.SECONDS, "1,3/10", new String[]{"1", "3", "13", "23", "33", "43", "53"}),
      Arguments.of(CronUnit.SECONDS, "1,*/10", new String[]{"0", "1", "10", "20", "30", "40", "50"}),
      Arguments.of(CronUnit.YEAR, "?", new String[]{"?"})
    );
  }

}
