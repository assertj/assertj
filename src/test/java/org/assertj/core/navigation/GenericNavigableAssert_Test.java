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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.navigation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Iterator;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.test.Vehicle;
import org.assertj.core.test.VehicleAssert;
import org.assertj.core.test.VehicleFactory;
import org.assertj.core.test.VehicleFactory.Car;
import org.junit.Before;
import org.junit.Test;

public abstract class GenericNavigableAssert_Test<T extends Iterable<Vehicle>, ASSERT extends AbstractIterableAssert<?, T, Vehicle, VehicleAssert>> {

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
  public void element_navigating_failing_tests() {
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        vehiclesAssert.element(10).isEqualTo(getVehicle(0));
      }
    }).hasMessageContaining("VehicleFactory.vehicles check index");

    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        vehiclesAssert.element(1).isEqualTo(getVehicle(2));
      }
    }).hasMessageContaining("VehicleFactory.vehicles element at index 1");
  }

  @Test
  public void first_element_navigating_failing_test() {
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        vehiclesAssert.first().isEqualTo(getVehicle(1));
      }
    }).hasMessageContaining("VehicleFactory.vehicles check first element");
  }
  
  @Test
  public void last_element_navigating_failing_test() {
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        vehiclesAssert.last().isEqualTo(getVehicle(1));
      }
    }).hasMessageContaining("VehicleFactory.vehicles check last element");
  }
  
}