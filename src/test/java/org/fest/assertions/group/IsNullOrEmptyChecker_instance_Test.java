/*
 * Created on Oct 8, 2010
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
package org.fest.assertions.group;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

/**
 * Tests for <code>{@link IsNullOrEmptyChecker#instance()}</code>.
 *
 * @author Alex Ruiz
 */
public class IsNullOrEmptyChecker_instance_Test {

  @Test public void should_return_singleton() {
    assertSame(IsNullOrEmptyChecker.INSTANCE, IsNullOrEmptyChecker.instance());
  }

  @Test public void should_contain_delegate_IsEmptyCheckers() {
    IsNullOrEmptyChecker checker = IsNullOrEmptyChecker.instance();
    List<IsEmptyChecker> checkers = checker.checkers;
    assertEquals(2, checkers.size());
    assertEquals(ArrayIsEmptyChecker.class, checkers.get(0).getClass());
    assertEquals(CollectionIsEmptyChecker.class, checkers.get(1).getClass());
  }
}
