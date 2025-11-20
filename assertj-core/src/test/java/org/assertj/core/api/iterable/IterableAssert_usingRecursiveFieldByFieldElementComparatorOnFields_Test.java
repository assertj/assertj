/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.IterableElementComparisonStrategy;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.testkit.Name;
import org.assertj.core.testkit.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings({ "unused", "FieldCanBeLocal" })
class IterableAssert_usingRecursiveFieldByFieldElementComparatorOnFields_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;

  @BeforeEach
  void before() {
    iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.usingRecursiveFieldByFieldElementComparatorOnFields("field");
  }

  @Override
  protected void verify_internal_effects() {
    Iterables iterables = getIterables(assertions);
    then(iterablesBefore).isNotSameAs(iterables);
    then(iterables.getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    then(getObjects(assertions).getComparisonStrategy()).isInstanceOf(IterableElementComparisonStrategy.class);
    var recursiveComparisonConfiguration = RecursiveComparisonConfiguration.builder()
                                                                           .withComparedFields("field")
                                                                           .build();
    ConfigurableRecursiveFieldByFieldComparator expectedComparator = new ConfigurableRecursiveFieldByFieldComparator(recursiveComparisonConfiguration);
    then(iterables.getComparator()).isEqualTo(expectedComparator);
    then(getObjects(assertions).getComparisonStrategy()).extracting("elementComparator").isEqualTo(expectedComparator);
  }

  @Test
  void should_compare_given_fields_recursively_and_none_other() {
    // GIVEN
    Player rose = new Player(new Name("Derrick", "Rose"), "Chicago Bulls");
    rose.nickname = new Name("Crazy", "Dunks");
    Player jalen = new Player(new Name("Jalen", "Rose"), "Chicago Bulls");
    jalen.nickname = new Name("Crazy", "Defense");
    // WHEN/THEN
    then(list(rose)).usingRecursiveFieldByFieldElementComparatorOnFields("name.last", "team", "nickname.first")
                    .contains(jalen);
  }

  // https://github.com/assertj/assertj/issues/3806
  static abstract class AppliedExemptionResponse {
    private final String exemptionCode;
    private final String description;
    private final String chargeItemCode;

    public AppliedExemptionResponse(String exemptionCode, String description, String chargeItemCode) {
      this.exemptionCode = exemptionCode;
      this.description = description;
      this.chargeItemCode = chargeItemCode;
    }
  }

  static class AppliedPartialExemptionResponse extends AppliedExemptionResponse {
    private final BigDecimal value;
    private final String type;

    public AppliedPartialExemptionResponse(String exemptionCode, String description, String chargeItemCode,
                                           BigDecimal value, String type) {
      super(exemptionCode, description, chargeItemCode);
      this.value = value;
      this.type = type;
    }
  }

  static class AppliedTotalExemptionResponse extends AppliedExemptionResponse {
    public AppliedTotalExemptionResponse(String exemptionCode, String description, String chargeItemCode) {
      super(exemptionCode, description, chargeItemCode);
    }
  }

  @Test
  void should_pass_when_comparing_polymorphic_objects_with_ignoring_non_existent_fields() {
    // GIVEN
    List<AppliedExemptionResponse> actual = List.of(new AppliedTotalExemptionResponse("E1", "E1 Desc", "T1"),
                                                    new AppliedPartialExemptionResponse("E2", "E2 Desc", "T2",
                                                                                        new BigDecimal("1"), "type_0011"));

    List<AppliedExemptionResponse> expected = List.of(new AppliedTotalExemptionResponse("E1", "E1 Desc", "T1"),
                                                      new AppliedPartialExemptionResponse("E2", "E2 Desc", "T2",
                                                                                          new BigDecimal("1"), "type_0011"));

    // WHEN/THEN
    then(actual).usingRecursiveFieldByFieldElementComparatorOnFields("exemptionCode", "description", "chargeItemCode", "value")
                .ignoringNonExistentComparedFields()
                .containsExactlyInAnyOrderElementsOf(expected);
  }

  static class BaseClass {
    private final String common = "same";
  }

  static class SubType1 extends BaseClass {
    // No 'inSubType2' field
    private final String inSubType1 = "type1";
  }

  static class SubType2 extends SubType1 {
    private final String inSubType2 = "type2";
  }

  @Test
  void should_pass_when_actual_does_not_have_all_compared_fields_and_ignoringNonExistentComparedFields_is_enabled() {
    // GIVEN
    List<BaseClass> actual = List.of(new SubType1(), new SubType2());
    List<BaseClass> expected = List.of(new SubType1(), new SubType2());
    // WHEN/THEN
    then(actual).usingRecursiveFieldByFieldElementComparatorOnFields("common", "inSubType1", "inSubType2")
                .ignoringNonExistentComparedFields()
                .containsAll(expected);
  }

  @Test
  void should_fail_when_actual_does_not_have_all_compared_fields() {
    // GIVEN
    List<BaseClass> actual = List.of(new SubType1(), new SubType2());
    List<BaseClass> expected = List.of(new SubType1(), new SubType2());
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(actual).usingRecursiveFieldByFieldElementComparatorOnFields("common",
                                                                                                                               "inSubType1",
                                                                                                                               "inSubType2")
                                                                          .containsAll(expected));
    // THEN
    then(exception).hasMessage("The following fields don't exist: {inSubType2}");
  }

}
