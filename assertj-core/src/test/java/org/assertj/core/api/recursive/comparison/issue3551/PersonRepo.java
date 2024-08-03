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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Repository
public interface PersonRepo extends JpaRepository<PersonRepo.PersonEntity, String> {
  @Entity
  public static class PersonEntity {
    @Id
    private String name;
  }

  public interface Person {
    String getName();
  }

  @Query(value = "SELECT 'alice' as name")
  Person getPerson();
}
