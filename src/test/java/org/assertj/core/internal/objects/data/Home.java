package org.assertj.core.internal.objects.data;

public class Home {
  public Address address = new Address();

  @Override
  public String toString() {
    return "Home [address=" + address + "]";
  }
}