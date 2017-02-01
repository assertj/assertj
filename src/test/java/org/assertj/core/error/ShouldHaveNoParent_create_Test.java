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

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveNoParent.FILE_HAS_PARENT;
import static org.assertj.core.error.ShouldHaveNoParent.PATH_HAS_PARENT;
import static org.assertj.core.error.ShouldHaveNoParent.shouldHaveNoParent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ShouldHaveNoParent#shouldHaveNoParent(File)} and {@link ShouldHaveNoParent#shouldHaveNoParent(Path)}
 * 
 * @author Jean-Christophe Gay
 * @author Francis Galiegue
 */
public class ShouldHaveNoParent_create_Test
{

  private TestDescription description;
  private Representation representation;

  private ErrorMessageFactory factory;
  private String actualMessage;
  private String expectedMessage;

  @Before
  public void setup()
  {
	description = new TestDescription("Test");
	representation = new StandardRepresentation();
  }

  @Test
  public void should_create_error_message_when_file_has_a_parent()
  {
	final File file = mock(File.class);
	final FakeFile parent = new FakeFile("unexpected.parent");
	when(file.getParentFile()).thenReturn(parent);
	String fileAbsolutePath = "/path/to/file";
	when(file.getAbsolutePath()).thenReturn(fileAbsolutePath);

	factory = shouldHaveNoParent(file);
	actualMessage = factory.create(description, representation);

	expectedMessage = String.format("[Test] " + FILE_HAS_PARENT, fileAbsolutePath, parent);

	assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void should_create_error_message_when_path_has_a_parent()
  {
	final Path path = mock(Path.class);
	final Path parent = mock(Path.class);
	when(path.getParent()).thenReturn(parent);

	factory = shouldHaveNoParent(path);
	actualMessage = factory.create(description, representation);

	expectedMessage = String.format("[Test] " + PATH_HAS_PARENT, path, parent);

	assertThat(actualMessage).isEqualTo(expectedMessage);
  }
}
