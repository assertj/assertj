package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.error.ShouldCronExpressionBeValid.shouldCronExpressionBeValid;

/**
 * CronExpression contains a cronable string endpoint that be assembled to set up a scheduled task.
 * @author Neil Wang
 */
public class CronField {

  private final CronUnit type;
  private final String value;
  private final Set<String> parsedValues = new HashSet<>();

  public CronField(CronUnit type, String value) {
    this.type = type;
    this.value = value;
  }

  public List<String> parsedValues() {
    return parsedValues.stream().sorted(Comparator.comparingInt(Integer::parseInt)).collect(Collectors.toList());
  }

  void assertIsValid(AssertionInfo info, Failures failures) {
    assertNotNull(info, value);
    if (Pattern.matches(this.type.wildcardPattern(), this.value)) {
      return;
    }
    for (String child : this.value.split(",")) {
      if (child.contains("/")) {
        final int allowSlashSize = 1;
        String[] v = child.split("/");
        if (v.length != allowSlashSize + 1) {
          throw failures.failure(info, shouldCronExpressionBeValid(this.value));
        }
        if (v[0].equals("*")) {
          continue;
        }
        assertIsValidWithBar(v[0], info, failures);
        assertNumberIsPositive(v[1], info, failures);
        continue;
      }
      assertIsValidWithBar(child, info, failures);
    }
  }

  public void parse(AssertionInfo info, Failures failures) {
    assertIsValid(info, failures);
    if (Pattern.matches(this.type.wildcardPattern(), this.value)) {
      this.parsedValues.add(this.value);
      return;
    }
    for (String child : this.value.split(",")) {
      if (child.contains("/")) {
        String[] v = child.split("/");
        if (!v[0].contains("-")) {
          int start;
          if (v[0].equals("*")) {
            start = 0;
          } else {
            start = assertNumberIsInt(v[0], info, failures);
          }
          int step = assertNumberIsInt(v[1], info, failures);
          IntStream.rangeClosed(start, this.type.max()).filter(i -> (i - start) % step == 0).forEachOrdered(i -> this.parsedValues.add(String.valueOf(i)));
        } else {
          String[] v1 = v[0].split("-");
          int start = assertNumberIsInt(v1[0], info, failures);
          int end = assertNumberIsInt(v1[1], info, failures);
          int step = assertNumberIsInt(v[1], info, failures);
          IntStream.rangeClosed(start, end).filter(i -> (i - start) % step == 0).forEachOrdered(i -> this.parsedValues.add(String.valueOf(i)));
        }
        continue;
      }
      if (child.contains("-")) {
        String[] v = child.split("-");
        int start = assertNumberIsInt(v[0], info, failures);
        int end = assertNumberIsInt(v[1], info, failures);
        IntStream.rangeClosed(start, end).forEachOrdered(i -> this.parsedValues.add(String.valueOf(i)));
        continue;
      }
      this.parsedValues.add(String.valueOf(assertNumberIsInt(child, info, failures)));
    }
  }

  private void assertIsValidWithBar(String value, AssertionInfo info, Failures failures) {
    String[] children = value.split("-");
    if (children.length == 1) {
      assertNumberIsValid(children[0], info, failures);
      return;
    }
    if (children.length != 2) {
      throw failures.failure(info, shouldCronExpressionBeValid(this.value));
    }
    for (int i = 0; i < children.length; i++) {
      assertNumberIsValid(children[i], info, failures);
      if (i == children.length - 1) {
        break;
      }
      if (assertNumberIsInt(children[i], info, failures) > assertNumberIsInt(children[i + 1], info, failures)) {
        throw failures.failure(info, shouldCronExpressionBeValid(this.value));
      }
    }
  }

  private void assertNumberIsValid(String actual, AssertionInfo info, Failures failures) {
    int value = assertNumberIsInt(actual, info, failures);
    if (value < this.type.min() || value > this.type.max()) {
      throw failures.failure(info, shouldCronExpressionBeValid(this.value));
    }
  }

  private void assertNumberIsPositive(String value, AssertionInfo info, Failures failures) {
    if (assertNumberIsInt(value, info, failures) < 0) {
      throw failures.failure(info, shouldCronExpressionBeValid(this.value));
    }
  }

  private int assertNumberIsInt(String value, AssertionInfo info, Failures failures) {
    if (type.mappings().containsKey(value.toUpperCase())) {
      value = type.mappings().get(value);
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw failures.failure(info, shouldCronExpressionBeValid(this.value));
    }
  }

  private void assertNotNull(AssertionInfo info, String actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
