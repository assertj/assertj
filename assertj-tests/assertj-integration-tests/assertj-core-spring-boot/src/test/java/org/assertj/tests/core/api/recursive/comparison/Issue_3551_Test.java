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

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@DisplayName("https://github.com/assertj/assertj/issues/3551")
@DataJpaTest
class Issue_3551_Test {

  @Autowired
  private PersonRepo personRepo;

  @Test
  void test() {
    Person alice = personRepo.getPerson();
    Person bob = new PersonImpl("bob");
    alice.getName();
    // FIXME Recursive comparison incorrectly asserts that alice and bob are the same
    assertThat(alice).usingRecursiveComparison().isEqualTo(bob);
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
  interface PersonRepo extends JpaRepository<PersonEntity, String> {

    @Query(value = "SELECT 'alice' as name")
    Person getPerson();

  }

  @SpringBootApplication
  @EnableJpaRepositories(considerNestedRepositories = true)
  static class TestConfiguration {
  }

}
