/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class UrlQueryParser {

  private static final String EQUAL = "=";

  private static final String AND = "&";

  private UrlQueryParser() {}

  static Map<String, List<String>> parseParameters(String query) {
    Map<String, List<String>> parameters = new LinkedHashMap<>();

    if (query != null && !query.isEmpty()) {
      for (String pair : query.split(AND)) {
        int equalIndex = pair.indexOf(EQUAL);
        String key = equalIndex == -1 ? pair : pair.substring(0, equalIndex);
        String value = equalIndex == -1 ? null : pair.substring(equalIndex + 1);

        try {
          key = URLDecoder.decode(key, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
          // UTF-8 is missing? Allow the key to remain encoded (no reasonable alternative).
        }

        if (value != null) {
          try {
            value = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
          } catch (UnsupportedEncodingException ex) {
            // UTF-8 is missing? Allow the value to remain encoded (no reasonable alternative).
          }
        }

        if (!parameters.containsKey(key)) {
          parameters.put(key, new ArrayList<>());
        }

        parameters.get(key).add(value);
      }
    }

    return parameters;
  }

}
