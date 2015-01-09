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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.error.ShouldStartWithPath.PATH_SHOULD_START_WITH;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.mockito.Mockito.mock;

public final class ShouldStartWithPath_create_Test
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
    public void should_create_error_message()
    {
        final Path actual = mock(Path.class);
        final Path other = mock(Path.class);

        factory = shouldStartWith(actual, other);
        actualMessage = factory.create(description, representation);

        expectedMessage = String.format("[Test] " + PATH_SHOULD_START_WITH,
            actual, other);

        assertEquals(expectedMessage, actualMessage);
    }
}
