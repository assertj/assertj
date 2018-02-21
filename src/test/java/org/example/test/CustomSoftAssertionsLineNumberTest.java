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
package org.example.test;

import static org.assertj.core.api.Assertions.fail;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

/**
 * The assertions classes have to be in a package other than org.assertj because it tests explicitely the behaviour of line numbers for assertions defined outsode assertj package 
 */
public class CustomSoftAssertionsLineNumberTest {

  public static class MyProjectClass {

    private final Object value;

    public MyProjectClass(Object value) {
      this.value = value;

    }

    public Object getValue() {
      return value;
    }
  }

  public static class MyProjectClassAssert extends AbstractAssert<MyProjectClassAssert, MyProjectClass> {

    public MyProjectClassAssert(MyProjectClass actual) {
      super(actual, MyProjectClassAssert.class);
    }

    public MyProjectClassAssert hasValue(Object value) {
      if (!Objects.equals(actual.getValue(), value)) {
        failWithMessage("Expecting value to be <%s> but was <%s>:", value, actual.getValue());
      }
      return this;
    }

  }

  public static class MyProjectSoftAssertions extends SoftAssertions {

    public MyProjectClassAssert assertThat(MyProjectClass actual) {
      return proxy(MyProjectClassAssert.class, MyProjectClass.class, actual);
    }
  }

  @Test
  public void should_print_line_numbers_of_failed_assertions_even_if_custom_assertion_in_non_assertj_package() {
    MyProjectSoftAssertions softly = new MyProjectSoftAssertions();
//    try {
      softly.assertThat(new MyProjectClass("v1")).hasValue("v2");
      softly.assertAll();
      fail("Should not reach here");
//    } catch (SoftAssertionError e) {
//      assertThat(e.getMessage()).contains(format("1) Expecting value to be <v2> but was <v1>:\n" + 
//          "at CustomSoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions_even_if_custom_assertion_in_non_assertj_package(CustomSoftAssertionsLineNumberTest.java:73)"));
//    }
  }

}
