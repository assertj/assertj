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

import static java.io.File.separator;
import static org.assertj.core.util.Strings.append;
import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Files#temporaryFolder()}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Files_temporaryFolder_Test extends Files_TestCase {

  @Test
  public void should_find_temporary_folder() {
    File temporaryFolder = Files.temporaryFolder();
    assertThat(temporaryFolder).isDirectory();
    String a = append(separator).to(temporaryFolder.getAbsolutePath());
    String e = append(separator).to(systemTemporaryFolder());
    assertThat(a).isEqualTo(e);
  }
}
