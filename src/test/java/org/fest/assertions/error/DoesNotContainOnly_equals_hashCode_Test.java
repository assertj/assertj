/*
 * Created on Jan 16, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertFalse;
import static org.fest.test.EqualsHashCodeContractAssert.*;
import static org.fest.util.Collections.set;

import org.junit.*;

/**
 * Tests for <code>{@link DoesNotContainOnly#equals(Object)}</code> and
 * <code>{@link DoesNotContainOnly#hashCode()}</code>.
 *
 * @author Yvonne Wang
 */
public class DoesNotContainOnly_equals_hashCode_Test {

  private static DoesNotContainOnly errorMessage;

  @BeforeClass public static void setUpOnce() {
    errorMessage = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Leia"), set("Luke"));
  }

  @Test public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(errorMessage);
  }

  @Test public void should_have_symmetric_equals() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Leia"), set("Luke"));
    assertEqualsIsSymmetric(errorMessage, obj2);
  }

  @Test public void should_have_transitive_equals() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Leia"), set("Luke"));
    DoesNotContainOnly obj3 = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Leia"), set("Luke"));
    assertEqualsIsTransitive(errorMessage, obj2, obj3);
  }

  @Test public void should_maintain_equals_and_hashCode_contract() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Leia"), set("Luke"));
    assertMaintainsEqualsAndHashCodeContract(errorMessage, obj2);
  }

  @Test public void should_not_be_equal_to_Object_of_different_type() {
    assertFalse(errorMessage.equals("Yoda"));
  }

  @Test public void should_not_be_equal_to_null() {
    assertFalse(errorMessage.equals(null));
  }

  @Test public void should_not_be_equal_to_DoesNotContainOnly_with_different_actual() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Han", "Yoda, Luke", set("Leia"), set("Luke"));
    assertFalse(errorMessage.equals(obj2));
  }

  @Test public void should_not_be_equal_to_DoesNotContainOnly_with_different_expected() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Yoda, Leia", "Han", set("Leia"), set("Luke"));
    assertFalse(errorMessage.equals(obj2));
  }

  @Test public void should_not_be_equal_to_DoesNotContainOnly_with_different_notExpected() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Han"), set("Luke"));
    assertFalse(errorMessage.equals(obj2));
  }

  @Test public void should_not_be_equal_to_DoesNotContainOnly_with_different_notFound() {
    DoesNotContainOnly obj2 = new DoesNotContainOnly("Yoda, Leia", "Yoda, Luke", set("Leia"), set("Han"));
    assertFalse(errorMessage.equals(obj2));
  }
}
