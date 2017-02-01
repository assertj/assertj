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
package org.assertj.core.test;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class CartoonCharacter {
  private final String name;
  private final List<CartoonCharacter> children = new ArrayList<>();

  public CartoonCharacter(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<CartoonCharacter> getChildren() {
    return children;
  }

  public CartoonCharacter[] getChildrenArray() {
    return children.toArray(new CartoonCharacter[0]);
  }

  public void addChildren(CartoonCharacter... kids) {
    children.addAll(asList(kids));
  }

  @Override
  public String toString() {
    return "CartoonCharacter [name=" + name + "]";
  }
  
  
}