/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.api.recursive.comparison.Color.BLUE;
import static org.assertj.tests.core.api.recursive.comparison.Color.GREEN;
import static org.assertj.tests.core.api.recursive.comparison.Color.RED;
import static org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_isEqualTo_with_enums_Test.EmployeeDTO.JobTitle.QA_ENGINEER;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_with_enums_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @Test
  void should_not_compare_enum_recursively() {
    // GIVEN
    Light actual = new Light(GREEN);
    Light expected = new Light(BLUE);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", actual.color, expected.color);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_compare_enum_by_reference_in_strictTypeChecking_mode() {
    // GIVEN
    Light actual = new Light(GREEN);
    Light expected = new Light(GREEN);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .withStrictTypeChecking()
                .isEqualTo(expected);
  }

  @Test
  void should_fail_when_enum_are_not_the_same_in_strictTypeChecking_mode() {
    // GIVEN
    Light actual = new Light(GREEN);
    Light expected = new Light(RED);
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", GREEN, RED);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_compare_enum_by_name_by_default() {
    // GIVEN
    Light actual = new Light(GREEN);
    LightDto expected = new LightDto(ColorDto.GREEN);
    // WHEN-THEN
    then(actual).usingRecursiveComparison()
                .isEqualTo(expected);
  }

  @Test
  void should_fail_when_enum_names_are_not_equals() {
    // GIVEN
    Light actual = new Light(GREEN);
    Light expected = new Light(RED);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", GREEN, RED);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_pass_when_enum_matches_string_when_allowing_to_compare_enum_with_string() {
    // GIVEN
    LightString actual = new LightString("GREEN");
    Light expected = new Light(GREEN);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .withEnumStringComparison()
                .isEqualTo(expected);
  }

  @Test
  void should_pass_when_string_matches_enum_when_allowing_to_compare_enum_with_string() {
    // GIVEN
    Light actual = new Light(GREEN);
    LightString expected = new LightString("GREEN");
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .withEnumStringComparison()
                .isEqualTo(expected);
  }

  @Test
  void should_fail_when_enum_does_not_match_string_when_allowing_to_compare_enum_with_string() {
    // GIVEN
    LightString actual = new LightString("VERT");
    Light expected = new Light(GREEN);
    recursiveComparisonConfiguration.allowComparingEnumAgainstString(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", "VERT", GREEN);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_string_does_not_match_enum_when_allowing_to_compare_enum_with_string() {
    // GIVEN
    Light actual = new Light(GREEN);
    LightString expected = new LightString("VERT");
    recursiveComparisonConfiguration.allowComparingEnumAgainstString(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", GREEN, "VERT");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_expected_is_an_enum_and_actual_is_not() {
    // GIVEN
    LightString actual = new LightString("GREEN");
    Light expected = new Light(GREEN);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", "GREEN", GREEN,
                                           "expected field is an enum but actual field is not (java.lang.String)");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_is_an_enum_and_expected_is_not() {
    // GIVEN
    Employee devPerson = new Employee("Example Name", "SOFTWARE_DEVELOPER");
    BlogPost devBlogPost = new BlogPost(devPerson);
    EmployeeDTO qaPersonDTO = new EmployeeDTO("Example Name", QA_ENGINEER);
    BlogPostDTO qaBlogPostDTO = new BlogPostDTO(qaPersonDTO);
    // WHEN/THEN
    ComparisonDifference difference = diff("author.jobTitle", QA_ENGINEER, "SOFTWARE_DEVELOPER",
                                           "expected field is a java.lang.String but actual field is an enum");
    compareRecursivelyFailsWithDifferences(qaBlogPostDTO, devBlogPost, difference);
  }

  @Test
  void should_fail_when_expected_is_an_enum_and_actual_is_not_an_enum_or_a_string() {
    // GIVEN
    LightNumber actual = new LightNumber(123);
    Light expected = new Light(GREEN);
    recursiveComparisonConfiguration.allowComparingEnumAgainstString(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", 123, GREEN,
                                           "expected field is an enum but actual field is not (java.lang.Integer)");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_is_an_enum_and_expected_is_not_an_enum_or_a_string() {
    // GIVEN
    Light actual = new Light(GREEN);
    LightNumber expected = new LightNumber(123);
    recursiveComparisonConfiguration.allowComparingEnumAgainstString(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", GREEN, 123,
                                           "expected field is a java.lang.Integer but actual field is an enum");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  static class LightString {
    public String color;

    public LightString(String value) {
      this.color = value;
    }

    @Override
    public String toString() {
      return format("LightString[color=%s]", this.color);
    }
  }

  static class LightNumber {
    public int color;

    public LightNumber(int value) {
      this.color = value;
    }

    @Override
    public String toString() {
      return format("LightNumber[color=%s]", this.color);
    }
  }

  public static class BlogPost {
    Employee author;

    public BlogPost(Employee author) {
      this.author = author;
    }

  }
  public static class BlogPostDTO {
    EmployeeDTO author;

    public BlogPostDTO(EmployeeDTO author) {
      this.author = author;
    }

  }
  public static class Employee {
    String name;
    String jobTitle;

    public Employee(String name, String jobTitle) {
      this.name = name;
      this.jobTitle = jobTitle;
    }

  }
  public static class EmployeeDTO {
    String name;
    JobTitle jobTitle;

    public EmployeeDTO(String name, JobTitle jobTitle) {
      this.name = name;
      this.jobTitle = jobTitle;
    }

    public enum JobTitle {
      SOFTWARE_DEVELOPER, QA_ENGINEER
    }
  }

}
