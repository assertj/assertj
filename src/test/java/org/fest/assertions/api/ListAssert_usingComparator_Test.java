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
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.Lists;
import org.fest.assertions.internal.Objects;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Tests for <code>{@link ListAssert#usingComparator(java.util.Comparator)}</code> and
 * <code>{@link ListAssert#usingDefaultComparator()}</code>.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ListAssert_usingComparator_Test {

  private ListAssert<String> assertions = new ListAssert<String>(new ArrayList<String>());

  @Mock
  private Comparator<List<String>> comparator;

  @Before
  public void before(){
    initMocks(this);
  }

  @Test
  public void using_default_comparator_test() {
    assertions.usingDefaultComparator();
    assertSame(assertions.objects, Objects.instance());
    assertSame(assertions.iterables, Iterables.instance());
    assertSame(assertions.lists, Lists.instance());
  }

  @Test
  public void using_custom_comparator_test() {
    // in that test, the comparator type is not important, we only check that we correctly switch of comparator
    assertions.usingComparator(comparator);
    assertSame(assertions.objects.getComparator(), comparator);
//    assertSame(assertions.iterables.getComparator(), Iterables.instance());
//    assertSame(assertions.lists.getComparator(), Lists.instance());
  }
}
