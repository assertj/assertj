/*
 * Created on Jan 15, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertFalse;
import static org.fest.test.EqualsHashCodeContractAssert.*;

import org.junit.*;

/**
 * Tests for <code>{@link BasicErrorMessageFactory#equals(Object)}</code> and
 * <code>{@link BasicErrorMessageFactory#hashCode()}</code>.
 * 
 * @author Yvonne Wang
 */
public class BasicErrorMessageFactory_equals_hashCode_Test {

  private static BasicErrorMessageFactory factory;

  @BeforeClass
  public static void setUpOnce() {
    factory = new BasicErrorMessageFactory("Hello %s", "Yoda");
  }

  @Test
  public void should_have_reflexive_equals() {
    assertEqualsIsReflexive(factory);
  }

  @Test
  public void should_have_symmetric_equals() {
    assertEqualsIsSymmetric(factory, new BasicErrorMessageFactory("Hello %s", "Yoda"));
  }

  @Test
  public void should_have_transitive_equals() {
    BasicErrorMessageFactory obj2 = new BasicErrorMessageFactory("Hello %s", "Yoda");
    BasicErrorMessageFactory obj3 = new BasicErrorMessageFactory("Hello %s", "Yoda");
    assertEqualsIsTransitive(factory, obj2, obj3);
  }

  @Test
  public void should_maintain_equals_and_hashCode_contract() {
    assertMaintainsEqualsAndHashCodeContract(factory, new BasicErrorMessageFactory("Hello %s", "Yoda"));
  }

  @Test
  public void should_not_be_equal_to_Object_of_different_type() {
    assertFalse(factory.equals("Yoda"));
  }

  @Test
  public void should_not_be_equal_to_null() {
    assertFalse(factory.equals(null));
  }

  @Test
  public void should_not_be_equal_to_BasicErrorMessage_with_different_format() {
    assertFalse(factory.equals(new BasicErrorMessageFactory("How are you, %s?", "Yoda")));
  }

  @Test
  public void should_not_be_equal_to_BasicErrorMessage_with_different_arguments() {
    assertFalse(factory.equals(new BasicErrorMessageFactory("Hello %s", "Luke")));
  }
}
