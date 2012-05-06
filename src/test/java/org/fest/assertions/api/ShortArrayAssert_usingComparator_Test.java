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
import static org.fest.assertions.test.ShortArrayFactory.emptyArray;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Comparator;

import org.fest.assertions.internal.Objects;
import org.fest.assertions.internal.ShortArrays;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Tests for <code>{@link ShortArrayAssert#usingComparator(java.util.Comparator)}</code> and
 * <code>{@link ShortArrayAssert#usingDefaultComparator()}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShortArrayAssert_usingComparator_Test {

  private ShortArrayAssert assertions;

  @Mock
  private Comparator<Short> elementComparator;
  @Mock
  private Comparator<short[]> comparator;

  @Before
  public void before(){
    initMocks(this);
    assertions = new ShortArrayAssert(emptyArray());
  }

  @Test
  public void using_default_comparator_test() {
    assertions.usingDefaultComparator();
    assertSame(assertions.objects, Objects.instance());
    assertSame(assertions.arrays, ShortArrays.instance());
  }
  
  @Test
  public void using_custom_comparator_test() {
    // in that test, the comparator type is not important, we only check that we correctly switch of comparator
    assertions.usingComparator(comparator);
    assertSame(assertions.objects.getComparator(), comparator);
    assertSame(assertions.arrays, ShortArrays.instance());
  }

  @Test
  public void using_custom_element_comparator_test() {
    // in that, we don't care of the comparator, the point to check is that we can't use a comparator
    assertions.usingElementComparator(elementComparator);
    assertSame(assertions.objects, Objects.instance());
    assertSame(assertions.arrays.getComparator(), elementComparator);
  }
}
