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
package org.assertj.core.navigation;

import static org.assertj.core.test.ExpectedException.none;

import java.util.Iterator;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Vehicle;
import org.assertj.core.test.VehicleAssert;
import org.assertj.core.test.VehicleFactory;
import org.assertj.core.test.VehicleFactory.Car;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public abstract class GenericNavigableAssert_Test<T extends Iterable<Vehicle>, ASSERT extends AbstractIterableAssert<?, T, Vehicle, VehicleAssert>> {

  @Rule
  public ExpectedException thrown = none();

  protected VehicleFactory vehicleFactory;
  protected T expectedVehicles;
  protected ASSERT vehiclesAssert;

  @SuppressWarnings("unchecked")
  @Before
  public void init() {
    vehicleFactory = new VehicleFactory();
    expectedVehicles = (T) vehicleFactory.getVehicles();
    vehiclesAssert = buildNavigableAssert();
    vehiclesAssert.as("VehicleFactory.vehicles");
  }

  protected abstract ASSERT buildNavigableAssert();

  @Test
  public void should_allow_to_assert_navigated_elements() {
    vehiclesAssert.first()
                  .hasName(getVehicle(0).getName())
                  .isEqualTo(getVehicle(0));
    vehiclesAssert.last().isEqualTo(getVehicle(2));
    vehiclesAssert.element(2).isEqualTo(getVehicle(2));
  }
  
  private Vehicle getVehicle(int index) {
    Iterator<Vehicle> actualIterator = expectedVehicles.iterator();
    for (int i = 0; i < index; i++) {
      actualIterator.next();
    }
    return actualIterator.next();
  }

  @Test
  public void should_honor_iterable_assertions() {
    vehiclesAssert.contains(getVehicle(1));
    vehiclesAssert.containsOnly(getVehicle(1), getVehicle(2), getVehicle(0));
    vehiclesAssert.containsExactly(getVehicle(0), getVehicle(1), getVehicle(2));
    vehiclesAssert.doesNotContain(new Car("doesNotExist"), new Car("doesNotExist2"));
  }

  @Test
  public void element_navigating_failing_test_index_greater_size() {
    thrown.expectAssertionErrorWithMessageContaining("VehicleFactory.vehicles check index");
    vehiclesAssert.element(10).isEqualTo(getVehicle(0));
  }

  @Test
  public void element_navigating_failing_test_actual_not_equal_to_given() {
    thrown.expectAssertionErrorWithMessageContaining("VehicleFactory.vehicles element at index 1");
    vehiclesAssert.element(1).isEqualTo(getVehicle(2));
  }

  @Test
  public void first_element_navigating_failing_test() {
    thrown.expectAssertionErrorWithMessageContaining("VehicleFactory.vehicles check first element");
    vehiclesAssert.first().isEqualTo(getVehicle(1));
  }

  @Test
  public void last_element_navigating_failing_test() {
    thrown.expectAssertionErrorWithMessageContaining("VehicleFactory.vehicles check last element");
    vehiclesAssert.last().isEqualTo(getVehicle(1));
  }
  
}