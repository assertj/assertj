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

import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.NavigationListAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.test.Vehicle;
import org.assertj.core.test.VehicleFactory;
import org.assertj.core.test.VehicleFactory.Car;
import org.assertj.core.util.Descriptions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests navigating a generated Assert class with a List property
 */
public class ListNavigation_Test {
  VehicleFactory vehicleFactory = new VehicleFactory();
  List<Vehicle> expectedVehicles = vehicleFactory.getVehicles();

  // if we had a generated assertion library for VehicleFactory this code would look more like
  // NavigationListAssert<Vehicle, ObjectAssert> vehicles = assertThat(vehicleFactory).vehicles();

  NavigationListAssert<Vehicle, ObjectAssert> vehicles = assertThat(expectedVehicles, new AssertFactory<Vehicle, ObjectAssert>() {
    @Override
    public ObjectAssert createAssert(Vehicle vehicle) {
      return (ObjectAssert) assertThat(vehicle);
    }
  });

  @Before
  public void init() {
    // Note this method wlould be generated in the VehicleFactoryAssert.vehicles() method
    vehicles.describedAs(Descriptions.navigateDescription(assertThat(vehicleFactory).describedAs("VehicleFactory"), "vehicles"));
  }

  @Test
  public void should_pass_assertions_on_list() throws Exception {
    vehicles.first().isEqualTo(expectedVehicles.get(0));
    vehicles.last().isEqualTo(expectedVehicles.get(expectedVehicles.size() - 1));
    vehicles.item(2).isEqualTo(expectedVehicles.get(2));
    vehicles.contains(expectedVehicles.get(1));
    vehicles.containsOnly(expectedVehicles.get(1), expectedVehicles.get(2), expectedVehicles.get(0));
    vehicles.containsExactly(expectedVehicles.get(0), expectedVehicles.get(1), expectedVehicles.get(2));
    vehicles.doesNotContain(new Car("doesNotExist"), new Car("doesNotExist2"));
  }

  @Test
  public void failing_tests() throws Exception {
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        vehicles.item(10).isEqualTo(expectedVehicles.get(0));
      }
    }).hasMessageContaining("VehicleFactory.vehicles.index");

    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        vehicles.item(1).isEqualTo(expectedVehicles.get(2));
      }
    }).hasMessageContaining("VehicleFactory.vehicles.index");
  }

}
