/*
 * Created on Sep 7, 2010
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

/**
 * Common failure messages.
 *
 * @author Alex Ruiz
 */
public final class FailureMessages {

  public static String arrayIsEmpty() {
    return "The array of values to evaluate should not be empty";
  }

  public static String arrayIsNull() {
    return "The array of values to evaluate should not be null";
  }

  public static String deltaIsNull() {
    return "The given delta should not be null";
  }

  public static String descriptionIsNull() {
    return "The description to set should not be null";
  }

  public static String unexpectedNull() {
    return "expecting non-null object but it was null";
  }

  private FailureMessages() {}
}
