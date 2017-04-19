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
  /**
   * @return the value of this description.
   */
  public abstract String value();

  @Override
  public String toString() {
    return value();
  }

  public static Description emptyIfNull(Description description) {
    return description == null ? emptyDescription() : description;
  }

  public static String mostRelevantDescription(Description existingDescription, String newDescription) {
    boolean isDescriptionSet = existingDescription != null && !isNullOrEmpty(existingDescription.value());
    return isDescriptionSet ? existingDescription.value() : newDescription;
  }

}