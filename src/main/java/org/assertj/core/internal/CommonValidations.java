/*
 * Created on Nov 29, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonErrors.iterableOfValuesForIsNull;
import static org.assertj.core.internal.CommonErrors.iterableOfValuesToLookForIsEmpty;
import static org.assertj.core.util.Iterables.sizeOf;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.data.Offset;

import java.lang.reflect.Array;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
final class CommonValidations {

  private static Failures failures = Failures.instance();

  private CommonValidations() {}
  
  static void checkIndexValueIsValid(Index index, int maximum) {
    if (index == null) throw new NullPointerException("Index should not be null");
    if (index.value <= maximum) return;
    String errorMessage = "Index should be between <%d> and <%d> (inclusive,) but was:\n <%d>";
    throw new IndexOutOfBoundsException(format(errorMessage, 0, maximum, index.value));
  }

  static void checkOffsetIsNotNull(Offset<?> offset) {
    if (offset == null) throw new NullPointerException("The given offset should not be null");
  }

  static void checkNumberIsNotNull(Number number) {
    if (number == null) throw new NullPointerException("The given number should not be null");
  }

  static void checkIsNotEmpty(Object[] values) {
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
  }

  static void checkIsNotEmpty(Iterable<?> iterable) {
    if (!iterable.iterator().hasNext()) throw iterableOfValuesToLookForIsEmpty();
  }

  static void checkIsNotNull(Object[] values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
  }

  static void checkIsNotNull(Iterable<?> iterable) {
    if (iterable == null) throw iterableOfValuesForIsNull();
  }

  static void checkIsNotNullAndNotEmpty(Object[] values) {
    checkIsNotNull(values);
    checkIsNotEmpty(values);
  }

  static void checkIsNotNullAndNotEmpty(Iterable<?> iterable) {
    checkIsNotNull(iterable);
    checkIsNotEmpty(iterable);
  }

  static void failIfEmptySinceActualIsNotEmpty(Object[] values) {
    if (values.length == 0) throw new AssertionError("actual is not empty");
  }

  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Object other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Array");
    checkSameSizes(info, actual, sizeOfActual, Array.getLength(other));
  }

  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Iterable<?> other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Iterable");
    checkSameSizes(info, actual, sizeOfActual, sizeOf(other));
  }

  private static void checkOtherIsNotNull(Object other, String otherType) {
    if (other == null) throw new NullPointerException("The "+ otherType +" to compare actual size with should not be null");
  }

  private static void checkSameSizes(AssertionInfo info, Object actual, int sizeOfActual, int sizeOfOther) {
    if (sizeOfActual != sizeOfOther) throw failures.failure(info, shouldHaveSameSizeAs(actual, sizeOfActual, sizeOfOther));
  }

  public static void checkSizes(Object actual, int sizeOfActual, int sizeOfOther, AssertionInfo info) {
    if (sizeOfActual != sizeOfOther) throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, sizeOfOther));
  }

  public static void checkTypeIsNotNull(Class<?> expectedType) {
    if (expectedType == null) throw new NullPointerException("The given type should not be null");
  }
}
