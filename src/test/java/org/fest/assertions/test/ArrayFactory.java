/*
 * Created on Oct 7, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.test;

import java.lang.reflect.Array;

/**
 * Factory of arrays.
 *
 * @author Alex Ruiz
 */
public final class ArrayFactory {

  public static boolean[] arrayOfBooleans(boolean...elements) {
    return elements;
  }

  public static byte[] arrayOfBytes(int...elements) {
    int size = elements.length;
    byte[] array = new byte[size];
    for (int i = 0; i < size; i++) array[i] = (byte) elements[i];
    return array;
  }

  public static char[] arrayOfChars(char...elements) {
    return elements;
  }

  public static int[] arrayOfInts(int...elements) {
    return elements;
  }

  public static Object emptyArrayOf(Class<?> componentType) {
    return Array.newInstance(componentType, 0);
  }

  private ArrayFactory() {}
}
