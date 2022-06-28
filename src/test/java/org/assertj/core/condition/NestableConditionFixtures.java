package org.assertj.core.condition;

import static org.assertj.core.condition.NestableCondition.nestable;
import static org.assertj.core.condition.VerboseCondition.verboseCondition;

import org.assertj.core.api.Condition;

class NestableConditionFixtures {
  static Condition<Name> first(String expected) {
    return verboseCondition(
                            it -> expected.equals(it.first),
                            "first: " + expected,
                            it -> " but was " + it.first);
  }

  static Condition<Name> last(String expected) {
    return verboseCondition(
                            it -> expected.equals(it.last),
                            "last: " + expected,
                            it -> " but was " + it.last);
  }

  static Condition<Address> firstLine(String expected) {
    return verboseCondition(
                            it -> expected.equals(it.firstLine),
                            "first line: " + expected,
                            it -> " but was " + it.firstLine);
  }

  static Condition<Address> postcode(String expected) {
    return verboseCondition(
                            it -> expected.equals(it.postcode),
                            "postcode: " + expected,
                            it -> " but was " + it.postcode);
  }

  static Condition<Country> name(String expected) {
    return verboseCondition(
                            it -> expected.equals(it.name),
                            "name: " + expected,
                            it -> " but was " + it.name);
  }

  @SafeVarargs
  static Condition<Customer> address(Condition<Address>... conditions) {
    return nestable("address", it -> it.address, conditions);
  }

  @SafeVarargs
  static Condition<Customer> name(Condition<Name>... conditions) {
    return nestable("name", it -> it.name, conditions);
  }

  @SafeVarargs
  static Condition<Customer> customer(Condition<Customer>... conditions) {
    return nestable("customer", conditions);
  }

  @SafeVarargs
  static Condition<Address> country(Condition<Country>... conditions) {
    return nestable("country", it -> it.country, conditions);
  }
}

class Customer {
  final Name name;
  final Address address;

  Customer(Name name, Address address) {
    this.name = name;
    this.address = address;
  }
}

class Name {
  final String first;
  final String last;

  Name(String first, String last) {
    this.first = first;
    this.last = last;
  }
}

class Address {
  final String firstLine;
  final String postcode;
  final Country country;

  Address(String firstLine, String postcode, Country country) {
    this.firstLine = firstLine;
    this.postcode = postcode;
    this.country = country;
  }
}

class Country {
  final String name;

  Country(String name) {
    this.name = name;
  }
}
