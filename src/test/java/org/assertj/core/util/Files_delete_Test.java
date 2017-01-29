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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for <code>{@link Files#delete(File)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Files_delete_Test extends Files_TestCase {

  @Test
  public void should_delete_folder() throws IOException {
    FolderFixture dir3 = new FolderFixture("dir_3");
    dir3.addFiles("file_3_1").addFiles("file_3_2").addFiles("file_3_2");
    dir3.addFolder("dir_3_1").addFiles("file_3_1_1").addFiles("file_3_1_2");
    File d = dir3.dir();
    String path = d.getCanonicalPath();
    Files.delete(d);
    assertThat(new File(path).exists()).isFalse();
  }
}
