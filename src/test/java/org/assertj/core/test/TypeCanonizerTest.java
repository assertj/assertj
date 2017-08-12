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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.AssertDelegateTarget;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Test;

/**
 * @author Filip Hrisafov
 * @author Clement Mathieu
 */
@SuppressWarnings("unused")
public class TypeCanonizerTest {
  private interface Asssert<T> {
  }

  @SuppressWarnings("unused")
  private static class Api {

    public static <T> Asssert<T> m(List<? extends T> in) {
      return null;
    }

    public static <T> Asssert<T> mSame(List<? extends T> in) {
      return null;
    }

    public static <T> Asssert<? extends T> mExtends(List<? extends T> in) {
      return null;
    }

    public static <ELEMENT> Asssert<? extends ELEMENT> mExtendsElement(List<? extends ELEMENT> in) {
      return null;
    }

    public static <T> Asssert<? super T> mSuper(List<? extends T> in) {
      return null;
    }

    public static <ELEMENT> Asssert<? super ELEMENT> mSuperElement(List<? extends ELEMENT> in) {
      return null;
    }

    public static <T> AbstractListAssert<?, List<? extends T>, T, ObjectAssert<T>> complex1(List<? extends T> in) {
      return null;
    }

    public static <T> AbstractListAssert<?, List<? extends T>, T, ObjectAssert<T>> complex2(List<? extends T> in) {
      return null;
    }

    public static <T extends AssertDelegateTarget> T returnsT(T assertion) {
      return null;
    }

    public static <T extends AssertDelegateTarget> T returnsT2(T assertion) {
      return null;
    }

    public static <K, V> MapAssert<K, V> doubleTypeVariables(Map<K, V> actual) {
      return null;
    }

    public static <K, V> MapAssert<K, V> doubleTypeVariables2(Map<K, V> actual) {
      return null;
    }

    public static <ELEMENT> ListAssert<ELEMENT> listAssert(List<? extends ELEMENT> actual) {
      return null;
    }

    public static <T> ListAssert<T> listAssert2(List<? extends T> actual) {
      return null;
    }

    public static <T> T[] genericArray(T[] actual) {
      return null;
    }

    public static <ELEMENT> ELEMENT[] genericArray2(ELEMENT[] actual) {
      return null;
    }

    public static <T> T[][] doubleGenericArray(T[] actual) {
      return null;
    }

    public static <ELEMENT> ELEMENT[][] doubleGenericArray2(ELEMENT[] actual) {
      return null;
    }
  }

  @Test
  public void T_and_T_are_equals() {
    Type m = resolveGenericReturnType(Api.class, "m");
    Type mSame = resolveGenericReturnType(Api.class, "mSame");

    assertThat(m).isEqualTo(mSame);
  }

  @Test
  public void T_and_QUESTION_MARK_extends_T_are_not_equals() {
    assertThat(resolveGenericReturnType(Api.class, "m"))
      .isNotEqualTo(resolveGenericReturnType(Api.class, "mExtends"));
  }

  @Test
  public void QUESTION_MARK_extends_T_and_QUESTION_MARK_extends_ELEMENT_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "mExtends"))
      .isEqualTo(resolveGenericReturnType(Api.class, "mExtendsElement"));
  }

  @Test
  public void QUESTION_MARK_super_T_and_QUESTION_MARK_super_ELEMENT_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "mSuper"))
      .isEqualTo(resolveGenericReturnType(Api.class, "mSuperElement"));
  }

  @Test
  public void T_and_QUESTION_MARK_super_T_are_not_equals() {
    assertThat(resolveGenericReturnType(Api.class, "m"))
      .isNotEqualTo(resolveGenericReturnType(Api.class, "mSuper"));
  }

  @Test
  public void T_extends_something_returns_T_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "returnsT"))
      .isEqualTo(resolveGenericReturnType(Api.class, "returnsT2"));
  }

  @Test
  public void list_asserts_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "listAssert"))
      .isEqualTo(resolveGenericReturnType(Api.class, "listAssert2"));
  }

  @Test
  public void K_and_V_and_K_and_V_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "doubleTypeVariables"))
      .isEqualTo(resolveGenericReturnType(Api.class, "doubleTypeVariables2"));
  }

  @Test
  public void generic_array_T_and_generic_array_ELEMENT_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "genericArray"))
      .isEqualTo(resolveGenericReturnType(Api.class, "genericArray2"));
  }

  @Test
  public void double_generic_array_T_and_double_generic_array_ELEMENT_are_equal() {
    assertThat(resolveGenericReturnType(Api.class, "doubleGenericArray"))
      .isEqualTo(resolveGenericReturnType(Api.class, "doubleGenericArray2"));
  }

  private static Type resolveGenericReturnType(Class<?> cls, String methodName) {
    for (Method method : cls.getMethods()) {
      if (method.getName().equals(methodName)) {
        return TypeCanonizer.canonize(method.getGenericReturnType());
      }
    }

    throw new RuntimeException("Method not found: class=" + cls + " name=" + methodName);
  }
}
