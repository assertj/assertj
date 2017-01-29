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

import java.util.List;

import org.assertj.core.error.MessageFormatter;

/**
 * An AssertionError that contains the error messages of the one or more AssertionErrors that caused this exception to
 * be thrown.
 * 
 * @author Brian Laframboise
 * 
 */
public class SoftAssertionError extends AssertionError {
  private static final long serialVersionUID = 5034494920024670595L;
  private final List<String> errors;
  private static final MessageFormatter formatter = MessageFormatter.instance();

  /**
   * Creates a new SoftAssertionError.
   * 
   * @param errors the causal AssertionError error messages in the order that they were thrown
   */
  public SoftAssertionError(List<String> errors) {
    super(createMessage(errors));
    this.errors = errors;
  }

  private static String createMessage(List<String> errors) {
    StringBuilder msg = new StringBuilder("%nThe following ");
    int size = errors.size();

    if (size == 1) {
      msg.append("assertion");
    } else {
      msg.append(size).append(" assertions");
    }
    msg.append(" failed:%n");

    for (int i = 0; i < size; i++) {
      msg.append(i + 1).append(") ").append(errors.get(i)).append("%n");
    }
    return formatter.format(null,null,msg.toString());
  }

  /**
   * Returns the causal AssertionError error messages in the order that they were thrown.
   * 
   * @return the list of error messages
   */
  public List<String> getErrors() {
    return errors;
  }
}
