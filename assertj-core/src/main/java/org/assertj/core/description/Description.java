/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.description;

import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.util.Strings.isNullOrEmpty;

/**
 * The description of a value.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public abstract class Description {

  /** Creates a new description. */
  public Description() {}

  /**
   * Returns this description's value.
   *
   * @return the value of this description.
   */
  public abstract String value();

  @Override
  public String toString() {
    return value();
  }

  /**
   * Returns an empty description when the given description is {@code null}.
   *
   * @param description the description
   * @return the description or an empty one
   */
  public static Description emptyIfNull(Description description) {
    return description == null ? emptyDescription() : description;
  }

  /**
   * Returns the existing description when set, otherwise the new description.
   *
   * @param existingDescription the existing description
   * @param newDescription the fallback description
   * @return the most relevant description
   */
  public static String mostRelevantDescription(Description existingDescription, String newDescription) {
    boolean isDescriptionSet = existingDescription != null && !isNullOrEmpty(existingDescription.value());
    return isDescriptionSet ? existingDescription.value() : newDescription;
  }

}