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
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.data.MapEntry;

/**
 * @author Alex Ruiz
 */
public final class Maps {
  public static Map<?, ?> mapOf(MapEntry... entries) {
    Map<Object, Object> map = new LinkedHashMap<Object, Object>();
    for (MapEntry entry : entries)
      map.put(entry.key, entry.value);
    return map;
  }

  private Maps() {
  }
}
