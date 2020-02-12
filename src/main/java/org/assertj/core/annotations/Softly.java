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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.annotations;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.extention.SoftlyExtension;

import java.lang.annotation.*;


/**
 * Signifies that a {@link SoftAssertions} field in test
 * class should be managed by {@link SoftlyExtension}
 *
 * @see SoftlyExtension
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Softly {
  /**
   * Whether to call {@link SoftAssertions#assertAll()} after
   * each test method
   *
   * @return true if behaviour is desired
   */
  boolean assertAfter() default true;
}
