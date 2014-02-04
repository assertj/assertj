/*
 * Created on Jul 05, 2012
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

import static org.assertj.core.error.ShouldBeWritable.shouldBeWritable;
import static org.junit.Assert.assertEquals;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeWritable}</code>.
 * 
 * @author Olivier Demeijer
 */

public class ShouldBeWritableTest {
  ErrorMessageFactory factory;

  @Before public void setup() {
    factory = shouldBeWritable(new FakeFile("pathname"));
  }

  @Test public void createExpectedMessage() {
    String actualMessage = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \nFile:\n <pathname>\nshould be writable", actualMessage);
  }
}
