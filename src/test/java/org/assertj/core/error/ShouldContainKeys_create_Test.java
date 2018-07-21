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
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Map;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;

/**
 * Tests for <code>{@link ShouldContainKeys#create(Description)}</code>.
 * 
 * @author Nicolas François
 * @author Willima Telloue
 * @author Joel Costigliola
 */
public class ShouldContainKeys_create_Test {

  @Test
  public void should_create_error_message() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainKeys(map, newLinkedHashSet("name"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting:%n <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%nto contain key:%n <\"name\">"));
  }

  @Test
  public void should_create_error_message_with_multiple_keys() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainKeys(map, newLinkedHashSet("name", "color"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting:%n <{\"color\"=\"green\", \"name\"=\"Yoda\"}>%nto contain keys:%n <[\"name\", \"color\"]>"));
  }
}
