/*
 * Created on Sep 18, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.IsNotNullOrEmpty.isNotNullOrEmpty;
import static org.fest.util.Collections.list;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link IsNotNullOrEmpty#create(Description)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class IsNotNullOrEmpty_create_Test {

  private ErrorMessage errorMessage;

  @Before public void setUp() {
    errorMessage = isNotNullOrEmpty(list("Luke", "Yoda"));
  }

  @Test public void should_create_error_message() {
    String message = errorMessage.create(new TextDescription("Test"));
    assertEquals("[Test] expecting null or empty but was:<['Luke', 'Yoda']>", message);
  }
}
