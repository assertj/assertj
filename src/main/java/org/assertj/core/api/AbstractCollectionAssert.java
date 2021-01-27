package org.assertj.core.api;

import org.assertj.core.data.Index;
import org.assertj.core.internal.Collection;

import java.util.List;

public abstract class AbstractCollectionAssert <SELF extends AbstractCollectionAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
  ACTUAL extends java.util.Collection<? extends ELEMENT>,
  ELEMENT,
  ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
  extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>
  implements IndexedObjectEnumerableAssert<SELF, ELEMENT> {

  Collection collection = Collection.instance();

  protected AbstractCollectionAssert(ACTUAL elements, Class<?> selfType) {
    super(elements, selfType);
  }


  /** {@inheritDoc} */
  @Override
  public SELF contains(ELEMENT value, Index index) {
//    lists.assertContains(info, actual, value, index);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotContain(ELEMENT value, Index index) {
//    lists.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies the collection is unmodifiable
   * <p>
   * Example:
   * <pre><code class='java'>
   *
   * // this assertion will pass
   * assertThat(Collections.unmodifiableCollection(new ArrayList<>())).isUnmodifiable()
   *
   * // this assertion will fail
   * assertThat(new ArrayList<>()).isUnmodifiable()
   *
   * @return {@code this} assertion object
   *
   * @throws NullPointerException if provided Collection is null
   * @throws AssertionError if the Collection provided is modifiable
   *
   * @since 3.18.2
   */
  public SELF isUnmodifiable() {
      collection.assertIsUnmodifiable(info, actual);
      return myself;
  }
}
