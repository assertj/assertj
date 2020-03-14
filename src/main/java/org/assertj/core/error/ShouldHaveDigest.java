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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import org.assertj.core.internal.DigestDiff;

/**
 * Creates an error message indicating that an assertion that verifies that file/inputStream/path have digest failed.
 *
 * @author Valeriy Vyrva
 */
public class ShouldHaveDigest extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldHaveDigest(Path actualSource, DigestDiff diff) {
    return new ShouldHaveDigest(actualSource, diff);
  }

  public static ErrorMessageFactory shouldHaveDigest(File actualSource, DigestDiff diff) {
    return new ShouldHaveDigest(actualSource, diff);
  }

  public static ErrorMessageFactory shouldHaveDigest(InputStream actualSource, DigestDiff diff) {
    return new ShouldHaveDigest(actualSource, diff);
  }

  private ShouldHaveDigest(Path actualSource, DigestDiff diff) {
    super(errorMessage("Path", diff), actualSource, diff.getExpected(), diff.getActual());
  }

  private ShouldHaveDigest(File actualSource, DigestDiff diff) {
    super(errorMessage("File", diff), actualSource, diff.getExpected(), diff.getActual());
  }

  private ShouldHaveDigest(InputStream actualSource, DigestDiff diff) {
    super(errorMessage("InputStream", diff), actualSource, diff.getExpected(), diff.getActual());
  }

  private static String errorMessage(String actualType, DigestDiff diff) {
    return "%nExpecting " + actualType + " %s " + diff.getDigestAlgorithm() + " digest to be:%n" +
           "  <%s>%n" +
           "but was:%n" +
           "  <%s>";
  }

}
