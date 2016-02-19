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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

public class Objects_assertIsEqualToComparingFieldByFieldRecursive_Test extends ObjectsBaseTest {

  @Test
  public void should_be_able_to_compare_objects_recursivly() {
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;
    
    Person other = new Person();
    other.name = "John";
    other.home.address.number = 1;
    
    objects.assertIsEqualToComparingFieldByFieldRecursively(someInfo(), actual, other); 
  }
  
  @Test
  public void should_be_able_to_compare_objects_with_different_types_recursivly() {
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;
    
    Human other = new Human();
    other.name = "John";
    other.home.address.number = 1;
    
    objects.assertIsEqualToComparingFieldByFieldRecursively(someInfo(), actual, other); 
  }
  
  @Test
  public void should_be_able_to_compare_objects_with_cylces_recursivly() {
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;
    
    Person other = new Person();
    other.name = "John";
    other.home.address.number = 1;
    other.neighbour = actual;
    
    actual.neighbour = other;
    
    objects.assertIsEqualToComparingFieldByFieldRecursively(someInfo(), actual, other); 
  }
  
  @Test
  public void should_fail_when_fields_differ() {
    AssertionInfo info = someInfo();
      
    Person actual = new Person();
    actual.name = "John";
    
    Person other = new Person();
    other.name = "Jack";
    
//    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other); 
//    } catch (AssertionError err) {
//      verify(failures).failure(info, shouldBeEqual(actual, other, StandardRepresentation.STANDARD_REPRESENTATION));
//      return;
//    }
//    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void should_fail_when_fields_of_childobjects_differ() {
    AssertionInfo info = someInfo();
      
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;
    
    Person other = new Person();
    other.name = "John";
    other.home.address.number = 2;
    
//    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other); 
//    } catch (AssertionError err) {
//      verify(failures).failure(info, shouldBeEqual(actual, other, StandardRepresentation.STANDARD_REPRESENTATION));
//      return;
//    }
//    failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void errorMessageSingleDifference() {
      Person actual = new Person();
      actual.name = "John";
      actual.home.address.number = 1;
      
      Person other = new Person();
      other.name = "John";
      other.home.address.number = 2;
      
      assertThat(actual).isEqualToComparingFieldByFieldRecursively(other);
  }
  
  @Test
  public void errorMessageMultipleDifferences() {
      Person actual = new Person();
      actual.name = "Jack";
      actual.home.address.number = 1;
      
      Person other = new Person();
      other.name = "John";
      other.home.address.number = 2;
      
      assertThat(actual).isEqualToComparingFieldByFieldRecursively(other);
  }

  public static class Person {
    public String name;
    public Home home = new Home();
    public Person neighbour;

    public String toString() {
        return "Person [name=" + name + ", home=" + home + "]";
    }
  }

  public static class Home {
    public Address address = new Address();

    public String toString() {
        return "Home [address=" + address + "]";
    }
  }

  public static class Address {
    public int number = 1;

    public String toString() {
        return "Address [number=" + number + "]";
    }
  }

  public static class Human extends Person{}

}
