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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.iterable;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ListAssert;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ContainsGenericsProblemTest {
  @Test
  public void testWithoutGenerics() throws Exception {
    List strings = Arrays.asList( "a", "b", "c" );
    assertThat( strings ).contains( "a", "b" );
  }

  @Test
  public void testConcreteType() throws Exception {
    List<String> strings = Arrays.asList( "a", "b", "c" );
    assertThat( strings ).contains( "a", "b" );
  }

  @Test
  public void testListAssertWithGenerics() throws Exception {
    List<? extends String> strings = Arrays.asList( "a", "b", "c" );
    assertThat( strings ).contains( "a", "b" );
  }
}
