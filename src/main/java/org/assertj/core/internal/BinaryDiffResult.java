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
package org.assertj.core.internal;

/**
 * Value class to hold the result of comparing two binary streams.
 * 
 * @author Olivier Michallat
 */
public class BinaryDiffResult {
  private static final int EOF = -1;

  public final int offset;
  public final String expected;
  public final String actual;
  
  /**
   * Builds a new instance.
   * 
   * @param offset the offset at which the difference occurred.
   * @param expected the expected byte as an int in the range 0 to 255, or -1 for EOF.
   * @param actual the actual byte in the same format.
   */
  public BinaryDiffResult(int offset, int expected, int actual) {
    this.offset = offset;
    this.expected = describe(expected);
    this.actual = describe(actual);
  }
  
  public boolean hasNoDiff() {
    return offset == EOF;
  }
  
  public static BinaryDiffResult noDiff() {
    return new BinaryDiffResult(EOF, 0, 0);
  }

  private String describe(int b) {
    return (b == EOF) ? "EOF" : "0x" + Integer.toHexString(b).toUpperCase();
  }
}
