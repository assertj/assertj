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
package org.assertj.core.util;

import static java.io.File.separator;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Arrays.isNullOrEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.append;
import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.util.Strings.quote;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility methods related to files.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Files {
  /**
   * Returns the names of the files inside the specified directory.
   *
   * @param dirName the name of the directory to start the search from.
   * @param recurse if {@code true}, we will look in subdirectories.
   * @return the names of the files inside the specified directory.
   * @throws IllegalArgumentException if the given directory name does not point to an existing directory.
   */
  public static List<String> fileNamesIn(String dirName, boolean recurse) {
    File dir = new File(dirName);
    checkArgument(dir.isDirectory(), "%s is not a directory", quote(dirName));
    return fileNamesIn(dir, recurse);
  }

  /**
   * Returns the names of the files inside the specified directory.
   *
   * @param dir the name of the directory to start the search from.
   * @param recurse if {@code true}, we will look in subdirectories.
   * @return the names of the files inside the specified directory.
   */
  private static List<String> fileNamesIn(File dir, boolean recurse) {
    List<String> scriptNames = new ArrayList<>();
    File[] existingFiles = dir.listFiles();
    if (isNullOrEmpty(existingFiles)) {
      return scriptNames;
    }
    for (File existingFile : existingFiles) {
      if (existingFile.isDirectory()) {
        if (recurse) {
          scriptNames.addAll(fileNamesIn(existingFile, recurse));
        }
        continue;
      }
      String filename = existingFile.getAbsolutePath();
      if (!scriptNames.contains(filename)) {
        scriptNames.add(filename);
      }
    }
    return scriptNames;
  }

  /**
   * Returns the system's temporary directory.
   *
   * @return the system's temporary directory.
   * @throws RuntimeException if this method cannot find or create the system's temporary directory.
   *
   * @deprecated Use either {@link org.junit.jupiter.api.io.TempDir} or {@link org.junit.rules.TemporaryFolder}
   */
  @Deprecated
  public static File temporaryFolder() {
    File temp = new File(temporaryFolderPath());
    if (!temp.isDirectory()) {
      throw new RuntimeException("Unable to find temporary directory");
    }
    return temp;
  }

  /**
   * Returns the path of the system's temporary directory. This method appends the system's file separator at the end of
   * the path.
   *
   * @return the path of the system's temporary directory.
   */
  public static String temporaryFolderPath() {
    return append(separator).to(System.getProperty("java.io.tmpdir"));
  }

  /**
   * Creates a new file in the system's temporary directory. The name of the file will be the result of:
   * <pre><code class='java'> concat(UUID.randomUUID().toString(), &quot;.txt&quot;);</code></pre>
   *
   * @return the created file.
   */
  public static File newTemporaryFile() {
    String tempFileName = concat(UUID.randomUUID().toString(), ".txt");
    return newFile(concat(temporaryFolderPath(), tempFileName));
  }

  /**
   * Creates a new directory in the system's temporary directory. The name of the directory will be the result of:
   * <pre><code class='java'> UUID.randomUUID().toString();</code></pre>
   *
   * @return the created file.
   */
  public static File newTemporaryFolder() {
    String tempFileName = UUID.randomUUID().toString();
    return newFolder(concat(temporaryFolderPath(), tempFileName));
  }

  /**
   * Creates a new file using the given path.
   *
   * @param path the path of the new file.
   * @return the new created file.
   * @throws RuntimeException if the path belongs to an existing non-empty directory.
   * @throws RuntimeException if the path belongs to an existing file.
   * @throws UncheckedIOException if any I/O error is thrown when creating the new file.
   */
  public static File newFile(String path) {
    File file = createFileIfPathIsNotANonEmptyDirectory(path);
    try {
      if (!file.createNewFile()) {
        throw cannotCreateNewFile(path, "a file was found with the same path");
      }
    } catch (IOException e) {
      throw cannotCreateNewFile(path, e);
    }
    return file;
  }

  /**
   * Creates a new directory using the given path.
   *
   * @param path the path of the new directory.
   * @return the new created directory.
   * @throws RuntimeException if the path belongs to an existing non-empty directory.
   * @throws RuntimeException if the path belongs to an existing file.
   * @throws RuntimeException if any I/O error is thrown when creating the new directory.
   */
  public static File newFolder(String path) {
    File file = createFileIfPathIsNotANonEmptyDirectory(path);
    try {
      if (!file.mkdir()) {
        throw cannotCreateNewFile(path, "a file was found with the same path");
      }
    } catch (Exception e) {
      throw cannotCreateNewFile(path, e);
    }
    return file;
  }

  private static File createFileIfPathIsNotANonEmptyDirectory(String path) {
    File file = new File(path);
    if (file.isDirectory() && !isNullOrEmpty(file.list())) {
      throw cannotCreateNewFile(path, "a non-empty directory was found with the same path");
    }
    return file;
  }

  private static UncheckedIOException cannotCreateNewFile(String path, String reason) {
    throw cannotCreateNewFile(path, reason, null);
  }

  private static UncheckedIOException cannotCreateNewFile(String path, Exception cause) {
    throw cannotCreateNewFile(path, null, cause);
  }

  private static UncheckedIOException cannotCreateNewFile(String path, String reason, Exception cause) {
    String message = String.format("Unable to create the new file %s", quote(path));
    if (!Strings.isNullOrEmpty(reason)) {
      message = concat(message, ": ", reason);
    }
    if (cause == null) {
      throw new RuntimeException(message);
    }
    if (cause instanceof IOException) {
      throw new UncheckedIOException(message, (IOException) cause);
    }
    throw new RuntimeException(message, cause);
  }

  /**
   * Returns the current directory.
   *
   * @return the current directory.
   * @throws UncheckedIOException if the current directory cannot be obtained.
   */
  public static File currentFolder() {
    try {
      return new File(".").getCanonicalFile();
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to get current directory", e);
    }
  }

  /**
   * Deletes the given file or directory.
   *
   * @param file the file or directory to delete.
   *
   * @deprecated use https://commons.apache.org/proper/commons-io/javadocs/api-release/org/apache/commons/io/FileUtils.html#forceDelete-java.io.File- instead
   */
  @Deprecated
  public static void delete(File file) {
    if (file.isFile()) {
      if (!file.delete()) {
        System.out.println("Fail to delete " + file);
      }
      return;
    }
    if (!file.isDirectory()) {
      return;
    }
    File[] existingFiles = file.listFiles();
    if (!isNullOrEmpty(existingFiles)) {
      for (File f : existingFiles) {
        delete(f);
      }
    }
    if (!file.delete()) {
      System.out.println("Fail to delete " + file);
    }
  }

  /**
   * Loads the text content of a file into a character string.
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws IllegalArgumentException if the given character set is not supported on this platform.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  public static String contentOf(File file, String charsetName) {
    checkArgumentCharsetIsSupported(charsetName);
    return contentOf(file, Charset.forName(charsetName));
  }

  /**
   * Loads the text content of a file into a character string.
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  public static String contentOf(File file, Charset charset) {
    requireNonNull(charset, "The charset should not be null");
    try {
      return new String(java.nio.file.Files.readAllBytes(file.toPath()), charset);
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to read " + file.getAbsolutePath(), e);
    }
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charset the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(File file, Charset charset) {
    requireNonNull(charset, "The charset should not be null");
    try {
      return java.nio.file.Files.readAllLines(file.toPath(), charset);
    } catch (IOException e) {
      throw new UncheckedIOException("Unable to read " + file.getAbsolutePath(), e);
    }
  }

  /**
   * Loads the text content of a file into a list of strings, each string corresponding to a line. The line endings are
   * either \n, \r or \r\n.
   *
   * @param file the file.
   * @param charsetName the name of the character set to use.
   * @return the content of the file.
   * @throws NullPointerException if the given charset is {@code null}.
   * @throws UncheckedIOException if an I/O exception occurs.
   */
  public static List<String> linesOf(File file, String charsetName) {
    checkArgumentCharsetIsSupported(charsetName);
    return linesOf(file, Charset.forName(charsetName));
  }

  private static void checkArgumentCharsetIsSupported(String charsetName) {
    checkArgument(Charset.isSupported(charsetName), "Charset:<'%s'> is not supported on this system", charsetName);
  }

  private Files() {}

}
