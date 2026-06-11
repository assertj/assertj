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
package org.assertj.tests.core.api.recursive.data;

import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.util.Lists.list;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.FieldLocation;

public class DualValueUtil {

  private static final FieldLocation FIELD_LOCATION = new FieldLocation("foo.bar");

  public static DualValue dualValue(Object value1, Object value2) {
    return new DualValue(FIELD_LOCATION, value1, value2, null);
  }

  public static DualValue rootDualValue(Object actual, Object expected) {
    return new DualValue(rootFieldLocation(), actual, expected, null);
  }

  public static DualValue dualValueWithPath(String... pathElements) {
    return new DualValue(new FieldLocation(list(pathElements)), "foo", "bar", null);
  }

  public static DualValue dualValue(String fieldLocation, Object value1, Object value2) {
    return new DualValue(new FieldLocation(fieldLocation), value1, value2, null);
  }

  public static DualValue dualValueWithRandomFieldLocation(Object value1, Object value2) {
    return new DualValue(randomFieldLocation(), value1, value2, null);
  }

  public static FieldLocation randomFieldLocation() {
    String path = RandomStringUtils.secure().next(RandomUtils.secure().randomInt(1, 10), true, false);
    return new FieldLocation(list(path));
  }

}
