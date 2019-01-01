package org.assertj.core.internal.objects.data;

public class AlwaysDifferentPerson extends Person {

  @Override
  public boolean equals(Object o) {
    return false;
  }
}