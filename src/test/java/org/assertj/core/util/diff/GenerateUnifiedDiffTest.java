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
package org.assertj.core.util.diff;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.util.Files;
import org.junit.Assert;
import org.junit.Test;

public class GenerateUnifiedDiffTest {

  /** File separator. */
  private static final String FS = File.separator;

  /** The base resource path. */
  private static String BASE_FOLDER_RESOURCES = "src" + FS + "test" + FS + "resources";

  /** The base folder containing the test files. Ends with {@link #FS}. */
  private static final String MOCK_FOLDER = BASE_FOLDER_RESOURCES + FS + "diffs" + FS;

  private List<String> fileToLines(String filename) {
    return Files.linesOf(new File(MOCK_FOLDER, filename), Charset.defaultCharset());
  }

  @Test
  public void testGenerateUnified() {
    List<String> origLines = fileToLines("original.txt");
    List<String> revLines = fileToLines("revised.txt");

    verify(origLines, revLines, "original.txt", "revised.txt");
  }

  @Test
  public void testGenerateUnifiedWithOneDelta() {
    List<String> origLines = fileToLines("one_delta_test_original.txt");
    List<String> revLines = fileToLines("one_delta_test_revised.txt");

    verify(origLines, revLines, "one_delta_test_original.txt", "one_delta_test_revised.txt");
  }

  @Test
  public void testGenerateUnifiedDiffWithoutAnyDeltas() {
    List<String> test = Arrays.asList("abc");

    Patch<String> patch = DiffUtils.diff(test, test);

    DiffUtils.generateUnifiedDiff("abc", "abc", test, patch, 0);
  }

  @Test
  public void testDiff_Issue10() {
    List<String> baseLines = fileToLines("issue10_base.txt");
    List<String> patchLines = fileToLines("issue10_patch.txt");
    Patch<String> p = DiffUtils.parseUnifiedDiff(patchLines);

    DiffUtils.patch(baseLines, p);
  }

  @Test
  public void testPatchWithNoDeltas() {
    List<String> lines1 = fileToLines("issue11_1.txt");
    List<String> lines2 = fileToLines("issue11_2.txt");

    verify(lines1, lines2, "issue11_1.txt", "issue11_2.txt");
  }

  @Test
  public void testDiff5() {
    List<String> lines1 = fileToLines("5A.txt");
    List<String> lines2 = fileToLines("5B.txt");

    verify(lines1, lines2, "5A.txt", "5B.txt");
  }

  @Test
  public void testDiffWithHeaderLineInText() {
    List<String> original = new ArrayList<>();
    List<String> revised = new ArrayList<>();

    original.add("test line1");
    original.add("test line2");
    original.add("test line 4");
    original.add("test line 5");

    revised.add("test line1");
    revised.add("test line2");
    revised.add("@@ -2,6 +2,7 @@");
    revised.add("test line 4");
    revised.add("test line 5");

    Patch<String> patch = DiffUtils.diff(original, revised);
    List<String> udiff = DiffUtils.generateUnifiedDiff("original", "revised",
                                                       original, patch, 10);
    DiffUtils.parseUnifiedDiff(udiff);
  }

  private void verify(List<String> origLines, List<String> revLines,
                      String originalFile, String revisedFile) {
    Patch<String> patch = DiffUtils.diff(origLines, revLines);
    List<String> unifiedDiff = DiffUtils.generateUnifiedDiff(originalFile, revisedFile,
                                                             origLines, patch, 10);

    Patch<String> fromUnifiedPatch = DiffUtils.parseUnifiedDiff(unifiedDiff);
    List<String> patchedLines = fromUnifiedPatch.applyTo(origLines);

    assertThat(patchedLines).hasSameSizeAs(revLines);
    for (int i = 0; i < revLines.size(); i++) {
      String l1 = revLines.get(i);
      String l2 = patchedLines.get(i);
      if (!l1.equals(l2)) {
        Assert.fail("Line " + (i + 1) + " of the patched file did not match the revised original");
      }
    }
  }
}
