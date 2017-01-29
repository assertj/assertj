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
package org.assertj.core.test;

import static org.assertj.core.data.Index.atIndex;

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;


/**
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public final class TestData {

  private static final AssertionInfo ASSERTION_INFO = new WritableAssertionInfo();
  private static final AssertionInfo ASSERTION_INFO_AS_HEX = new WritableAssertionInfo();
  private static final TextDescription DESCRIPTION = new TextDescription(
      "who's the more foolish: the fool, or the fool who follows him?");
  private static final Index INDEX = atIndex(0);
  private static final Pattern MATCH_ANYTHING = Pattern.compile(".*");

  static {
    ((WritableAssertionInfo) ASSERTION_INFO_AS_HEX).useHexadecimalRepresentation();
  }

  public static Pattern matchAnything() {
    return MATCH_ANYTHING;
  }

  public static Index someIndex() {
    return INDEX;
  }

  public static AssertionInfo someInfo() {
    return ASSERTION_INFO;
  }

  public static AssertionInfo someHexInfo() {
    return ASSERTION_INFO_AS_HEX;
  }

  public static Description someDescription() {
    return DESCRIPTION;
  }

  public static String someTextDescription() {
    return "there's always a bigger fish";
  }

  private TestData() {}
}
