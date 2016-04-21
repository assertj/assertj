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

import static java.lang.String.format;
import static org.assertj.core.api.DescriptionValidations.checkIsNotNull;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.core.util.Strings.quote;

import org.assertj.core.description.Description;
import org.assertj.core.description.EmptyTextDescription;
import org.assertj.core.presentation.BinaryRepresentation;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.UnicodeRepresentation;


/**
 * Writable information about an assertion.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class WritableAssertionInfo implements AssertionInfo {

  private String overridingErrorMessage;
  private Description description;
  private Representation representation;

  public WritableAssertionInfo(Representation customRepresentation) {
    useRepresentation(customRepresentation == null ? STANDARD_REPRESENTATION : customRepresentation);
  }

  public WritableAssertionInfo() {
    useRepresentation(STANDARD_REPRESENTATION);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String overridingErrorMessage() {
    return overridingErrorMessage;
  }

  /**
   * Sets the message that will replace the default message of an assertion failure.
   *
   * @param newErrorMessage the new message. It can be {@code null}.
   */
  public void overridingErrorMessage(String newErrorMessage) {
    overridingErrorMessage = newErrorMessage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Description description() {
    return description;
  }


  /**
   * Returns the text of this object's description, or {@code null} if such description is {@code null}.
   *
   * @return the text of this object's description, or {@code null} if such description is {@code null}.
   */
  public String descriptionText() {
    return description != null ? description.value() : null;
  }

  /**
   * Sets the description of an assertion.
   *
   * @param newDescription the new description.
   * @param args           if {@code newDescription} is a format String, {@code args} is argument of {@link String#format(String, Object...)}
   * @throws NullPointerException if the given description is {@code null}.
   * @see #description(Description)
   */
  public void description(String newDescription, Object... args) {
    description = checkIsNotNull(newDescription, args);
  }

  /**
   * Sets the description of an assertion. To remove or clear the description, pass a <code>{@link EmptyTextDescription}</code> as
   * argument.
   *
   * @param newDescription the new description.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public void description(Description newDescription) {
    description = checkIsNotNull(newDescription);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Representation representation() {
    return representation;
  }

  public void useHexadecimalRepresentation() {
    representation = new HexadecimalRepresentation();
  }

  public void useUnicodeRepresentation() {
    representation = new UnicodeRepresentation();
  }

  public void useBinaryRepresentation() {
    representation = new BinaryRepresentation();
  }

  public void useRepresentation(Representation newRepresentation) {
    checkNotNull(newRepresentation, "The representation to use should not be null.");
    representation = newRepresentation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    String format = "%s[overridingErrorMessage=%s, description=%s, representation=%s]";
    return format(format, getClass().getSimpleName(), quote(overridingErrorMessage()), quote(descriptionText()), quote(representation()));
  }
}
