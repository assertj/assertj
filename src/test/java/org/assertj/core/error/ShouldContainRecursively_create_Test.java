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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainRecursively.directoryShouldContainRecursively;
import static org.assertj.core.util.Lists.list;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class ShouldContainRecursively_create_Test {

  @Test
  public void should_create_error_message_for_files() {
    // GIVEN
    File root = new File("root");
    File foo = new File(root, "foo");
    File bar = new File(root, "b%%ar% %s %n");
    ErrorMessageFactory factory = directoryShouldContainRecursively(root, list(foo, bar), "regex:.*txt");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    // we can't put the concrete message as root, foo and bar will have different absolute path on different machines.
    then(message).startsWith(format("[Test] %nExpecting directory or any of its subdirectories (recursively):%n"))
                 .containsSubsequence(root.toString(),
                                      "to contain at least one file matching \"regex:.*txt\" but there was none.",
                                      "The directory content was:",
                                      foo.toString(),
                                      bar.toString());
  }

  @Test
  public void should_create_error_message_for_paths() {
    // GIVEN
    Path root = Paths.get("root");
    Path foo = root.resolve("foo");
    Path bar = root.resolve("b%%ar% %s %n");
    ErrorMessageFactory factory = directoryShouldContainRecursively(root, list(foo, bar), "regex:.*txt");
    // WHEN
    String message = factory.create(new TextDescription("Test"));
    // THEN
    // we can't put the concrete message as root, foo and bar will have different absolute path on different machines.
    then(message).startsWith(format("[Test] %nExpecting directory or any of its subdirectories (recursively):%n"))
                 .containsSubsequence(root.toString(),
                                      "to contain at least one file matching \"regex:.*txt\" but there was none.",
                                      "The directory content was:",
                                      foo.toString(),
                                      bar.toString());
  }

}
