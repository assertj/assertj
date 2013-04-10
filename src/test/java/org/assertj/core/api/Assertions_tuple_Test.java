package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.assertj.core.groups.Tuple;

public class Assertions_tuple_Test {

  @Test
  public void should_create_tuple() {
    Tuple tuple = Assertions.tuple("Yoda", 800, "Jedi");
    assertThat(tuple).isEqualTo(new Tuple("Yoda", 800, "Jedi"));
  }

}
