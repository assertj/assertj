package org.assertj.core.internal;

import static org.assertj.core.util.Iterables.sizeOf;

import java.util.Comparator;
import java.util.Iterator;

import org.assertj.core.presentation.StandardRepresentation;

public class IterableElementComparisonStrategy<T> extends StandardComparisonStrategy {

  // stateless => can be shared
  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

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
	return "IterableElementComparisonStrategy using " + STANDARD_REPRESENTATION.toStringOf(elementComparator);
  }
  
  @Override
  public String asText() {
    return "when comparing elements using " + STANDARD_REPRESENTATION.toStringOf(elementComparator);
  }
  
  @Override
  public boolean isStandard() {
    return false;
  }
}
