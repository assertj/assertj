/*
 * Created on Dec 21, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import java.util.Map;

import org.fest.assertions.core.EnumerableAssert;
import org.fest.assertions.data.MapEntry;

/**
 * Assertions for <code>{@link Map}</code>s.
 * <p>
 * To create a new instance of this class invoke <code>{@link Assertions#assertThat(Map)}</code>.
 * </p>
 *
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class MapAssert extends GenericAssert<MapAssert, Map<?, ?>> implements EnumerableAssert<MapAssert> {

  protected MapAssert(Map<?, ?> actual) {
    super(actual, MapAssert.class);
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    // TODO Auto-generated method stub
  }

  /** {@inheritDoc} */
  public void isEmpty() {
    // TODO Auto-generated method stub
  }

  /** {@inheritDoc} */
  public MapAssert isNotEmpty() {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public MapAssert hasSize(int expected) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   *
   * @param entries
   * @return
   */
  public MapAssert contains(MapEntry...entries) {
    // TODO implement
    return null;
  }

  /**
   *
   * @param entries
   * @return
   */
  public MapAssert doesNotContain(MapEntry...entries) {
    // TODO implement
    return null;
  }
}
