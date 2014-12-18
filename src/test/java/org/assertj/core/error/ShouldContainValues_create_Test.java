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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainValue.shouldContainValue;
import static org.assertj.core.error.ShouldContainValues.shouldContainValues;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Sets.newHashSet;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldContainValues#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alexander Bischof
 */
public class ShouldContainValues_create_Test {

  @Test
  public void should_create_error_message() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainValues(map, newHashSet(Arrays.asList("VeryOld")));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \nExpecting:\n <{\"name\"=\"Yoda\", \"color\"=\"green\"}>\nto contain value:\n <\"VeryOld\">", message);
  }

  @Test
  public void should_create_error_message_with_multiplevalues() {
	Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
	ErrorMessageFactory factory = shouldContainValues(map, newHashSet(Arrays.asList("VeryOld", "VeryVeryOld")));
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertEquals("[Test] \nExpecting:\n <{\"name\"=\"Yoda\", \"color\"=\"green\"}>\nto contain values:\n <[\"VeryOld\", \"VeryVeryOld\"]>", message);
  }
}
