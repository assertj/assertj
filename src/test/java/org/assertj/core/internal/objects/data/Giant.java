package org.assertj.core.internal.objects.data;

public class Giant extends Person {
  public double height = 3.0;

  @Override
  public String toString() {
    return "Giant [name=" + name + ", home=" + home + ", " + "height " + height + "]";
  }
}