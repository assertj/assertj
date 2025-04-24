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
package org.assertj.core.api;

import org.assertj.core.util.CheckReturnValue;

public class FileSizeAssert<T> extends AbstractFileSizeAssert<FileAssert> {

  private AbstractFileAssert<FileAssert> fileAssert;

  public FileSizeAssert(AbstractFileAssert<FileAssert> fileAssert) {
    super(fileAssert.actual.length(), FileSizeAssert.class);
    this.fileAssert = fileAssert;
  }

  @Override
  @CheckReturnValue
  public AbstractFileAssert<FileAssert> returnToFile() {
    return fileAssert;
  }
}
