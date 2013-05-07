/*
 * Created on Oct 26, 2010
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.api;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for <code>{@link Assertions#assertThat(List)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public class Assertions_assertThat_with_List_Test {
  @Test
  public void should_create_Assert() {
    ListAssert<Object> assertions = Assertions.assertThat(emptyList());
    assertNotNull(assertions);

      List<String> strings0 = new ArrayList<String>();
      List<? extends String> strings1 = new ArrayList<String>();
    Assertions.assertThat(strings0).isEqualTo(strings1);
  }

  @Test
  public void should_pass_actual() {
    List<String> names = singletonList("Luke");
    ListAssert<String> assertions = Assertions.assertThat(names);
    assertSame(names, assertions.actual);
  }
}
