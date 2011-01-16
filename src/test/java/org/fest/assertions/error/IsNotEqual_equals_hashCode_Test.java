/*
 * Created on Jan 15, 2011
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

import org.junit.*;

/**
 * Tests for <code>{@link IsNotEqual#equals(Object)}</code> and <code>{@link IsNotEqual#hashCode()}</code>.
 *
 * @author Yvonne Wang
 */
public class IsNotEqual_equals_hashCode_Test {

  private static IsNotEqual message;

  @BeforeClass public static void setUpOnce() {
    message = new IsNotEqual("Yoda", "Luke");
  }

  @Test public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(message);
  }

  @Test public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(message, new IsNotEqual("Yoda", "Luke"));
  }

  @Test public void should_have_transitive_equals() {
    assertEqualsIsTransitive(message, new IsNotEqual("Yoda", "Luke"), new IsNotEqual("Yoda", "Luke"));
  }

  @Test public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(message, new IsNotEqual("Yoda", "Luke"));
  }

  @Test public void should_not_be_equal_to_Object_of_different_type() {
    assertFalse(message.equals("Yoda"));
  }

  @Test public void should_not_be_equal_to_null() {
    assertFalse(message.equals(null));
  }

  @Test public void should_not_be_equal_to_IsNotEqual_with_different_actual() {
    assertFalse(message.equals(new IsNotEqual("Leia", "Luke")));
  }

  @Test public void should_not_be_equal_to_IsNotEqual_with_different_expected() {
    assertFalse(message.equals(new IsNotEqual("Yoda", "Leia")));
  }
}
