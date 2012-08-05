/*
 * Created on Nov 29, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.string;

import static junit.framework.Assert.assertSame;

import org.fest.assertions.api.StringAssert;
import org.fest.assertions.api.StringAssertBaseTest;
import org.fest.assertions.internal.Objects;
import org.fest.assertions.internal.Strings;
import org.fest.assertions.util.CaseInsensitiveStringComparator;
import org.junit.Before;

/**
 * Tests for <code>{@link StringAssert#usingDefaultComparator()}</code>.
 * 
 * @author Joel Costigliola
 */
public class StringAssert_usingDefaultComparator_Test extends StringAssertBaseTest {

  @Before
  public void before() {
    assertions.usingComparator(CaseInsensitiveStringComparator.instance);
  }

  @Override
  protected StringAssert invoke_api_method() {
    return assertions.usingDefaultComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertSame(getObjects(assertions), Objects.instance());
    assertSame(getStrings(assertions), Strings.instance());
  }
}
