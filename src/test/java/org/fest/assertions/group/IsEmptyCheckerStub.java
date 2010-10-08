/*
 * Created on Oct 8, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.group;

class IsEmptyCheckerStub implements IsEmptyChecker {

  private static final IsEmptyCheckerStub MATCHING = new IsEmptyCheckerStub(true);
  private static final IsEmptyCheckerStub NOT_MATCHING = new IsEmptyCheckerStub(false);

  static IsEmptyChecker matchingChecker() {
    return MATCHING;
  }

  static IsEmptyChecker notMatchingChecker() {
    return NOT_MATCHING;
  }

  private final boolean matching;

  private IsEmptyCheckerStub(boolean matching) {
    this.matching = matching;
  }

  public boolean canHandle(Object o) {
    return matching;
  }

  public boolean isEmpty(Object o) {
    return matching;
  }
}