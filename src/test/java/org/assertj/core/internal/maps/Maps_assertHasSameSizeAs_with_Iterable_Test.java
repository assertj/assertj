/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link Maps#assertHasSameSizeAs(org.assertj.core.api.AssertionInfo, java.util.Map, Iterable)}</code>.
 * 
 * @author Nicolas François
 */
public class Maps_assertHasSameSizeAs_with_Iterable_Test extends MapsBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = mapOf(entry("name", "Yoda"), entry("job", "Yedi Master"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertHasSameSizeAs(someInfo(), null, newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    List<String> other = newArrayList("Solo", "Leia", "Yoda");

    thrown.expectAssertionError(shouldHaveSameSizeAs(actual, actual.size(), other.size())
                                .create(null, info.representation()));

    maps.assertHasSameSizeAs(info, actual, other);
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    maps.assertHasSameSizeAs(someInfo(), actual, newArrayList("Solo", "Leia"));
  }

}
