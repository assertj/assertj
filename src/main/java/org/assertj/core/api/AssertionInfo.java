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

import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

/**
 * Information about an assertion.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public interface AssertionInfo {

  /**
   * Returns the message that, if specified, will replace the default message of an assertion failure.
   * @return the message that, if specified, will replace the default message of an assertion failure.
   */
  String overridingErrorMessage();

  /**
   * Returns the description of an assertion.
   * @return the description of an assertion.
   */
  Description description();

  Representation representation();
}