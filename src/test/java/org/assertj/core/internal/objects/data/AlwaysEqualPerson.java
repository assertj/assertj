package org.assertj.core.internal.objects.data;

public class AlwaysEqualPerson extends Person {

  @Override
  public boolean equals(Object o) {
    return true;
  }
}