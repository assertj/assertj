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
package org.assertj.core.api.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;


/**
 * Test for <code>{@link PathAssert#usingCharset(String)}</code>.
 */
public class PathAssert_usingCharset_String_Test extends PathAssertBaseTest {

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.usingCharset(otherCharset.name());
  }

  @Override
  protected void verify_internal_effects() {
	assertThat(getCharset(assertions)).isEqualTo(otherCharset);
  }
}
