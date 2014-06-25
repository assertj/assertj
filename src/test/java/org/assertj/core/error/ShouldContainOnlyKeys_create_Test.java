/*
 * Created on Sep 17, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Map;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ShouldContainOnlyKeys#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Joel Costigliola.
 */
public class ShouldContainOnlyKeys_create_Test {

  private ErrorMessageFactory factory;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    factory = shouldContainOnlyKeys((Map<String, String>) mapOf(entry("name", "Yoda"), entry("color", "green")),
                                    newArrayList("jedi", "color"),
                                    newLinkedHashSet("name"),
                                    newLinkedHashSet("jedi"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \n"
                 + "Expecting:\n"
                 + "  <{\"name\"=\"Yoda\", \"color\"=\"green\"}>\n"
                 + "to contain only following keys:\n"
                 + "  <[\"jedi\", \"color\"]>\n"
                 + "keys not found:\n"
                 + "  <[\"name\"]>\n"
                 + "and keys not expected:\n"
                 + "  <[\"jedi\"]>\n", message);
  }

}
