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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.description;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JoinDescription#JoinDescription(String, String, Collection)}.
 *
 * @author Edgar Asatryan
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JoinDescription_constructor_Test {
  @Test
  void should_set_values() {
    //given
    String prefix = "a";
    String suffix = "b";
    Collection<Description> descriptions = Arrays.asList(new TestDescription("1"), new TestDescription("2"));
    JoinDescription desc = new JoinDescription(prefix, suffix, descriptions);

    //then
    assertThat(desc.prefix).isEqualTo(prefix);
    assertThat(desc.suffix).isEqualTo(suffix);
    assertThat(desc.descriptions).isEqualTo(descriptions);
  }

  @Test
  void should_throw_when_descriptions_contains_null() {
    //given
    List<Description> descriptions = Arrays.asList(
      new TestDescription("1"),
      new TestDescription("2"),
      null,
      new TestDescription("3")
    );

    //then
    assertThatNullPointerException()
      .isThrownBy(() -> new JoinDescription("a", "b", descriptions))
      .withMessage("The descriptions should not contain null elements");
  }
}
