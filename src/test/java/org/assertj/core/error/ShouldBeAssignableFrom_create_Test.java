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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;

import org.assertj.core.api.Assertions;
import org.assertj.core.description.TextDescription;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldBeAssignableFrom#shouldBeAssignableFrom(Class, java.util.Set, java.util.Set)}</code>
 *
 * @author William Delanoue
 */
public class ShouldBeAssignableFrom_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeAssignableFrom(ShouldBeAssignableFrom_create_Test.class,
        Sets.<Class<?>> newLinkedHashSet(String.class, Integer.class), Sets.<Class<?>> newLinkedHashSet((String.class)));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    Assertions.assertThat(message).isEqualTo(
        "[Test] \n" + "Expecting\n" + " <org.assertj.core.error.ShouldBeAssignableFrom_create_Test>\n"
            + "to be assignable from:\n" + " <[java.lang.String, java.lang.Integer]>\n"
            + "but was not assignable from:\n" + " <[java.lang.String]>");
  }
}
