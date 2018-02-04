/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.assumptions.BaseAssumptionRunner.run;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.ProxyableMapAssert;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * verify that assertions final methods or methods changing the object under test in {@link MapAssert} work with assumptions 
 * (i.e. that they are proxied correctly in {@link ProxyableMapAssert}).
 */
@RunWith(Parameterized.class)
public class Map_special_assertion_methods_in_assumptions_Test extends BaseAssumptionsRunnerTest {

  private static int ranTests = 0;

  public Map_special_assertion_methods_in_assumptions_Test(AssumptionRunner<?> assumptionRunner) {
    super(assumptionRunner);
  }

  @Override
  protected void incrementRunTests() {
    ranTests++;
  }

  @SuppressWarnings("unchecked")
  @Parameters
  public static Object[][] provideAssumptionsRunners() {

    List<String> names = asList("Dave", "Jeff");
    LinkedHashSet<String> jobs = newLinkedHashSet("Plumber", "Builder");
    Iterable<String> cities = asList("Dover", "Boston", "Paris");
    int[] ranks = { 1, 2, 3 };
    Map<String, Object> iterableMap = new LinkedHashMap<>();
    iterableMap.put("name", names);
    iterableMap.put("job", jobs);
    iterableMap.put("city", cities);
    iterableMap.put("rank", ranks);

    Map<String, String> map = mapOf(entry("a", "1"), entry("b", "2"), entry("c", "3"));

    return new AssumptionRunner[][] {
        run(map,
            value -> assumeThat(value).contains(entry("a", "1"), entry("c", "3")),
            value -> assumeThat(value).contains(entry("a", "2"), entry("c", "3"))),
        run(map,
            value -> assumeThat(value).containsAnyOf(entry("a", "1"), entry("a", "2")),
            value -> assumeThat(value).containsAnyOf(entry("a", "2"), entry("a", "3"))),
        run(map,
            value -> assumeThat(value).containsExactly(entry("a", "1"), entry("b", "2"), entry("c", "3")),
            value -> assumeThat(value).containsExactly(entry("b", "2"), entry("a", "1"), entry("c", "3"))),
        run(map,
            value -> assumeThat(value).containsKeys("a", "b", "c"),
            value -> assumeThat(value).containsKeys("a", "b", "d")),
        run(map,
            value -> assumeThat(value).containsOnly(entry("b", "2"), entry("a", "1"), entry("c", "3")),
            value -> assumeThat(value).containsOnly(entry("b", "2"), entry("a", "1"))),
        run(map,
            value -> assumeThat(value).containsOnlyKeys("a", "b", "c"),
            value -> assumeThat(value).containsOnlyKeys("a", "b", "d")),
        run(map,
            value -> assumeThat(value).containsValues("1", "2"),
            value -> assumeThat(value).containsValues("1", "5")),
        run(map,
            value -> assumeThat(value).doesNotContain(entry("a", "2"), entry("a", "3")),
            value -> assumeThat(value).doesNotContain(entry("a", "1"), entry("c", "3"))),
        run(map,
            value -> assumeThat(value).doesNotContainKeys("d", "e", "f"),
            value -> assumeThat(value).doesNotContainKeys("a", "e", "f")),
        run(map,
            value -> assumeThat(value).extracting("a", "b").contains("1", "2"),
            value -> assumeThat(value).extracting("a", "b").contains("456")),
        run(iterableMap,
            value -> assumeThat(value).flatExtracting("name", "job", "city", "rank")
                                      .contains("Jeff", "Builder", "Dover", "Boston", "Paris", 1, 2, 3),
            value -> assumeThat(value).flatExtracting("name", "job", "city", "rank")
                                      .contains("Unexpected", "Builder", "Dover", "Boston", "Paris", 1, 2, 3)),
        run(map,
            value -> assumeThat(value).size().isGreaterThan(2),
            value -> assumeThat(value).size().isGreaterThan(10)),
        run(map,
            value -> assumeThat(value).size().isGreaterThan(2).returnToMap().containsKeys("a", "b", "c"),
            value -> assumeThat(value).size().isGreaterThan(2).returnToMap().containsKeys("unknown")),
        run(map,
            value -> assumeThat(value).size().isPositive().returnToMap().size().isPositive(),
            value -> assumeThat(value).size().isPositive().returnToMap().size().isNegative()),
    };
  };

  @AfterClass
  public static void afterClass() {
    assertThat(ranTests).as("number of tests run").isEqualTo(provideAssumptionsRunners().length);
  }

}
