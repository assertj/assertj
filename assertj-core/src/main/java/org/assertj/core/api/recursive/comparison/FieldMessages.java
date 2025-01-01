/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import java.util.Map.Entry;
import java.util.stream.Stream;

/**
 * An internal holder of the custom messages for fields described by their path without element index.
 */
public class FieldMessages extends FieldHolder<String> {

  /**
   * Pairs the giving error {@code message} with the {@code fieldLocation}.
   *
   * @param fieldLocation the field location where to apply the giving error message
   * @param message the error message
   */
  public void registerMessage(String fieldLocation, String message) {
    super.put(fieldLocation, message);
  }

  /**
   * Checks, whether an any custom message is associated with the giving field location.
   *
   * @param fieldLocation the field location which association need to check
   * @return is field location contain a custom message
   */
  public boolean hasMessageForField(String fieldLocation) {
    return super.hasEntity(fieldLocation);
  }

  /**
   * Retrieves a custom message, which is associated with the giving field location. If this location does not
   * associate with any custom message - this method returns null.
   *
   * @param fieldLocation the field location that has to be associated with a message
   * @return a custom message or null
   */
  public String getMessageForField(String fieldLocation) {
    return super.get(fieldLocation);
  }

  /**
   * Returns a sequence of associated field-message pairs.
   *
   * @return sequence of field-message pairs
   */
  public Stream<Entry<String, String>> messageByFields() {
    return super.entryByField();
  }
}
