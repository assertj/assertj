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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.internal;

import java.util.Comparator;

import org.assertj.core.api.Assertions;

/**
 * Compare Object field by field including private fields unless
 * {@link Assertions#setAllowComparingPrivateFields(boolean)}has been called with false.
 */
public class FieldByFieldComparator implements Comparator<Object> {

  private static final int NOT_EQUAL = -1;

  @Override
  public int compare(Object actual, Object other) {
	if (actual == null && other == null) return 0;
	if (actual == null || other == null) return NOT_EQUAL;
	// value returned is not relevant for ordering if objects are not equal.
	return areEqual(actual, other) ? 0 : NOT_EQUAL;
  }

  protected boolean areEqual(Object actual, Object other) {
	return Objects.instance().areEqualToIgnoringGivenFields(actual, other);
  }

  @Override
  public String toString() {
	return "field by field comparator on all fields";
  }

}