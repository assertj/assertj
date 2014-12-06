/*
 * Created on Apr 29, 2007
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
 * Copyright @2007-2011 the original author or authors.
 */
package org.assertj.core.util;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for <code>{@link Collections#format(org.assertj.core.presentation.Representation, java.util.Collection)}</code>
 * .
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Collections_format_Test {

  @Test
  public void should_return_null_if_Collection_is_null() {
    assertThat(Collections.format(new StandardRepresentation(), null)).isNull();
    assertThat(Collections.format(null)).isNull();
  }

  @Test
  public void should_return_empty_brackets_if_Collection_is_empty() {
    assertThat(Collections.format(new StandardRepresentation(), new ArrayList<String>())).isEqualTo("[]");
    assertThat(Collections.format(new ArrayList<String>())).isEqualTo("[]");
    // custom start and end
    assertThat(Collections.format(new StandardRepresentation(), new ArrayList<String>(), ">", "<")).isEqualTo("><");
    assertThat(Collections.format(new ArrayList<String>(), ">", "<")).isEqualTo("><");
  }

  @Test
  public void should_format_Collection() {
    List<? extends Object> list = asList("First", 3);
    assertThat(Collections.format(new StandardRepresentation(), list)).isEqualTo("[\"First\", 3]");
    assertThat(Collections.format(list)).isEqualTo("[\"First\", 3]");
    // custom start and end
    assertThat(Collections.format(new StandardRepresentation(), list, "(", ")")).isEqualTo("(\"First\", 3)");
    assertThat(Collections.format(list, "(", ")")).isEqualTo("(\"First\", 3)");
  }
}
