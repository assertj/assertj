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
package org.assertj.core.navigation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.StringAssert;
import org.junit.Test;

public class ClassBasedNavigableList_withString_Test {

  @Test
  public void should_navigate_to_list_elements_and_perform_specific_string_assertions() {
    List<String> list = newArrayList("one", "two", "three");
  
    assertThat(list, StringAssert.class).first().startsWith("o");
    assertThat(list, StringAssert.class).last().endsWith("ee");
    assertThat(list, StringAssert.class).element(1).contains("w");
  }
  
  @Test
  public void should_honor_list_assertions() {
    List<String> list = newArrayList("one", "two", "three");
    
    assertThat(list, StringAssert.class).contains("one", atIndex(0))
                                        .first().startsWith("o");
  }

}
