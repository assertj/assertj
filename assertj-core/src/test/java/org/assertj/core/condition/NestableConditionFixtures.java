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
package org.assertj.core.condition;

import static org.assertj.core.condition.NestableCondition.nestable;
import static org.assertj.core.condition.VerboseCondition.verboseCondition;

import org.assertj.core.api.Condition;

class NestableConditionFixtures {
  static Condition<Name> first(String expected) {
    return verboseCondition(name -> expected.equals(name.first),
                            "first: " + expected,
                            name -> " but was " + name.first);
  }

  static Condition<Name> last(String expected) {
    return verboseCondition(name -> expected.equals(name.last),
                            "last: " + expected,
                            name -> " but was " + name.last);
  }

  static Condition<Address> firstLine(String expected) {
    return verboseCondition(address -> expected.equals(address.firstLine),
                            "first line: " + expected,
                            address -> " but was " + address.firstLine);
  }

  static Condition<Address> postcode(String expected) {
    return verboseCondition(address -> expected.equals(address.postcode),
                            "postcode: " + expected,
                            address -> " but was " + address.postcode);
  }

  static Condition<Country> name(String expected) {
    return verboseCondition(country -> expected.equals(country.name),
                            "name: " + expected,
                            country -> " but was " + country.name);
  }

  @SafeVarargs
  static Condition<Customer> address(Condition<Address>... conditions) {
    return nestable("address", customer -> customer.address, conditions);
  }

  @SafeVarargs
  static Condition<Customer> name(Condition<Name>... conditions) {
    return nestable("name", it -> it.name, conditions);
  }

  @SafeVarargs
  static Condition<Customer> customer(Condition<Customer>... conditions) {
    return nestable("customer", conditions);
  }

  static Condition<ValueCustomer> value(Integer expected) {
    return verboseCondition(valueCustomer -> expected.equals(valueCustomer.value),
                            "value: " + expected,
                            valueCustomer -> " but was " + valueCustomer.value);
  }

  @SafeVarargs
  static Condition<Address> country(Condition<Country>... conditions) {
    return nestable("country", address -> address.country, conditions);
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

class ValueCustomer extends Customer {
  final Integer value;

  ValueCustomer(Name name, Address address, Integer value) {
    super(name, address);
    this.value = value;
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
