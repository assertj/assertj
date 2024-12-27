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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal;

/**
 * Reports an encoding issue.
 *
 * @author Ludovic VIEGAS
 */
public class EncodingIssue {
  private final long line;
  private final String string;

  public EncodingIssue(long line, String string) {
    this.line = line;
    this.string = string;
  }

  /**
   * Get the line containing the encoding issue.
   *
   * @return the line number
   */
  public long getLine() {
    return line;
  }

  /**
   * Get the text containing the encoding issue.
   *
   * @return the string
   */
  public String getString() {
    return string;
  }

  @Override
  public String toString() {
    return String.format("line %d: %s", line, string);
  }
}
