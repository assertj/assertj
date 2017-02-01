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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldExist.PATH_SHOULD_EXIST;
import static org.assertj.core.error.ShouldExist.PATH_SHOULD_EXIST_NO_FOLLOW_LINKS;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldExist.shouldExistNoFollowLinks;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ShouldExist#create(Description, Representation)}
 * 
 * @author Yvonne Wang
 */
public class ShouldExist_create_Test {

  private TestDescription description;
  private Representation representation;

  private ErrorMessageFactory factory;
  private String actualMessage;
  private String expectedMessage;

  @Before
  public void setUp() {
    description = new TestDescription("Test");
    representation = new StandardRepresentation();
  }

  @Test
  public void should_create_error_message_for_File() {
    factory = shouldExist(new FakeFile("xyz"));
    actualMessage = factory.create(description, representation);

    expectedMessage = String.format("[Test] %nExpecting file:%n  <xyz>%nto exist.");

    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void should_create_error_message_for_Path_following_symbolic_links() {
    final Path actual = mock(Path.class);

    factory = shouldExist(actual);
    actualMessage = factory.create(description, representation);

    expectedMessage = String.format("[Test] " + PATH_SHOULD_EXIST, actual);

    assertThat(actualMessage).isEqualTo(expectedMessage);
  }
  
  @Test
  public void should_create_error_message_for_Path_not_following_symbolic_links() {
	final Path actual = mock(Path.class);
	
	factory = shouldExistNoFollowLinks(actual);
	actualMessage = factory.create(description, representation);
	
	expectedMessage = String.format("[Test] " + PATH_SHOULD_EXIST_NO_FOLLOW_LINKS, actual);
	
	assertThat(actualMessage).isEqualTo(expectedMessage);
  }
}
