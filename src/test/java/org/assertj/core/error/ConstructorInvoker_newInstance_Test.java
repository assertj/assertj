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
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.error.ConstructorInvoker;
import org.junit.*;

/**
 * Tests for <code>{@link ConstructorInvoker#newInstance(String, Class[], Object[])}</code>.
 * 
 * @author Alex Ruiz
 */
public class ConstructorInvoker_newInstance_Test {

  private ConstructorInvoker invoker;

  @Before
  public void setUp() {
    invoker = new ConstructorInvoker();
  }

  @Test
  public void should_create_Object_using_reflection() throws Exception {
    Object o = invoker.newInstance("java.lang.Exception", new Class<?>[] { String.class }, new Object[] { "Hi" });
    assertThat(o).isInstanceOf(Exception.class);
    assertThat((Exception) o).hasMessage("Hi");
  }
}
