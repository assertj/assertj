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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.util.diff.stream;

import static java.util.Arrays.asList;

import java.util.List;

class StringStreamView implements StreamView {
  private final String input;

  StringStreamView(String input) {
    this.input = input;
  }

  @Override
  public List<CharSequence> lines() {
    return asList(input.split("(\r)?\n"));
  }

  @Override
  public void close() {}
}
