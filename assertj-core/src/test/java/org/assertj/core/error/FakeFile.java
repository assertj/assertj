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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import java.io.File;

/**
 * @author Yvonne Wang
 */
@SuppressWarnings("serial")
class FakeFile extends File {
  private final String absolutePath;
  private boolean noParent;
  @SuppressWarnings("unused")
  private String parent;

  FakeFile(String absolutePath) {
    super(absolutePath);
    this.absolutePath = absolutePath;
  }

  FakeFile(String absolutePath, boolean noParent) {
    super(absolutePath);
    this.absolutePath = absolutePath;
    this.noParent = noParent;
  }

  FakeFile(String absolutePath, String parent) {
    super(absolutePath);
    this.absolutePath = absolutePath;
    this.parent = parent;
  }

  @Override
  public String getAbsolutePath() {
    // ToStringOf uses absolute path instead of toString
    return absolutePath;
  }

  @Override
  public String getParent() {
    return noParent ? null : super.getParent();
  }
}