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

import java.util.concurrent.Future;

/**
 * Assertion methods for {@link Future}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Future)}</code>.
 * </p>
 * @param <RESULT> the type contained within the Future
 *
 * @author Ruben Dijkstra
 */
public class FutureAssert<RESULT> extends AbstractFutureAssert<FutureAssert<RESULT>, Future<RESULT>, RESULT> {

  protected FutureAssert(Future<RESULT> actual) {
    super(actual, FutureAssert.class);
  }

}
