package org.assertj.core.api;

import static org.assertj.core.api.Assertions.expect;

import org.junit.jupiter.api.Test;

class ExpectationTest {

  @Test
  void toChange_string_from_to() {
    Person person = new Person("maria", 30);
    expect(() -> person.setName("pedro")).toChange(person, Person::getName)
                                         .from("maria")
                                         .to("pedro");
  }

  @Test
  void toChange_int_from_to() {
    Person person = new Person("maria", 30);
    expect(() -> person.setAge(25)).toChange(person, Person::getAge)
                                   .from(30)
                                   .to(25);
  }

  @Test
  void toChange_int_by() {
    Person person = new Person("maria", 30);
    expect(() -> person.setAge(25)).toChange(person, Person::getAge)
                                   .by(5);
  }

  @Test
  void notToChange() {
    Person person = new Person("maria", 30);
    expect(() -> person.setName("pedro")).notToChange(person, Person::getAge);
  }

  static class Person {

    private String name;
    private int age;

    Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    String getName() {
      return name;
    }

    void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }

}
