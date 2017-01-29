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

import java.util.Arrays;
import java.util.List;

/**
 * Used for testing property access of inner classes.
 * 
 * @author Marcus Klimstra (CGI)
 */
public class VehicleFactory {

  public List<Vehicle> getVehicles() {
    Vehicle car1 = new Car("Toyota");
    Vehicle car2 = new Car("Honda");
    Vehicle car3 = new Car("Audi");
    return Arrays.asList(car1, car2, car3);
  }

  public static class Car implements Vehicle {
    private String brand;

    public Car(String brand) {
      this.brand = brand;
    }

    @Override
    public String getName() {
      return brand;
    }

    @Override
    public String toString() {
      return "Car(" + brand + ")";
    }
  }
}
