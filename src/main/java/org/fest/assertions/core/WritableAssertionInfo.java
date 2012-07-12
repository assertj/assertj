/*
 * Created on Jul 29, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.core;

import static java.lang.String.format;

import static org.fest.assertions.core.DescriptionValidations.checkIsNotNull;
import static org.fest.util.Strings.quote;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.EmptyTextDescription;

/**
 * Writable information about an assertion.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class WritableAssertionInfo implements AssertionInfo {

  private String overridingErrorMessage;
  private Description description;

  /** {@inheritDoc} */
  public String overridingErrorMessage() {
    return overridingErrorMessage;
  }

  /**
   * Sets the message that will replace the default message of an assertion failure.
   * @param newErrorMessage the new message. It can be {@code null}.
   */
  public void overridingErrorMessage(String newErrorMessage) {
    overridingErrorMessage = newErrorMessage;
  }

  /** {@inheritDoc} */
  public Description description() {
    return description;
  }

  /**
   * Returns the text of this object's description, or {@code null} if such description is {@code null}.
   * @return the text of this object's description, or {@code null} if such description is {@code null}.
   */
  public String descriptionText() {
    return description != null ? description.value() : null;
  }

  /**
   * Sets the description of an assertion.
   * @param newDescription the new description.
   * @throws NullPointerException if the given description is {@code null}.
   * @see #description(Description)
   */
  public void description(String newDescription) {
    description = checkIsNotNull(newDescription);
  }

  /**
   * Sets the description of an assertion. To remove or clear the description, pass a <code>{@link EmptyTextDescription}</code> as
   * argument.
   * @param newDescription the new description.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public void description(Description newDescription) {
    description = checkIsNotNull(newDescription);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    String format = "%s[overridingErrorMessage=%s, description=%s]";
    return format(format, getClass().getSimpleName(), quote(overridingErrorMessage()), quote(descriptionText()));
  }
}
