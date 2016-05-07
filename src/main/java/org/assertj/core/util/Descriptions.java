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
package org.assertj.core.util;

import org.assertj.core.api.AbstractAssert;

/**
 * Helper methods for working with descriptions
 */
public class Descriptions {

  /**
   * A helper method to create the description after navigating the assert class to a property or method call.
   *
   * If there is no description set on the asssert object then we use the class name to avoid using 'null'.
   *
   * @param asserter the assert object we are navigating from
   * @param propertyName the text of the property, field or method call we are navigating into
   * @return a combined description of the asserter and the navigation property, field or method call
   */
  public static String navigateDescription(AbstractAssert asserter, String propertyName) {
    String text = asserter.descriptionText();
    if (text == null || text.length() == 0) {
      text = asserter.getClass().getSimpleName();
      String postfix = "Assert";
      if (text.endsWith(postfix)) {
        text = text.substring(0, text.length() - postfix.length());
      }
    }
    return text + "." + propertyName;
  }

  private Descriptions() {}

}
