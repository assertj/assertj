/*
 * Created on Jun 3, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;
import static org.assertj.core.test.Maps.mapOf;

import java.util.Map;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldNotContainKey#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldNotContainValue_create_Test {

  @Test
  public void should_create_error_message() {
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldNotContainValue(map, "green");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \nExpecting:\n <{\"name\"=\"Yoda\", \"color\"=\"green\"}>\nnot to contain value:\n <\"green\">", message);
  }

}
