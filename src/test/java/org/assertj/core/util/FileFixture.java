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
package org.assertj.core.util;

import static java.io.File.separator;
import static java.lang.String.format;
import static org.assertj.core.util.Strings.*;

import java.io.*;
import java.util.logging.Logger;

/**
 * Creates and deletes files in the file system.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class FileFixture {

  private static Logger logger = Logger.getLogger(FolderFixture.class.getName());

  final String name;
  final FolderFixture parent;

  private File file;

  public FileFixture(String name, FolderFixture parent) throws IOException {
    this.name = name;
    this.parent = parent;
    create();
  }

  private void create() throws IOException {
    String path = relativePath();
    file = new File(path);
    if (!file.exists()) {
      boolean fileCreated = file.createNewFile();
      if (!fileCreated) throw new AssertionError(format("Unable to create file %s", quote(path)));
      logger.info(format("Created file %s", quote(path)));
    }
    if (!file.isFile()) throw new AssertionError(format("%s should be a file", quote(path)));
    logger.info(format("The file %s exists", quote(path)));
  }

  public void delete() {
    String path = relativePath();
    boolean fileDeleted = file.delete();
    if (!fileDeleted) throw new AssertionError(String.format("Unable to delete file %s", quote(path)));
    logger.info(format("The file %s was deleted", quote(path)));
  }

  String relativePath() {
    return parent != null ? concat(parent.relativePath(), separator, name) : name;
  }

  String absolutePath() {
    return file.getAbsolutePath();
  }
}
