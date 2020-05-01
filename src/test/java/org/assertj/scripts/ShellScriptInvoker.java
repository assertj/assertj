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
package org.assertj.scripts;

import static com.google.common.base.Charsets.UTF_8;
import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Files.currentFolder;

import java.io.File;
import java.io.IOException;

import org.assertj.core.util.Files;

/**
 * A class that invokes the shell script and compare the output.
 *
 * <p>It will use a temporary file to write the input and call the script, then it will compare the file to the expected value</p>
 * @author XiaoMingZHM, Eveneko
 */
public class ShellScriptInvoker {
  private static final String TEMP_FILE_NAME = "convert-junit-assertions-to-assertj-test-temp-file.txt";
  private static final String TEMP_DIRECTORY = "target";
  private static final File TEMP_FILE = new File(TEMP_DIRECTORY, TEMP_FILE_NAME);
  private String conversionScript;
  private String root;

  public ShellScriptInvoker(String conversionScript) {
    // get the absolute path for this repository.
    this.root = currentFolder().getAbsolutePath().replace(File.separator, "/");
    // target directory is created by maven for each build, it is a temporary directory (deleted by mvn clean)
    // the path of the shell script it should test.
    this.conversionScript = conversionScript;
  }

  public void startTest(String input, String expected) throws Exception {
    try {
      writeToTempFile(input);
      convertAssertionsInTempFile();
      String convertedInput = readTempFile();
      assertThat(convertedInput).isEqualTo(expected);
    } finally {
      deleteTempFile();
    }
  }

  private void convertAssertionsInTempFile() throws Exception {
    String shellCommand = format("cd %s/%s && ", root, TEMP_DIRECTORY) + // go to TEMP_DIRECTORY
                          format("chmod +x %s/%s && ", root, conversionScript) + // make sure the script is executable
                          format("%s/%s %s", root, conversionScript, TEMP_FILE_NAME); // run the script
    Process process = getRuntime().exec(array("sh", "-c", shellCommand), null, null);
    int status = process.waitFor();
    if (status != 0) throw new RuntimeException("return status of shell script is " + status);
  }

  private String readTempFile() {
    return Files.contentOf(TEMP_FILE, UTF_8);
  }

  // write the input to the file. create it if it doesn't exist.
  private void writeToTempFile(String input) throws IOException {
    writeStringToFile(TEMP_FILE, input, UTF_8);
  }

  private void deleteTempFile() {
    deleteQuietly(TEMP_FILE);
  }
}
