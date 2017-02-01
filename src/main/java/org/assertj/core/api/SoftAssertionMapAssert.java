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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Map;

/**
 * Concrete assertions for {@link Map}s without any final methods to allow proxying.
 */
public class SoftAssertionMapAssert<KEY, VALUE> extends AbstractMapAssert<SoftAssertionMapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> {

  public SoftAssertionMapAssert(Map<KEY, VALUE> actual) {
    super(actual, SoftAssertionMapAssert.class);
  }

}
