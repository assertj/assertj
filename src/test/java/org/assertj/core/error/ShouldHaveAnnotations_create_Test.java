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
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;

import java.lang.annotation.Annotation;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ShouldHaveAnnotations#shouldHaveAnnotations(Class, java.util.Collection, java.util.Collection)}}</code>
 * 
 * @author William Delanoue
 */
public class ShouldHaveAnnotations_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldHaveAnnotations(ShouldHaveAnnotations_create_Test.class,
                                   Lists.<Class<? extends Annotation>> newArrayList(Override.class, Deprecated.class),
                                   Lists.<Class<? extends Annotation>> newArrayList(SuppressWarnings.class));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
                                  "[Test] %n"
                                      + "Expecting%n"
                                      + "  <org.assertj.core.error.ShouldHaveAnnotations_create_Test>%n"
                                      + "to have annotations:%n"
                                      + "  <[java.lang.Override, java.lang.Deprecated]>%n"
                                      + "but the following annotations were not found:%n"
                                      + "  <[java.lang.SuppressWarnings]>"));
  }
}
