/*
 * Created on Nov 22, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.DoesNotContainSequence.doesNotContainSequence;
import static org.fest.util.Collections.list;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link DoesNotContainSequence#create(Description)}</code>.
 *
 * @author Alex Ruiz
 */
public class DoesNotContainSequence_create_Test {

  private ErrorMessage errorMessage;

  @Before public void setUp() {
    errorMessage = doesNotContainSequence(list("Yoda", "Luke"), list("Han", "Leia"));
  }

  @Test public void should_create_error_message() {
    String message = errorMessage.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<['Yoda', 'Luke']> to contain sequence:<['Han', 'Leia']>", message);
  }
}
