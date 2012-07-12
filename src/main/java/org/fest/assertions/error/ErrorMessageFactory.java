/*
 * Created on Oct 18, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.description.Description;

/**
 * Factory of error messages.
 * 
 * @author Alex Ruiz
 */
public interface ErrorMessageFactory {

  /**
   * Creates a new error message as a result of a failed assertion.
   * @param d the description of the failed assertion.
   * @return the created error message.
   */
  String create(Description d);
}