package org.assertj.core.internal.objects.data;

public class AlwaysDifferentAddress extends Address {

  @Override
  public boolean equals(Object o) {
    return false;
  }
}