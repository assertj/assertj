/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.util.introspection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GetterIntrospectionStrategyTest {

  private final GetterIntrospectionStrategy introspectionStrategy;

  public GetterIntrospectionStrategyTest() {
    this.introspectionStrategy = GetterIntrospectionStrategy.instance();
  }

  @Test
  void getMemberNames() {
    Assertions.assertThat(introspectionStrategy.getMemberNames(Child.class))
              .containsExactlyInAnyOrder("getOrder", "getName", "isFlag", "isF", "getNonStaticS", "getSMTH");
  }

  @Test
  void getMemberNamesAsFields() {
    Assertions.assertThat(introspectionStrategy.getMemberNamesAsFields(Child.class))
              .containsExactlyInAnyOrder("order", "name", "flag", "f", "nonStaticS", "sMTH");
  }

  public static class Parent {

    protected final String name;
    protected final boolean flag;

    private Parent(String name) {
      this.name = name;
      this.flag = false;
    }

    public String getName() {
      return name;
    }

    public static String getStaticS() {
      return "StaticS";
    }

    public String getNonStaticS() {
      return "NonStaticS";
    }

    public String getSMTH() {
      return "SMTH";
    }

    public boolean isFlag() {
      return flag;
    }

    public boolean isF() {
      return isFlag();
    }

    public String someOtherMethod() {
      return null;
    }
  }

  public static class Child extends Parent {

    protected final int order;

    private Child(String name, int order) {
      super(name);
      this.order = order;
    }

    public int getOrder() {
      return order;
    }

    public static String getStaticH() {
      return "StaticH";
    }
  }

}
