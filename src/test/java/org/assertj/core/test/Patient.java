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

import java.sql.Timestamp;
import java.util.Objects;

public class Patient {
  private Timestamp dateOfBirth;

  private Health health;

  public Patient(Timestamp timestamp) {
    dateOfBirth = timestamp;
    health = new Health(180, 80);
  }

  public Timestamp getDateOfBirth() {
    return dateOfBirth;
  }

  public Health getHealth() {
    return health;
  }

  public class Health {
    private int height;
    private int weight;
    private int health;

    Health(int height, int weight) {
      this.height = height;
      this.weight = weight;
      this.health = height * weight;
    }

    public int getHealth() {
      return health;
    }

    public int getHeight() {
      return height;
    }

    public int getWeight() {
      return weight;
    }

    @Override
    public String toString() {
      return Integer.toString(health);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(health);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      Health other = (Health) obj;
      if (health != other.health) return false;
      return true;
    }

  }
}