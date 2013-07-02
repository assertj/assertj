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

  static class Car implements Vehicle {
    private String brand;

    public Car(String brand) {
      this.brand = brand;
    }

    @Override
    public String getName() {
      return brand;
    }
  }
}