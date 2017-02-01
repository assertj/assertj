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

import static org.assertj.core.api.Assertions.atIndex;

import java.util.List;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.test.Vehicle;
import org.assertj.core.test.VehicleAssert;
import org.junit.Test;

public abstract class BaseNavigableListAssert_Test
    extends GenericNavigableAssert_Test<List<Vehicle>, AbstractListAssert<?, List<Vehicle>, Vehicle, VehicleAssert>> {

  @Test
  public void should_honor_list_assertions() {
    vehiclesAssert.contains(expectedVehicles.get(1), atIndex(1));
  }

}