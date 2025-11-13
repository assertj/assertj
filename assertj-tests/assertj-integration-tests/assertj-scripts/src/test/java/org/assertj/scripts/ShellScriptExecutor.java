/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.scripts;

import static java.lang.Runtime.getRuntime;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;

import java.nio.file.Path;

/**
 * @author XiaoMingZHM, Eveneko
 */
class ShellScriptExecutor {

  static void execute(Path script, Path inputFile) throws Exception {
    String command = "cd %s && ".formatted(script.getParent()) +
                     "chmod +x %s && ".formatted(script.getFileName()) +
                     "./%s %s".formatted(script.getFileName(), inputFile.getFileName());
    Process process = getRuntime().exec(array("sh", "-c", command), null, null);
    int status = process.waitFor();
    if (status != 0) throw new RuntimeException("Execution failed with return code: " + status);
  }

}
