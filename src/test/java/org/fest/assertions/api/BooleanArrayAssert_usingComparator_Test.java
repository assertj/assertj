/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;

import static org.fest.assertions.test.BooleanArrayFactory.emptyArray;
import static org.fest.assertions.test.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;

import org.fest.assertions.internal.BooleanArrays;
import org.fest.assertions.internal.Objects;
import org.fest.assertions.test.ExpectedException;
import org.fest.assertions.util.CaseInsensitiveStringComparator;

/**
 * Tests for <code>{@link BooleanArrayAssert#usingComparator(java.util.Comparator)}</code> and
 * <code>{@link BooleanArrayAssert#usingDefaultComparator()}</code>.
 * 
 * @author Joel Costigliola
 */
public class BooleanArrayAssert_usingComparator_Test {

  @Rule
  public ExpectedException thrown = none();
  
  private BooleanArrayAssert assertions = new BooleanArrayAssert(emptyArray());

  @Test
  public void using_default_comparator_test() {
    assertions.usingDefaultComparator();
    assertSame(assertions.objects, Objects.instance());
    assertSame(assertions.arrays, BooleanArrays.instance());
  }
  
  @Test
  public void using_custom_comparator_test() {
    thrown.expect(UnsupportedOperationException.class);
    // in that, we don't care of the comparator, the point to check is that we can't use a comparator
    assertions.usingComparator(CaseInsensitiveStringComparator.instance);
  }
  
}
