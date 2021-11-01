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
package org.assertj.core.api.recursive.assertion;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNoException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.assertj.core.api.recursive.FieldLocation;
import org.junit.jupiter.api.Test;

class RecursiveAssertionDriver_RecursionTest extends AbstractRecursiveAssertionDriverTestBase {
  
  @Test
  void should_recurse_through_object_tree() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object objectTree = simpleCycleStructure();
    Predicate<Object> boomOnOveruse = predicateThatThrowsWhenCalledTooOften(25);
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(boomOnOveruse, objectTree);
    // THEN
    then(failedFields).hasSize(3).contains(
             FieldLocation.rootFieldLocation(),
             FieldLocation.rootFieldLocation().field("linkToMiddle"),
             FieldLocation.rootFieldLocation().field("linkToMiddle").field("linkToBottom")
        );
  }
  
  @Test
  void should_detect_cycle_and_break_looping() {
    // GIVEN
    RecursiveAssertionDriver objectUnderTest = testSubjectWithDefaultConfiguration();
    Object objectTree = simpleCycleStructure();
    Predicate<Object> boomOnOveruse = predicateThatThrowsWhenCalledTooOften(100);
    // WHEN / THEN
    thenNoException().isThrownBy(() -> objectUnderTest.assertOverObjectGraph(boomOnOveruse, objectTree));
  }

  private Predicate<Object> predicateThatThrowsWhenCalledTooOften(int maxCalls) {
    AtomicInteger callLimit = new AtomicInteger(maxCalls - 1);
    return o -> {
      int call = callLimit.getAndDecrement();
      if (call < 0) throw new RuntimeException("Called too often -- assuming cycling -- BOOM!");
      return false;
    };
  }



  Top simpleCycleStructure() {
    Top top = new Top();
    Middle middle = new Middle();
    Bottom bottom = new Bottom();
    
    top.linkToMiddle = middle;
    middle.linkToBottom = bottom;
    bottom.loopBackToTop = top;
    
    return top;
  }
  
  class Top {
    Middle linkToMiddle;
  }
  
  class Middle {
    Bottom linkToBottom;
  }
  
  class Bottom {
    Top loopBackToTop;
  }
}
