/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

// Not run by the build (class name does not match *Test), most examples fail on purpose to show error messages.
// The fine-grained representations are not registered in META-INF/services as that would affect all tests.
class RepresentationExample {

  // tag::custom_representation[]
  // dummy class
  private class Example {}

  public class CustomRepresentation extends StandardRepresentation { // <1>

    // override fallbackToStringOf to handle Example formatting
    @Override
    public String fallbackToStringOf(Object o) { // <2>
      if (o instanceof Example) return "Example";
      // fallback to default formatting.
      return super.fallbackToStringOf(o);
    }

    // override a predefined type formatting: String
    @Override
    protected String toStringOf(String str) { // <3>
      return "$" + str + "$";
    }
  }
  // end::custom_representation[]

  @Test
  void global_representation_with_example() {
    // tag::use_representation[]
    // to call before executing tests
    Assertions.useRepresentation(new CustomRepresentation());
    // end::use_representation[]
    try {
      // tag::example_is_null[]
      assertThat(new Example()).isNull();
      // end::example_is_null[]
    } finally {
      Assertions.useDefaultRepresentation();
    }
  }

  @Test
  void global_representation_with_string() {
    Assertions.useRepresentation(new CustomRepresentation());
    try {
      // tag::string_starts_with[]
      // this one fails ...
      assertThat("foo").startsWith("bar");
      // end::string_starts_with[]
    } finally {
      Assertions.useDefaultRepresentation();
    }
  }

  @Test
  void per_assertion_representation() {
    // tag::per_assertion_representation[]
    Representation customRepresentation = new CustomRepresentation();

    // this assertion fails ...
    assertThat(new Example()).withRepresentation(customRepresentation)
                             .isNull();

    assertThat("foo").withRepresentation(customRepresentation)
                     .startsWith("bar");
    // end::per_assertion_representation[]
  }

  // tag::lotr[]
  public class Hobbit {

    public String name;
    public String age;

    @Override
    public String toString() {
      return format("Hobbit [name=%s, age=%s]", name, age);
    }
  }

  public class LotrRepresentation implements Representation {

    @Override
    public String toStringOf(Object object) {
      if (object instanceof Hobbit) {
        Hobbit hobbit = (Hobbit) object;
        return String.format("HOBBIT [name=%s, age=%s]", hobbit.name, hobbit.age);
      }
      return null;
    }

    // only needed if another library was to represent Hobbit, in this case the one with highest priority wins
    @Override
    public int getPriority() {
      return 5;
    }
  }
  // end::lotr[]

  // tag::starwars[]
  public class Jedi {

    public String name;
    public String age;

    @Override
    public String toString() {
      return format("Jedi [name=%s, age=%s]", name, age);
    }
  }

  public class StarWarsRepresentation implements Representation {

    @Override
    public String toStringOf(Object object) {
      if (object instanceof Jedi) {
        Jedi jedi = (Jedi) object;
        return String.format("JEDI [name=%s, age=%s]", jedi.name, jedi.age);
      }
      return null;
    }

    @Override
    public int getPriority() {
      return 10;
    }
  }
  // end::starwars[]

  @Test
  void fine_grained_representations() {
    // tag::fine_grained_representations[]
    Hobbit frodo = new Hobbit();
    frodo.name = "Frodo";
    frodo.age = "33";

    Jedi luke = new Jedi();
    luke.name = "Luke";
    luke.age = "23";

    assertThat(frodo).isEqualTo(luke);
    // end::fine_grained_representations[]
  }
}
// @formatter:on
