/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.api.recursive.comparison.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.time.Duration;
import java.time.Instant;

import org.assertj.core.api.recursive.comparison.ComparingProperties;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_Test {

  private final ComparingProperties comparingProperties = new ComparingProperties();

  @Test
  void should_pass_if_record_components_are_equal() {
    // GIVEN
    FooRecord foo1 = new FooRecord(1, 22, "foo1");
    FooRecord foo2 = new FooRecord(1, 22, "foo1");
    // WHEN/THEN (would have failed if getName() or Ignored() were used)
    then(foo1).usingRecursiveComparison()
              .withIntrospectionStrategy(comparingProperties)
              .isEqualTo(foo2);
  }

  @Test
  void should_pass_if_record_with_nested_record_are_equal() {
    // GIVEN
    var baz1 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    var baz2 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    // WHEN/THEN
    then(baz1).usingRecursiveComparison()
              .withIntrospectionStrategy(comparingProperties)
              .isEqualTo(baz2);
  }

  @Test
  void should_be_faster_the_second_time_as_the_getter_introspection_is_cached() {
    // GIVEN
    var baz1 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    var baz2 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    // WHEN
    long duration1 = durationOfComparingRecursively(baz1, baz2);
    long duration2 = durationOfComparingRecursively(baz1, baz2);
    // THEN
    then(duration2).isLessThan(duration1);
    IO.println("no cache run: " + duration1 + "ms | run with cache: " + duration2 + "ms");
  }

  private long durationOfComparingRecursively(BazRecord baz1, BazRecord baz2) {
    var start = Instant.now();
    then(baz1).usingRecursiveComparison()
              .withIntrospectionStrategy(comparingProperties)
              .isEqualTo(baz2);
    var end = Instant.now();
    return Duration.between(start, end).toMillis();
  }

  @Test
  void should_pass_when_mixing_records_and_regular_java_classes() {
    // GIVEN
    var baz1 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    var baz2 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    var obj1 = new StandardClassWithRecord("obj", 1, new RegularBar("reg bar", 2),
                                           new RecordWithRegularClassField("rec", new RegularBar("reg bar", 2)), baz1);
    var obj2 = new StandardClassWithRecord("obj", 1, new RegularBar("reg bar", 2),
                                           new RecordWithRegularClassField("rec", new RegularBar("reg bar", 2)), baz2);
    // WHEN/THEN
    then(obj1).usingRecursiveComparison()
              .withIntrospectionStrategy(comparingProperties)
              .isEqualTo(obj2);
  }

  @Test
  void should_fail_when_mixing_records_and_regular_java_classes() {
    // GIVEN
    var baz1 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    var baz2 = new BazRecord("baz", new BarRecord("bar", new FooRecord(1, 22, "foo1")));
    var obj1 = new StandardClassWithRecord("obj", 1, new RegularBar("reg bar", 2),
                                           new RecordWithRegularClassField("rec", new RegularBar("oops1", 2)), baz1);
    var obj2 = new StandardClassWithRecord("obj", 1, new RegularBar("reg bar", 2),
                                           new RecordWithRegularClassField("rec", new RegularBar("oops2", 2)), baz2);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(obj1)
                                                                    .usingRecursiveComparison()
                                                                    .withIntrospectionStrategy(comparingProperties)
                                                                    .isEqualTo(obj2));
    // THEN
    then(assertionError).hasMessageContainingAll("field/property 'rec.bar.name' differ:",
                                                 "actual value  : \"oops1\"",
                                                 "expected value: \"oops2\"");
  }

  public record BarRecord(String name, FooRecord foo) {
  }

  public record BazRecord(String name, BarRecord bar) {
  }

  public record RecordWithRegularClassField(String name, RegularBar bar) {
  }

  @SuppressWarnings({ "unused", "FieldCanBeLocal", "ClassCanBeRecord" })
  public static class StandardClassWithRecord {
    private final String name;
    private final Integer num;
    private final RegularBar bar;
    private final RecordWithRegularClassField rec;
    private final BazRecord bazRecord;

    public StandardClassWithRecord(String name, Integer num, RegularBar bar, RecordWithRegularClassField rec,
                                   BazRecord bazRecord) {
      this.name = name;
      this.num = num;
      this.bar = bar;
      this.rec = rec;
      this.bazRecord = bazRecord;
    }

    public String getName() {
      return name;
    }

    public Integer getNum() {
      return num;
    }

    public BazRecord getBazRecord() {
      return bazRecord;
    }

    public RecordWithRegularClassField getRec() {
      return rec;
    }

    public RegularBar getBar() {
      return bar;
    }
  }

  @SuppressWarnings({ "unused", "FieldCanBeLocal", "ClassCanBeRecord" })
  public static class RegularBar {
    private final String name;
    private final Integer num;

    public RegularBar(String name, Integer num) {
      this.name = name;
      this.num = num;
    }

    public String getName() {
      return name;
    }

    public Integer getNum() {
      return num;
    }
  }

  @Test
  void should_fail_if_record_components_differ() {
    // GIVEN
    FooRecord foo1 = new FooRecord(1, 22, "foo1");
    FooRecord foo2 = new FooRecord(1, 22, "foo2");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(foo1)
                                                                    .usingRecursiveComparison()
                                                                    .withIntrospectionStrategy(comparingProperties)
                                                                    .isEqualTo(foo2));
    // THEN
    then(assertionError).hasMessageContainingAll("field/property 'name' differ:",
                                                 "actual value  : \"foo1\"",
                                                 "expected value: \"foo2\"");
  }

}
