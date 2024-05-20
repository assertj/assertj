/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.util.AssertionsUtil.getDifferentCharsetFrom;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.nio.charset.Charset;
import org.assertj.core.internal.Files;

/**
 * Base class for {@link FileAssert} tests.
 *
 * @author Olivier Michallat
 */
public abstract class FileAssertBaseTest extends BaseTestTemplate<FileAssert, File> {
  protected Files files;
  protected Charset defaultCharset;
  protected Charset otherCharset;

  @Override
  protected FileAssert create_assertions() {
    return new FileAssert(new File("abc"));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    files = mock(Files.class);
    assertions.files = files;

    defaultCharset = defaultCharset();
    otherCharset = getDifferentCharsetFrom(defaultCharset);
  }

  protected Files getFiles(FileAssert someAssertions) {
    return someAssertions.files;
  }

  protected Charset getCharset(FileAssert someAssertions) {
    return someAssertions.charset;
  }
}
