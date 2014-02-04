/*
 * Created on Jan 29, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.error.ShouldExist.shouldExist;


import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.*;

/**
 * Tests for <code>{@link ShouldExist#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Yvonne Wang
 */
public class ShouldExist_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldExist(new FakeFile("xyz"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \nExpecting file:<xyz> to exist", message);
  }
}
