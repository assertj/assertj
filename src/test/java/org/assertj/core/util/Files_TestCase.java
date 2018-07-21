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
package org.assertj.core.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base test case for <code>{@link Files}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public abstract class Files_TestCase {

  FolderFixture root;

  @BeforeEach
  public final void setUp() throws Exception {
    root = new FolderFixture("root");
    root.addFolder("dir_1").addFiles("file_1_1", "file_1_2").addFolder("dir_1_1").addFiles("file_1_1_1");
    root.addFolder("dir_2").addFiles("file_2_1", "file_2_2", "file_2_3");
  }

  @AfterEach
  public final void tearDown() {
    root.delete();
  }

  static String systemTemporaryFolder() {
    return System.getProperty("java.io.tmpdir");
  }
}
