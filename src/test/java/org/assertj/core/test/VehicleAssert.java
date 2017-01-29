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

import org.assertj.core.api.AbstractAssert;

public class VehicleAssert extends AbstractAssert<VehicleAssert, Vehicle> {

  public VehicleAssert(Vehicle actual) {
    super(actual, VehicleAssert.class);
  }

  public static VehicleAssert assertThat(Vehicle actual) {
    return new VehicleAssert(actual);
  }

  public VehicleAssert hasName(String name) {
    // check that actual Vehicle we want to make assertions on is not null.
    isNotNull();

    // check condition
    if (!actual.getName().equals(name)) {
      failWithMessage("Expected vehicle's name to be <%s> but was <%s>", name, actual.getName());
    }

    // return the current assertion for method chaining
    return this;
  }

}
