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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.presentation.ThrowableRepresentation;

public class Throwables_Description_Test {

  public static class test1{
    public class test2{
      public class test3{
        public class test4{

          public void exception_layer_4() {
            throw new RuntimeException();
          }
        }
        public void exception_layer_3() {
          test4 t = new test4();
          t.exception_layer_4();
        }
      }
      public void exception_layer_2() {
        test3 t = new test3();
        t.exception_layer_3();
      }
    }
    public void exception_layer_1() {
      test2 t = new test2();
      t.exception_layer_2();
    }
  }

  public static void main(String[] args) {
    Exception e = null;
    try {
      test1 t = new test1();
      t.exception_layer_1();
    } catch (RuntimeException e2) {
      e = e2;
    }
//    e.printStackTrace();
//    System.out.println("-------------");
//    System.out.println(e.toString());
//    assertThat(e).isNull();
//    assertThat(e).as(ExceptionUtils.getStackTrace(e)).isNull();
//    AbstractAssert.setCustomRepresentation(ThrowableRepresentation.THROWABLE_REPRESENTATION);
    assertThat(e).isEqualTo(new IndexOutOfBoundsException());
  }
}
