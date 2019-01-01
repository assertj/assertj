package org.assertj.core.internal.objects.data;

public class AlwaysEqualAddress extends Address {

  @Override
  public boolean equals(Object o) {
    return true;
  }
}