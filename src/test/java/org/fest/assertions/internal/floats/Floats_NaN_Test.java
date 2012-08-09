/*
 * Created on Jan 14, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.floats;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.fest.assertions.internal.Floats;
import org.fest.assertions.internal.FloatsBaseTest;

/**
 * Tests for <code>{@link Floats#NaN()}</code>.
 * 
 * @author Joel Costigliola
 */
public class Floats_NaN_Test extends FloatsBaseTest {

  @Test
  public void check_float_NaN_method() {
    assertEquals(Float.NaN, NaN(), 0d);
  }
}