package org.assertj.core.api.test;

public class ComparableExample implements Comparable<ComparableExample> {

  private int id;

  public ComparableExample(int id) {
    this.id = id;
  }

  @Override
  public int compareTo(ComparableExample that) {
    return this.id - that.id;
  }
}