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
package org.fest.assertions.group;

import static org.fest.assertions.test.ArrayFactory.emptyArrayOf;
import static org.fest.assertions.test.ParameterSource.parametersFrom;
import static org.fest.assertions.test.Types.primitiveTypes;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link ArrayIsEmptyChecker#canHandle(Object)}</code>
 *
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class ArrayIsEmptyChecker_canHandle_withArrays_Test {

  private final Object array;

  @Parameters public static List<Object[]> parameters() {
    List<Class<?>> types = primitiveTypes();
    types.add(Object.class);
    return parametersFrom(types);
  }

  public ArrayIsEmptyChecker_canHandle_withArrays_Test(Class<?> type) {
    this.array = emptyArrayOf(type);
  }

  private static ArrayIsEmptyChecker checker;

  @BeforeClass public static void setUpOnce() {
    checker = ArrayIsEmptyChecker.instance();
  }

  @Test public void should_return_true_if_object_is_array() {
    assertTrue(checker.canHandle(array));
  }
}
