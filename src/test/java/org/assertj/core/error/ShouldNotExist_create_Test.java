/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldNotExist.FILE_SHOULD_NOT_EXIST;
import static org.assertj.core.error.ShouldNotExist.PATH_SHOULD_NOT_EXIST;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldNotExist#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Yvonne Wang
 */
public class ShouldNotExist_create_Test {

  private TestDescription description;
  private StandardRepresentation representation;

  private ErrorMessageFactory factory;
  private String actualMessage;
  private String expectedMessage;

  @BeforeEach
  public void setUp() {
	description = new TestDescription("Test");
	representation = new StandardRepresentation();
  }

  @Test
  public void should_create_error_message_for_File_object() {
	final FakeFile file = new FakeFile("xyz");

	factory = shouldNotExist(file);
	actualMessage = factory.create(description, representation);

	expectedMessage = String.format("[Test] " + FILE_SHOULD_NOT_EXIST, file);

	assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void should_create_error_message_for_Path_object() {
	final Path path = mock(Path.class);

	factory = shouldNotExist(path);
	actualMessage = factory.create(description, representation);

	expectedMessage = String.format("[Test] " + PATH_SHOULD_NOT_EXIST, path);

	assertThat(actualMessage).isEqualTo(expectedMessage);
  }
}
