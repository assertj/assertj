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
