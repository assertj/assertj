/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison.issue3533;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBoootTestApplication.class)
class RecursiveComparisonAssert_isEqualTo_comparingOnlyFieldsWithJpa_Test {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private CityRepository cityRepository;

  private Person person;

  @BeforeEach
  public void setUp() {
    // Create and save a City
    City city = new City();
    city.setId(1L);
    city.setName("Paris");
    city.setPostalCode("75000");
    city = cityRepository.save(city);

    // Create and save a User
    person = new Person();
    person.setId(1L);
    person.setFirstName("John");
    person.setLastName("Doe");
    person.setBirthCities(set(city));
    personRepository.save(person);
  }

  @Test
  @Disabled("reproducing a failing test that is actually normal")
  void testSaveAndRetrieveUser() {
    Person retrievedUser = personRepository.findById(1L).orElse(null);

    // Verify the fields of the retrieved user
    assertThat(retrievedUser).isNotNull()
                             .usingRecursiveComparison()
                             .comparingOnlyFields("firstName", "lastName")
                             .isEqualTo(person);
  }

}
