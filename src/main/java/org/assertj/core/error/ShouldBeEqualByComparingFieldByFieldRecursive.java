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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

import java.util.List;

import org.assertj.core.util.DeepDifference.Difference;

/**
 */
public class ShouldBeEqualByComparingFieldByFieldRecursive extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldBeEqualComparingOnlyGivenFields(Object actual, Object other, List<Difference> differences) {
      StringBuilder descriptionOfDifferences = new StringBuilder();
      for (Difference difference : differences) {
          descriptionOfDifferences.append(format("%nPath to difference: %s%nactual: %s%nexpected: %s%n",
                  join(difference.getPath(), "->"), difference.getActual(), difference.getOther()));
      }
      return new ShouldBeEqualByComparingFieldByFieldRecursive("Expected: %s%nto equal to: %s%nwhen recursively comparing field by field, but found the following difference(s):%n" 
              + descriptionOfDifferences.toString(), actual, other);
  }

  private ShouldBeEqualByComparingFieldByFieldRecursive(String message, Object actual, Object other) {
      super(message, actual, other);
  }

  private static String join(List<String> list, String conjunction) {
      StringBuilder sb = new StringBuilder();
      boolean isFirst = true;
      for (String item : list) {
         if (isFirst) {
             isFirst = false;
         }
         else {
             sb.append(conjunction);
         }
         sb.append(item);
      }
      return sb.toString();
   }
}
