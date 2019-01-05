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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.objects.data;

import java.util.Date;

public class Person {
  public Date dateOfBirth;
  public String name;
  public Home home = new Home();
  public Person neighbour;

  public Person() {}

  public Person(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Person [name=" + name + ", home=" + home + "]";
  }
}