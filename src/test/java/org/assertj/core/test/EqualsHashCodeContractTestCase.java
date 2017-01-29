/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.test;

/**
 * Test case that provides the contract for verification that an object's {@code equals} and {@code hashCode} are
 * implemented correctly.
 *
 * @author Alex Ruiz
 */
public interface EqualsHashCodeContractTestCase {

  void should_not_be_equal_to_Object_of_different_type();

  /**
   * If two objects are equal, they must remain equal as long as they are not modified.
   */
  void equals_should_be_consistent();

  /**
   * The object must be equal to itself, which it would be at any given instance; unless you intentionally override the
   * equals method to behave otherwise.
   */
  void equals_should_be_reflexive();

  /**
   * If object of one class is equal to another class object, the other class object must be equal to this class object.
   * In other words, one object can not unilaterally decide whether it is equal to another object; two objects, and
   * consequently the classes to which they belong, must bilaterally decide if they are equal or not. They BOTH must
   * agree.
   */
  void equals_should_be_symmetric();

  /**
   * If the first object is equal to the second object and the second object is equal to the third object; then the
   * first object is equal to the third object. In other words, if two objects agree that they are equal, and follow the
   * symmetry principle, one of them can not decide to have a similar contract with another object of different class.
   * All three must agree and follow symmetry principle for various permutations of these three classes.
   */
  void equals_should_be_transitive();

  /**
   * If two objects are equal, then they must have the same hash code, however the opposite is NOT true.
   */
  void should_maintain_equals_and_hashCode_contract();

  /**
   * Verifies that the implementation of the method {@code equals} returns {@code false} if a {@code null} is passed as
   * argument.
   */
  void should_not_be_equal_to_null();
}
