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
package org.assertj.core.internal;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import org.assertj.core.util.VisibleForTesting;


/**
 * Wrapper for <code>{@link java.nio.file.Files}</code> to test {@link Paths}.
 */
public class NioFilesWrapper {

  private static final NioFilesWrapper INSTANCE = new NioFilesWrapper();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static NioFilesWrapper instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  NioFilesWrapper() {}

  public boolean isRegularFile(Path path) {
    return Files.isRegularFile(path);
  }
  
  public boolean isSymbolicLink(Path path) {
	return Files.isSymbolicLink(path);
  }
  
  public boolean isDirectory(Path path) {
	return Files.isDirectory(path);
  }
  
  public boolean exists(Path path, LinkOption... options) {
	return Files.exists(path, options);
  }

  public boolean notExists(Path path, LinkOption... options) {
	return Files.notExists(path, options);
  }
  
  public boolean isReadable(Path path) {
	return Files.isReadable(path);
  }
  
  public boolean isWritable(Path path) {
	return Files.isWritable(path);
  }
  
  public boolean isExecutable(Path path) {
	return Files.isExecutable(path);
  }
  
}

