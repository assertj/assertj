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

import static java.lang.String.format;

/**
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Employee {

  // intentionally public to test retrieval of a public field that is not a property
  public long id;
  // name is both a public field and a property => will be accessed as a property by extracting code
  public Name name;
  // surname is only a public field
  public Name surname;
  // keep private to test we are able to read property that is not a public field
  private int age;
  // keep private to test we are able to read private field without property
  @SuppressWarnings("unused")
  private String city = "New York";

  public Employee() {}

  public Employee(long id, Name name, int age) {
    this.id = id;
    setName(name);
    setAge(age);
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }
  
  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }
  
  // pure property not backed by a field
  public boolean isAdult() {
    return age > 18;
  }
  
  // testing nested combinations of field/property
  public Employee field;
  public Employee getMe(){
    return this;
  }

  @Override
  public String toString() {
    return format("%s[id=%d, name=%s, age=%d]", getClass().getSimpleName(), id, name, age);
  }
}
