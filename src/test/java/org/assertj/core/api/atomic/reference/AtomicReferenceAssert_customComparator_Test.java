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
package org.assertj.core.api.atomic.reference;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceAssert_customComparator_Test {

  @Test
  public void should_honor_custom_comparator() {
    Comparator<AtomicReference<String>> comparator = new Comparator<AtomicReference<String>>() {
      
      @Override
      public int compare(AtomicReference<String> o1, AtomicReference<String> o2) {
        return o1.get().compareToIgnoreCase(o2.get());
      }
    };
    
    assertThat(new AtomicReference<String>("foo")).usingComparator(comparator).isEqualTo(new AtomicReference<String>("FOO"));
  }
  
}
