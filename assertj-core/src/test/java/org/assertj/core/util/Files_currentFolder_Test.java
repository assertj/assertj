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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#currentFolder()}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
class Files_currentFolder_Test extends Files_TestCase {

  @Test
  void should_return_current_folder() throws IOException {
    File e = new File(".");
    File a = Files.currentFolder();
    assertThat(a.getCanonicalPath()).isEqualTo(e.getCanonicalPath());
  }
}
