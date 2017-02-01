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
package org.assertj.core.error;

import java.util.concurrent.atomic.AtomicStampedReference;

public class ShouldHaveStamp extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_STAMP = "%nExpecting%n  <%s>%nto have stamp:%n  <%s>%nbut had:%n  <%s>";

  private ShouldHaveStamp(AtomicStampedReference<?> actual, int expectedStamp) {
    super(SHOULD_HAVE_STAMP, actual, expectedStamp, actual.getStamp());
  }

  public static ErrorMessageFactory shouldHaveStamp(AtomicStampedReference<?> actual, int expectedStamp) {
    return new ShouldHaveStamp(actual, expectedStamp);
  }
}
