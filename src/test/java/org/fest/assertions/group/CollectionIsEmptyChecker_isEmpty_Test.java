/*
 * Created on Oct 7, 2010
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

import static java.util.Collections.emptyList;
import static org.fest.util.Collections.list;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link CollectionIsEmptyChecker#isEmpty(Object)}</code>
 *
 * @author Alex Ruiz
 */
public class CollectionIsEmptyChecker_isEmpty_Test {

  private static CollectionIsEmptyChecker checker;

  @BeforeClass public static void setUpOnce() {
    checker = CollectionIsEmptyChecker.instance();
  }

  @Test public void should_return_true_if_array_is_empty() {
    assertTrue(checker.isEmpty(emptyList()));
  }

  @Test public void should_return_false_if_array_is_not_empty() {
    assertFalse(checker.isEmpty(list("Yoda")));
  }
}
