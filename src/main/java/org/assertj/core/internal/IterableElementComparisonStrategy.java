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
import static org.assertj.core.util.IterableUtil.sizeOf;

import java.util.Comparator;
import java.util.Iterator;

public class IterableElementComparisonStrategy<T> extends StandardComparisonStrategy {

  private Comparator<? super T> elementComparator;

  public IterableElementComparisonStrategy(Comparator<? super T> elementComparator) {
	this.elementComparator = elementComparator;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean areEqual(Object actual, Object other) {
	if (actual == null && other == null) return true;
	if (actual == null || other == null) return false;
	// expecting actual and other to be iterable<T>
	return actual instanceof Iterable && other instanceof Iterable
	       && compareElementsOf((Iterable<T>) actual, (Iterable<T>) other);
  }

  private boolean compareElementsOf(Iterable<T> actual, Iterable<T> other) {
	if (sizeOf(actual) != sizeOf(other)) return false;
	// compare their elements with elementComparator
	Iterator<T> iterator = other.iterator();
	for (T actualElement : actual) {
	  T otherElement = iterator.next();
	  if (elementComparator.compare(actualElement, otherElement) != 0) return false;
	}
	return true;
  }

  @Override
  public String toString() {
    return "IterableElementComparisonStrategy using " + CONFIGURATION_PROVIDER.representation()
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
