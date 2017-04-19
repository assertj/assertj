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
package org.assertj.core.internal;

public final class CommonErrors {

  public static NullPointerException arrayOfValuesToLookForIsNull() {
    return new NullPointerException(ErrorMessages.arrayOfValuesToLookForIsNull());
  }

  public static NullPointerException iterableToLookForIsNull() {
    return new NullPointerException(ErrorMessages.iterableToLookForIsNull());
  }

  public static NullPointerException iterableOfValuesToLookForIsNull() {
    return new NullPointerException(ErrorMessages.iterableValuesToLookForIsNull());
  }

  public static IllegalArgumentException arrayOfValuesToLookForIsEmpty() {
    return new IllegalArgumentException(ErrorMessages.arrayOfValuesToLookForIsEmpty());
  }

  public static IllegalArgumentException iterableOfValuesToLookForIsEmpty() {
    return new IllegalArgumentException(ErrorMessages.iterableValuesToLookForIsEmpty());
  }

  public static void wrongElementTypeForFlatExtracting(Object group) {
    throw new IllegalArgumentException("Flat extracting expects extracted values to be Iterables or arrays but was a "
                                       + group.getClass().getSimpleName());
  }

  private CommonErrors() {}

}
