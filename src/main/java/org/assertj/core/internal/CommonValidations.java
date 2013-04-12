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

import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;

import org.assertj.core.data.Index;
import org.assertj.core.data.Offset;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
final class CommonValidations {

  private CommonValidations() {}
  
  static void checkIndexValueIsValid(Index index, int maximum) {
    if (index == null) throw new NullPointerException("Index should not be null");
    if (index.value <= maximum) return;
    String format = "Index should be between <%d> and <%d> (inclusive,) but was:\n <%d>";
    throw new IndexOutOfBoundsException(String.format(format, 0, maximum, index.value));
  }

  static void checkOffsetIsNotNull(Offset<?> offset) {
    if (offset == null) throw new NullPointerException("The given offset should not be null");
  }

  static void checkNumberIsNotNull(Number number) {
    if (number == null) throw new NullPointerException("The given number should not be null");
  }

  static void checkIsNotEmpty(Object[] values) {
    if (values.length == 0) {
      throw arrayOfValuesToLookForIsEmpty();
    }
  }

  static void checkIsNotNull(Object[] values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
  }

  static void checkIsNotNullAndNotEmpty(Object[] values) {
    checkIsNotNull(values);
    checkIsNotEmpty(values);
  }

  static void failIfEmptySinceActualIsNotEmpty(Object[] values) {
    if (values.length == 0) {
      throw new AssertionError("actual is not empty");
    }
  }
}
