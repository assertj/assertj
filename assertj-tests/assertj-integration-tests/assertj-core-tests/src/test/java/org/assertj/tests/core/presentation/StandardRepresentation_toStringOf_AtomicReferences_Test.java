/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.presentation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import org.junit.jupiter.api.Test;

@SuppressWarnings({ "unused" })
class StandardRepresentation_toStringOf_AtomicReferences_Test {

  @Test
  void should_use_assertj_representation_for_AtomicReference() {
    // GIVEN
    Object myData = new AtomicReference<>("value");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("AtomicReference[\"value\"]");
  }

  @Test
  void should_use_overridden_toString_in_AtomicReference_subclass() {
    class MyData extends AtomicReference<String> {
      private final String description;

      MyData(String description) {
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    // GIVEN
    Object myData = new MyData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[Description]");
  }

  @Test
  void should_use_the_last_overridden_toString_in_AtomicReference_subclasses() {
    class MyData extends AtomicReference<String> {
      protected final String description;

      MyData(String description) {
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    class MySubData extends MyData {

      MySubData(String description) {
        super(description);
      }

      @Override
      public String toString() {
        return "SubMyData[" + description + "]";
      }
    }
    // GIVEN
    Object myData = new MySubData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("SubMyData[Description]");
  }

  @Test
  void should_use_overridden_toString_in_AtomicReference_intermediate_subclass() {
    class MyData extends AtomicReference<String> {
      protected final String description;

      MyData(String description) {
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    class MySubData extends MyData {

      MySubData(String description) {
        super(description);
      }

      // no toString => use MyData.toString
    }
    // GIVEN
    Object myData = new MySubData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[Description]");
  }

  @Test
  void should_use_assertj_AtomicReference_representation_as_toString_was_not_overridden_in_AtomicReference_subclass() {
    class MyData extends AtomicReference<String> {

      MyData(String value) {
        super(value);
      }

      // no overridden toString => use assertj representation
    }
    // GIVEN
    Object myData = new MyData("value");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[\"value\"]");
  }

  @Test
  void should_use_assertj_representation_for_AtomicMarkableReference() {
    // GIVEN
    Object myData = new AtomicMarkableReference<>("value", true);
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("AtomicMarkableReference[marked=true, reference=\"value\"]");
  }

  @Test
  void should_use_overridden_toString_in_AtomicMarkableReference_subclass() {
    class MyData extends AtomicMarkableReference<String> {
      private final String description;

      MyData(String description) {
        super(description, true);
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    // GIVEN
    Object myData = new MyData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[Description]");
  }

  @Test
  void should_use_assertj_AtomicMarkableReference_representation_as_toString_was_not_overridden_in_AtomicMarkableReference_subclass() {
    class MyData extends AtomicMarkableReference<String> {

      MyData(String description) {
        super(description, true);
      }

      // has no overridden toString, use the predefined one which gives "%s[marked=%s, reference=%s]"
    }
    // GIVEN
    Object myData = new MyData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[marked=true, reference=\"Description\"]");
  }

  @Test
  void should_use_assertj_representation_for_AtomicStampedReference() {
    // GIVEN
    Object myData = new AtomicStampedReference<>("value", 1);
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("AtomicStampedReference[stamp=1, reference=\"value\"]");
  }

  @Test
  void should_use_overridden_toString_AtomicStampedReference() {
    class MyData extends AtomicStampedReference<String> {
      private final String description;

      MyData(String description) {
        super(description, 0);
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    // GIVEN
    Object myData = new MyData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[Description]");
  }

  @Test
  void should_use_predefined_toString_AtomicStampedReference() {
    class MyData extends AtomicStampedReference<String> {

      MyData(String description) {
        super(description, 0);
      }

      // has no overridden toString, use the predefined one which gives "%s[stamp=%s, reference=%s]"
    }
    // GIVEN
    Object myData = new MyData("Description");
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myData);
    // THEN
    then(representation).isEqualTo("MyData[stamp=0, reference=\"Description\"]");
  }

  @Test
  void should_use_smartFormat() {
    class MyIterable implements Iterable<String> {
      final ArrayList<String> arrayList;

      public MyIterable(String[] strings) {
        arrayList = new ArrayList<>(Arrays.asList(strings));
      }

      @Override
      public Iterator<String> iterator() {
        return arrayList.iterator();
      }

      // has no overridden toString
    }
    // GIVEN
    String[] strings = { "A", "B", "C" };
    MyIterable myIterable = new MyIterable(strings);
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myIterable);
    // THEN
    then(representation).isEqualTo("[\"A\", \"B\", \"C\"]");
  }

  @Test
  void should_use_overridden_toString() {
    class MyIterable implements Iterable<String> {
      final List<String> arrayList;

      public MyIterable(String[] strings) {
        arrayList = list(strings);
      }

      @Override
      public Iterator<String> iterator() {
        return arrayList.iterator();
      }

      @Override
      public String toString() {
        return "MyIterable: " + arrayList;
      }
    }
    // GIVEN
    String[] strings = { "A", "B", "C" };
    MyIterable myIterable = new MyIterable(strings);
    // WHEN
    String representation = STANDARD_REPRESENTATION.toStringOf(myIterable);
    // THEN
    then(representation).isEqualTo("MyIterable: [A, B, C]");
  }
}
