/*
 * Created on Sep 26, 2010
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
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.test.TestData.fivePixelBlueImage;
import static org.fest.util.Collections.list;

import java.awt.Dimension;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link DoesNotHaveSize#create(Description)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class DoesNotHaveSize_create_Test {

  private ErrorMessage errorMessage;

  @Before public void setUp() {
  }

  @Test public void should_create_error_message_when_actual_is_Collection() {
    errorMessage = doesNotHaveSize(list("Luke", "Yoda"), 8);
    String message = errorMessage.create(new TextDescription("Test"));
    assertEquals("[Test] expected size:<8> but was:<2> in:<['Luke', 'Yoda']>", message);
  }

  @Test public void should_create_error_message_when_actual_is_BufferedImage() {
    errorMessage = doesNotHaveSize(fivePixelBlueImage(), new Dimension(5, 5), new Dimension(6, 6));
    String message = errorMessage.create(new TextDescription("Test"));
    assertEquals("[Test] expected image size:<(w=6, h=6)> but was:<(w=5, h=5)>", message);
  }
}
