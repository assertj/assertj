/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("https://github.com/assertj/assertj/issues/3533")
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
class Issue_3533_Test {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private CityRepository cityRepository;

  @Test
  void test() {
    // Create and save a City
    City city = new City();
    city.setId(1L);
    city.setName("Paris");
    city.setPostalCode("75000");
    city = cityRepository.save(city);

    // Create and save a User
    Person person = new Person();
    person.setId(1L);
    person.setFirstName("John");
    person.setLastName("Doe");
    person.setBirthCities(Set.of(city));
    personRepository.save(person);

    Person retrievedUser = personRepository.findById(1L).orElse(null);

    // Verify the fields of the retrieved user
    assertThat(retrievedUser).isNotNull()
                             .usingRecursiveComparison()
                             .comparingOnlyFields("firstName", "lastName")
                             .isEqualTo(person);
  }

  @Entity
  static class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String postalCode;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPostalCode() {
      return postalCode;
    }

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }

    public City() {}
  }

  interface CityRepository extends JpaRepository<City, Long> {
  }

  @Entity
  static class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<City> birthCities;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    public void setBirthCities(Set<City> birthCities) {
      this.birthCities = birthCities;
    }

    public Set<City> getBirthCities() {
      return birthCities;
    }

    public Person() {}

  }

  interface PersonRepository extends JpaRepository<Person, Long> {
  }

  @SpringBootApplication
  @EnableJpaRepositories(considerNestedRepositories = true)
  static class TestConfiguration {
  }

}
