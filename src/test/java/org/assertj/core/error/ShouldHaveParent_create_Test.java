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
import static org.assertj.core.error.ShouldHaveParent.FILE_NOT_EXPECTED_PARENT;
import static org.assertj.core.error.ShouldHaveParent.FILE_NO_PARENT;
import static org.assertj.core.error.ShouldHaveParent.PATH_NOT_EXPECTED_PARENT;
import static org.assertj.core.error.ShouldHaveParent.PATH_NO_PARENT;
import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ShouldHaveParent#shouldHaveParent(File, File)} and {@link
 * ShouldHaveParent#shouldHaveParent(Path, Path)}
 *
 * @author Jean-Christophe Gay
 * @author Francis Galiegue
 */
public class ShouldHaveParent_create_Test
{
    private final File expectedFileParent = new FakeFile("expected.parent");
    private final Path expectedPathParent = mock(Path.class);

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
    public void should_create_error_message_when_file_has_no_parent()
    {
        final File actual = spy(new FakeFile("actual"));
        when(actual.getParentFile()).thenReturn(null);

        factory = shouldHaveParent(actual, expectedFileParent);
        actualMessage = factory.create(description, representation);

        expectedMessage = String.format("[Test] " + FILE_NO_PARENT,
            actual, expectedFileParent);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void should_create_error_message_when_file_does_not_have_expected_parent()
    {
        final File actual = spy(new FakeFile("actual"));
        final FakeFile actualParent = new FakeFile("not.expected.parent");
        when(actual.getParentFile()).thenReturn(actualParent);

        factory = shouldHaveParent(actual, expectedFileParent);
        actualMessage = factory.create(description, representation);

        expectedMessage = String.format("[Test] " + FILE_NOT_EXPECTED_PARENT,
            actual, expectedFileParent, actualParent);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void should_create_error_message_when_path_has_no_parent()
    {
        final Path actual = mock(Path.class);

        factory = shouldHaveParent(actual, expectedPathParent);
        actualMessage = factory.create(description, representation);

        expectedMessage = String.format("[Test] " + PATH_NO_PARENT,
            actual, expectedPathParent);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void should_create_error_message_when_path_does_not_have_expected_parent()
    {
        final Path actual = mock(Path.class);
        final Path actualParent = mock(Path.class);

        factory = shouldHaveParent(actual, actualParent, expectedPathParent);
        actualMessage = factory.create(description, representation);

        expectedMessage = String.format("[Test] " + PATH_NOT_EXPECTED_PARENT,
            actual, expectedPathParent, actualParent);

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
