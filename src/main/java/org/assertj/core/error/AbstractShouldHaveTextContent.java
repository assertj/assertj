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
package org.assertj.core.error;

import java.util.List;

import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.diff.Delta;

/**
 * Base class for text content error.
 */
public class AbstractShouldHaveTextContent extends BasicErrorMessageFactory {

  protected String diffs;

  public AbstractShouldHaveTextContent(String format, Object... arguments) {
    super(format, arguments);
  }

  @Override
  public String create(Description d, Representation representation) {
    // we append diffs here as we can't add in super constructor call, see why below.
    //
    // case 1 - append diffs to String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:" + diffs, actual, expected);
    // this leads to a MissingFormatArgumentException if diffs contains a format specifier (like %s) because the String
    // will finally be evaluated with String.format
    //
    // case 2 - add as format arg to the String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:"actual, expected, diffs);
    // this is better than case 1 but the diffs String will be quoted before the class to String.format as all String in
    // AssertJ error message. This is not what we want.
    //
    // The solution is to keep diffs as an attribute and append it after String.format has been applied on the error
    // message.
    return super.create(d, representation) + diffs;
  }

  protected static String diffsAsString(List<Delta<String>> diffsList) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Delta<String> diff : diffsList)
      stringBuilder.append(org.assertj.core.util.Compatibility.System.lineSeparator()).append(diff);
    return stringBuilder.toString();
  }

}