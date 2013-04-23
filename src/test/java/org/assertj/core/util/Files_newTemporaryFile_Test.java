/*
 * Created on Sep 23, 2006
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2006-2011 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * Tests for <code>{@link Files#newTemporaryFolder()}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class Files_newTemporaryFile_Test extends Files_TestCase {

  @Test
  public void should_create_new_temporary_file() {
    File f = null;
    try {
      f = Files.newTemporaryFile();
      assertTrue(f.isFile());
    } finally {
      if (f != null) f.delete();
    }
  }
}
