/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.fest.assertions.api;

import com.google.common.collect.Multimap;

/**
 * The entry point for all Guava assertions.
 * 
 * @author @marcelfalliere
 * @author @miralak
 * @author Joel Costigliola
 */
public class GUAVA {

  public static <K, V> MultimapAssert<K, V> assertThat(Multimap<K, V> actual) {
    return new MultimapAssert<K, V>(actual);
  }

  /**
   * protected to avoid direct instanciation but allowing subclassing.
   */
  protected GUAVA() {
    // empty
  }

}
