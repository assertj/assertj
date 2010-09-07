/*
 * Created on Jul 26, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import org.fest.assertions.description.*;

/**
 * Validation of a <code>{@link Description}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class DescriptionValidator {

  /**
   * Validates that given description (as text) is not {@code null}.
   * @param d the description to verify.
   * @return a new {@code Description} that uses the given text as its value.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public static Description notNull(String d) {
    if (d == null) throw bomb();
    return new TextDescription(d);
  }

  /**
   * Validates that the given description is not {@code null}.
   * @param d the description to verify.
   * @return the given description, if it is not {@code null}.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public static Description notNull(Description d) {
    if (d == null) throw bomb();
    return d;
  }

  private static RuntimeException bomb() {
    return new NullPointerException("The description to set should not be null");
  }

  private DescriptionValidator() {}
}
