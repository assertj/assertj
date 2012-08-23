/*
 * Created on Jun 12, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.ShouldBeOfClassIn.shouldBeOfClassIn;
import static org.fest.util.Lists.newArrayList;

import java.io.File;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeOfClassIn#create(Description)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldBeOfClassIn_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeOfClassIn("Yoda", newArrayList(Long.class, File.class));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"));
    assertEquals("[Test] expected <'Yoda'> should have type in <[java.lang.Long, java.io.File]> but was:<java.lang.String>",
        message);
  }
}
