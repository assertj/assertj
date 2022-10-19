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
package org.assertj.core.api.recursive.comparison;

import org.assertj.core.internal.objects.data.PersonDto;
import org.assertj.core.test.Person;

public interface PersonData {

  Person sheldon = new Person("Sheldon");
  Person leonard = new Person("Leonard");
  Person howard = new Person("Howard");
  Person raj = new Person("Rajesh");
  Person penny = new Person("Penny");

  PersonDto sheldonDto = new PersonDto("Sheldon");
  PersonDto leonardDto = new PersonDto("Leonard");
  PersonDto howardDto = new PersonDto("Howard");
  PersonDto rajDto = new PersonDto("Rajesh");
  PersonDto pennyDto = new PersonDto("Penny");

}
