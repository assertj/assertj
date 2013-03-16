/*
 * Created on Sep 4, 2012
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
 * Copyright @2012 Google Inc. and others.
 */
package org.assertj.core.util;

/**
 * Verifies correct argument values and state. Borrowed from Guava.
 * 
 * @author alruiz@google.com (Alex Ruiz)
 */
public final class Preconditions {
  /**
   * Verifies that the given {@code String} is not {@code null} or empty.
   * 
   * @param s the given {@code String}.
   * @return the validated {@code String}.
   * @throws NullPointerException if the given {@code String} is {@code null}.
   * @throws IllegalArgumentException if the given {@code String} is empty.
   */
  public static String checkNotNullOrEmpty(String s) {
    checkNotNull(s);
    if (s.isEmpty()) {
      throw new IllegalArgumentException();
    }
    return s;
  }

  /**
   * Verifies that the given object reference is not {@code null}.
   * 
   * @param reference the given object reference.
   * @return the non-{@code null} reference that was validated.
   * @throws NullPointerException if the given object reference is {@code null}.
   */
  public static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException();
    }
    return reference;
  }

  private Preconditions() {}
}
