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
package org.assertj.core.presentation;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import org.assertj.core.description.Description;

/**
 * Encapsulate a {@link Predicate} description to be able to control how it is formatted in error messages using a
 * {@link Representation}.
 * <p>
 * Immutable class.
 */
public class PredicateDescription {
  private static final String DEFAULT = "given";
  public static final PredicateDescription GIVEN = new PredicateDescription(DEFAULT);

  // can be public as it is immutable, never null
  public final String description;

  /**
   * Build a {@link PredicateDescription}.
   * 
   * @param description must not be null
   */
  public PredicateDescription(String description) {
	requireNonNull(description, "The predicate description must not be null");
	this.description = description;
  }

  public boolean isDefault() {
	return DEFAULT.equals(description);
  }

  public static PredicateDescription fromDescription(Description description) {
    PredicateDescription desc = PredicateDescription.GIVEN;
    if (description != null) {
      String value = description.value();
      desc = value != null ? new PredicateDescription(value) : desc;
    }
    return desc;
  }
}
