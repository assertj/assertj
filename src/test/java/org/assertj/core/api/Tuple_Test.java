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
package org.assertj.core.api;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.Test;

public class Tuple_Test {

  @Test
  public void should_create_tuple() {
    Tuple tuple = new Tuple("Yoda", 800, "Jedi");
    assertThat(tuple).isEqualTo(new Tuple("Yoda", 800, "Jedi"));
  }

  @Test
  public void tuple_equal_should_support_primitive_array() {
    Tuple tuple = new Tuple("1".getBytes(), "Name");
    assertThat(tuple).isEqualTo(new Tuple("1".getBytes(), "Name"));
  }

  @Test
  public void should_create_empty_tuple() {
    Tuple tuple = new Tuple();
    assertThat(tuple).isEqualTo(new Tuple());
  }

  @Test
  public void convert_tuple_to_an_array() {
    Tuple tuple = new Tuple("Yoda", 800, "Jedi");
    assertThat(tuple.toArray()).isEqualTo(array("Yoda", 800, "Jedi"));
  }

  @Test
  public void convert_tuple_to_a_list() {
    Tuple tuple = new Tuple("Yoda", 800, "Jedi");
    assertThat(tuple.toList()).isEqualTo(newArrayList("Yoda", 800, "Jedi"));
  }

  @Test
  public void tuple_representation() {
    Tuple tuple = new Tuple("Yoda", 800, "Jedi");
    assertThat(tuple).hasToString("(\"Yoda\", 800, \"Jedi\")");
  }

  @Test
  public void test_for_issue_448() {
    SinteticClass item1 = new SinteticClass("1".getBytes(), "Foo");
    SinteticClass item2 = new SinteticClass("2".getBytes(), "Bar");
    SinteticClass item3 = new SinteticClass("3".getBytes(), "Baz");
    List<SinteticClass> list = asList(item1, item2, item3);

    assertThat(list).extracting("pk", "name")
                    .contains(tuple("1".getBytes(), "Foo"),
                              tuple("2".getBytes(), "Bar"),
                              tuple("3".getBytes(), "Baz"));

    assertThat(list).extracting("pk", "name")
                    .contains(tuple("1".getBytes(), "Foo"))
                    .contains(tuple("2".getBytes(), "Bar"))
                    .contains(tuple("3".getBytes(), "Baz"));
  }
}

final class SinteticClass {

  private byte[] pk;
  private String name;

  public SinteticClass(byte[] pk, String name) {
    this.pk = pk;
    this.name = name;
  }

  public byte[] getPk() {
    return pk;
  }

  public String getName() {
    return name;
  }
}
