/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// https://github.com/assertj/assertj/issues/3551
@DisplayName("gh-3551")
@DataJpaTest
class Issue_3551_Test {

  @Autowired
  private PersonRepository repository;

  @Test
  void should_fail_when_comparing_proxied_value_with_non_proxied_value_if_non_proxied_value_extractor_is_registered() {
    // GIVEN
    Person alice = repository.getPerson();
    Person bob = new PersonImpl("bob");
    alice.getName();
    // WHEN
    var iae = catchIllegalArgumentException(() -> assertThat(alice).usingRecursiveComparison().isEqualTo(bob));
    // THEN
    then(iae).hasMessageContaining("can't compare a proxied value with a non proxied one");
  }

  interface Person {
    String getName();
  }

  static class PersonImpl implements Person {

    private final String name;

    PersonImpl(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

  }

  @Entity
  static class PersonEntity {

    @SuppressWarnings("unused")
    @Id
    private String name;

  }

  @Repository
  interface PersonRepository extends JpaRepository<PersonEntity, String> {

    @Query(value = "SELECT 'alice' as name")
    Person getPerson();

  }

  @SpringBootApplication
  @EnableJpaRepositories(considerNestedRepositories = true)
  static class TestConfiguration {
  }

}
