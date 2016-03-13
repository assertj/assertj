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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.test.ErrorMessages.descriptionIsNull;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link DescriptionValidations#checkIsNotNull(org.assertj.core.description.Description)}</code> and
 * <code>{@link DescriptionValidations#checkIsNotNull(String)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class DescriptionValidator_notNull_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_description_is_null() {
    thrown.expectNullPointerException(descriptionIsNull());
    Description d = null;
    DescriptionValidations.checkIsNotNull(d);
  }

  @Test
  public void should_throw_error_if_text_description_is_null() {
    thrown.expectNullPointerException(descriptionIsNull());
    String d = null;
    DescriptionValidations.checkIsNotNull(d);
  }

  @Test
  public void should_return_description_with_given_text() {
    Description d = DescriptionValidations.checkIsNotNull("Yoda");
    assertThat(d.value()).isEqualTo("Yoda");
  }

  @Test
  public void should_return_same_description() {
    Description e = new TextDescription("Yoda");
    Description d = DescriptionValidations.checkIsNotNull(e);
    assertThat(d).isSameAs(e);
  }
}
