package org.assertj.core.test;

import java.util.ArrayList;
import java.util.List;

public class CartoonCharacter {
  private final String name;
  private final List<CartoonCharacter> children = new ArrayList<CartoonCharacter>();

  public CartoonCharacter(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<CartoonCharacter> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return "CartoonCharacter [name=" + name + "]";
  }
  
  
}