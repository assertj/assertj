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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.objects.data;

import java.util.*;

public class PersonWithAnnotation {
  @DateAnnotation
  public Date dateOfBirth;
  @NameAnnotation
  public String name;
  public Optional<String> phone;
  public OptionalInt age;
  public OptionalLong id;
  public OptionalDouble weight;
  @AddressAnnotation
  public HomeWithAnnotation home = new HomeWithAnnotation();
  public PersonWithAnnotation neighbour;

  public PersonWithAnnotation() {}

  public PersonWithAnnotation(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return String.format("Person [dateOfBirth=%s, name=%s, phone=%s, home=%s]", dateOfBirth, name, phone, home);
  }

}

