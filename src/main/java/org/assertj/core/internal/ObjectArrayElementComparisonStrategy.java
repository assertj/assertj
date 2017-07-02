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

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.util.Arrays.isArray;

import java.util.Comparator;

public class ObjectArrayElementComparisonStrategy<T> extends StandardComparisonStrategy {

  private Comparator<? super T> elementComparator;

  public ObjectArrayElementComparisonStrategy(Comparator<? super T> elementComparator) {
	this.elementComparator = elementComparator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean areEqual(Object actual, Object other) {
	if (actual == null && other == null) return true;
	if (actual == null || other == null) return false;
    // expecting actual and other to be T[]
    return isArray(actual) && isArray(other) && compareElementsOf((T[]) actual, (T[]) other);
  }

  private boolean compareElementsOf(T[] actual, T[] other) {
    if (actual.length != other.length) return false;
	// compare their elements with elementComparator
    for (int i = 0; i < actual.length; i++) {
      if (elementComparator.compare(actual[i], other[i]) != 0) return false;
	}
	return true;
  }

  @Override
  public String toString() {
    return "ObjectArrayElementComparisonStrategy using " + CONFIGURATION_PROVIDER.representation()
                                                                                 .toStringOf(elementComparator);
  }
  
  @Override
  public String asText() {
    return "when comparing elements using " + CONFIGURATION_PROVIDER.representation().toStringOf(elementComparator);
  }
  
  @Override
  public boolean isStandard() {
    return false;
  }
}
