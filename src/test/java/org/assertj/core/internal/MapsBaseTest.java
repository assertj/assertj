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
package org.assertj.core.internal;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.Maps.mapOf;


import static org.mockito.Mockito.spy;

import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Maps;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for {@link Maps} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Maps} attributes appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class MapsBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Map<String, String> actual;
  protected Failures failures;
  protected Maps maps;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    actual = (Map<String, String>) mapOf(entry("name", "Yoda"), entry("color", "green"));
    failures = spy(new Failures());
    maps = new Maps();
    maps.failures = failures;
  }

  protected static MapEntry[] emptyEntries() {
    return new MapEntry[0];
  }
  
  protected static String[] emptyKeys() {
    return new String[0];
  }
}