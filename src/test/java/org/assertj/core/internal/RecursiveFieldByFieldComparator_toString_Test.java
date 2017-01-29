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

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RecursiveFieldByFieldComparator_toString_Test {

  @Test
  @SuppressWarnings("unchecked")
  public void should_return_description_of_RecursiveFieldByFieldComparator() {
    assertThat(new RecursiveFieldByFieldComparator(EMPTY_MAP, new TypeComparators())).hasToString("recursive field/property by field/property comparator on all fields/properties");
  }
}
