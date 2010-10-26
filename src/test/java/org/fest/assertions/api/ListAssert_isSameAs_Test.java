/*
 * Created on Oct 26, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertSame;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.List;

import org.fest.assertions.internal.Objects;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ListAssert#isSameAs(List)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ListAssert_isSameAs_Test {

  private Objects objects;
  private ListAssert assertions;

  @Before public void setUp() {
    objects = mock(Objects.class);
    assertions = new ListAssert(emptyList());
    assertions.objects = objects;
  }

  @Test public void should_verify_that_actual_value_is_same_as_expected_value() {
    List<String> expected = list("Luke");
    assertions.isSameAs(expected);
    verify(objects).assertSame(assertions.info, assertions.actual, expected);
  }

  @Test public void should_return_this() {
    ListAssert returned = assertions.isSameAs(emptyList());
    assertSame(assertions, returned);
  }
}
