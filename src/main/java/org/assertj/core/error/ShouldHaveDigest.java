/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.DigestDiff;

/**
 * Creates an error message indicating that an assertion that verifies that file/inputStream/path have digest failed.
 *
 * @author Valeriy Vyrva
 */
public class ShouldHaveDigest extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveDigest(String actualSource, DigestDiff diff) {
    return new ShouldHaveDigest(actualSource, diff);
  }

  private ShouldHaveDigest(String actualSource, DigestDiff diff) {
    super("%nPath:%n  <%s>%n  expected to have %s-digest <%s>%n  but have <%s>"
      , actualSource, diff.getDigest().getAlgorithm(), diff.getExpected(), diff.getActual()
    );
  }
}
