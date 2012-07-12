/*
 * Created on Jul 15, 2010
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

import static org.fest.assertions.core.DescriptionValidations.checkIsNotNull;

import org.fest.assertions.description.Description;
import org.fest.util.VisibleForTesting;

/**
 * A condition to be met by an object.
 * @param <T> the type of object this condition accepts.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public abstract class Condition<T> implements Descriptable<Condition<T>> {

  @VisibleForTesting
  Description description;

  /**
   * Creates a new </code>{@link Condition}</code>. The default description of this condition will the simple name of the
   * condition's class.
   */
  public Condition() {
    as(getClass().getSimpleName());
  }

  /**
   * Creates a new </code>{@link Condition}</code>.
   * @param description the description of this condition.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public Condition(String description) {
    as(description);
  }

  /**
   * Creates a new </code>{@link Condition}</code>.
   * @param description the description of this condition.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public Condition(Description description) {
    as(description);
  }

  /** {@inheritDoc} */
  public Condition<T> describedAs(String newDescription) {
    return as(newDescription);
  }

  /** {@inheritDoc} */
  public Condition<T> as(String newDescription) {
    description = checkIsNotNull(newDescription);
    return this;
  }

  /** {@inheritDoc} */
  public Condition<T> describedAs(Description newDescription) {
    return as(newDescription);
  }

  /** {@inheritDoc} */
  public Condition<T> as(Description newDescription) {
    description = checkIsNotNull(newDescription);
    return this;
  }

  /**
   * Returns the description of this condition.
   * @return the description of this condition.
   */
  public Description description() {
    return description;
  }

  /**
   * Verifies that the given value satisfies this condition.
   * @param value the value to verify.
   * @return {@code true} if the given value satisfies this condition; {@code false} otherwise.
   */
  public abstract boolean matches(T value);

  @Override
  public String toString() {
    return description.value();
  }
}
