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

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;

import org.fest.assertions.data.MapEntry;

/**
 * The entry point for all Guava assertions.
 * 
 * @author @marcelfalliere
 * @author @miralak
 * @author Kornel
 * @author Joel Costigliola 
 */
public class GUAVA {

  public static <K, V> MultimapAssert<K, V> assertThat(Multimap<K, V> actual) {
    return new MultimapAssert<K, V>(actual);
  }
  
  public static <T> OptionalAssert<T> assertThat(final Optional<T> actual) {
      return new OptionalAssert<T>(actual);
  }

  // ------------------------------------------------------------------------------------------------------
  // Data utility methods : not assertions but here to have a single entry point to all Guava Assert features.
  // ------------------------------------------------------------------------------------------------------

  /**
   * Only delegate to {@link MapEntry#entry(Object, Object)} so that Assertions offers a full feature entry point to all
   * Fest Assert features (but you can use {@link MapEntry} if you prefer).
   * <p>
   * Typical usage is to call <code>entry</code> in MapAssert <code>contains</code> assertion, see examples below :
   * 
   * <pre>
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));
   * </pre>
   */
  public static MapEntry entry(Object key, Object value) {
    return MapEntry.entry(key, value);
  }

  /**
   * protected to avoid direct instanciation but allowing subclassing.
   */
  protected GUAVA() {
    // empty
  }

}
