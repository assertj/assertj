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