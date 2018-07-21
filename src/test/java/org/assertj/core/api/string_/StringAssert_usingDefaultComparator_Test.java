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
package org.assertj.core.api.string_;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.StringAssert;
import org.assertj.core.api.StringAssertBaseTest;
import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.Strings;
import org.assertj.core.util.CaseInsensitiveCharSequenceComparator;
import org.junit.jupiter.api.BeforeEach;

public class StringAssert_usingDefaultComparator_Test extends StringAssertBaseTest {

  @BeforeEach
  public void before() {
    assertions.usingComparator(CaseInsensitiveCharSequenceComparator.instance);
  }

  @Override
  protected StringAssert invoke_api_method() {
    return assertions.usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(getObjects(assertions)).isSameAs(Objects.instance());
    assertThat(getStrings(assertions)).isSameAs(Strings.instance());
    assertThat(getComparables(assertions)).isEqualTo(new Comparables());
  }
}
