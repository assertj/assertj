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
package org.assertj.core.api.recursive.comparison.issue3551;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PersonRepoTest {
  @Autowired
  private PersonRepo personRepo;

  private class PersonImpl implements PersonRepo.Person {
    private String name;

    @Override
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public PersonImpl(String name) {
      this.name = name;
    }

    public PersonImpl() {}

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PersonImpl person = (PersonImpl) o;
      return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(name);
    }
  }

  @Test
  public void test() {
    PersonRepo.Person alice = personRepo.getPerson();
    PersonRepo.Person bob = new PersonImpl("bob");
    alice.getName();
    // Recursive comparison incorrectly asserts that alice and bob are the same
    assertThat(alice).usingRecursiveComparison().isEqualTo(bob);
  }
}
