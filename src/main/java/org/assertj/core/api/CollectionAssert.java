package org.assertj.core.api;

import java.util.Collection;

public class CollectionAssert<ELEMENT> extends
  FactoryBasedNavigableCollectionAssert<CollectionAssert<ELEMENT>, Collection<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {


  public CollectionAssert(Collection<? extends ELEMENT> elements) {
    super(elements, CollectionAssert.class, new ObjectAssertFactory<>());
  }
}
