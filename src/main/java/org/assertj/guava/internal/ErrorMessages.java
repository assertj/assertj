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
package org.assertj.guava.internal;

/**
 * @author Ilya Koshaleu
 */
public final class ErrorMessages {

  public static String rangeSetValuesToLookForIsNull() {
    return "Range set of values to look for should not be null";
  }

  public static String rangeSetValuesToLookForIsEmpty() {
    return "Range set of values to look for should not be empty";
  }

  private ErrorMessages() {}
}
