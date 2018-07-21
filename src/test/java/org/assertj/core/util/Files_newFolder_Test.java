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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#newFolder(String)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Files_newFolder_Test extends Files_TestCase {

  @Test
  public void should_create_new_folder() {
    File f = null;
    try {
      f = Files.newFolder("folder");
      assertThat(f).isDirectory();
    } finally {
      if (f != null) f.delete();
    }
  }
}
