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
package org.fest.assertions.internal;

import java.util.Map;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.MapEntry;

/**
 * TODO comment.
 *
 * @author Alex Ruiz
 */
public class Maps {

  private static Maps INSTANCE = new Maps();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Maps instance() {
    return INSTANCE ;
  }

  private Maps() {}

  /**
   * @param info
   * @param actual
   */
  public void assertNullOrEmpty(AssertionInfo info, Map<?, ?> actual) {

  }

  /**
   * @param info
   * @param actual
   */
  public void assertEmpty(AssertionInfo info, Map<?, ?> actual) {
    // TODO Auto-generated method stub

  }

  /**
   * @param info
   * @param actual
   */
  public void assertNotEmpty(AssertionInfo info, Map<?, ?> actual) {
    // TODO Auto-generated method stub

  }

  /**
   * @param info
   * @param actual
   * @param expectedSize
   */
  public void assertHasSize(AssertionInfo info, Map<?, ?> actual, int expectedSize) {
    // TODO Auto-generated method stub

  }

  /**
   * @param info
   * @param actual
   * @param entries
   */
  public void assertContains(AssertionInfo info, Map<?, ?> actual, MapEntry[] entries) {
    // TODO Auto-generated method stub

  }

  /**
   * @param info
   * @param actual
   * @param entries
   */
  public void assertDoesNotContain(AssertionInfo info, Map<?, ?> actual, MapEntry[] entries) {
    // TODO Auto-generated method stub

  }
}
