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
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.core.util.Strings.isNullOrEmpty;
import static org.assertj.core.util.Strings.quote;

import org.assertj.core.description.Description;
import org.assertj.core.description.EmptyTextDescription;
import org.assertj.core.description.TextDescription;
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

  private static final String EMPTY_STRING = "";
  private String overridingErrorMessage;
  private Description description;
  private Representation representation;

  public WritableAssertionInfo(Representation customRepresentation) {
    useRepresentation(customRepresentation == null ? CONFIGURATION_PROVIDER.representation() : customRepresentation);
  }

  public WritableAssertionInfo() {
    useRepresentation(CONFIGURATION_PROVIDER.representation());
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
   * Returns the text of this object's description, it is an empty String if no description was set.
   *
   * @return the text of this object's description.
   */
  public String descriptionText() {
    return description == null ? EMPTY_STRING : description.value();
  }

  /**
   * Returns whether the text of this object's description was set.
   *
   * @return whether the text of this object's description was set.
   */
  public boolean hasDescription() {
    return description != null && !isNullOrEmpty(description.value());
  }

  /**
   * Sets the description of an assertion, if given null an empty {@link Description} is set.
   *
   * @param newDescription the new description.
   * @param args           if {@code newDescription} is a format String, {@code args} is argument of {@link String#format(String, Object...)}
   * @see #description(Description)
   */
  public void description(String newDescription, Object... args) {
    description = new TextDescription(newDescription, args);
  }

  /**
   * Sets the description of an assertion, if given null an empty {@link Description} is set.
   * <p> 
   * To remove or clear the description, pass a <code>{@link EmptyTextDescription}</code> as
   * argument.
   *
   * @param newDescription the new description.
   */
  public void description(Description newDescription) {
    description = Description.emptyIfNull(newDescription);
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

  public static String mostRelevantDescriptionIn(WritableAssertionInfo info, String newDescription) {
    return info.hasDescription() ? info.descriptionText() : newDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    String format = "%s[overridingErrorMessage=%s, description=%s, representation=%s]";
    return format(format, getClass().getSimpleName(), quote(overridingErrorMessage()), quote(descriptionText()),
                  quote(representation()));
  }

}
