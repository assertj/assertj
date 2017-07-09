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

import java.util.List;

import org.assertj.core.api.ClassBasedNavigableListAssert;
import org.assertj.core.test.IllegalVehicleAssert;
import org.assertj.core.test.Vehicle;
import org.assertj.core.test.VehicleAssert;
import org.assertj.core.test.VehicleFactory;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassBasedNavigableList_Test extends BaseNavigableListAssert_Test {

  @Override
  protected ClassBasedNavigableListAssert<?, List<Vehicle>, Vehicle, VehicleAssert> buildNavigableAssert() {
    return assertThat(expectedVehicles, VehicleAssert.class);
  }

  @Test
  public void do_not_swallow_reflection_problem() {
    thrown.expectWithMessageContaining(RuntimeException.class, "not access a member of class org.assertj.core.test.IllegalVehicleAssert");
    assertThat(expectedVehicles, IllegalVehicleAssert.class)
      .toAssert(new VehicleFactory.Car("car"), "unused");
  }

}
