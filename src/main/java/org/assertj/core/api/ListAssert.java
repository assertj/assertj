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

/**
 * Assertion methods for {@link List}s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(List)}</code>.
 * <p>
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ListAssert<ELEMENT> extends
    FactoryBasedNavigableListAssert<ListAssert<ELEMENT>, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> {

  public ListAssert(List<? extends ELEMENT> actual) {
    super(actual, ListAssert.class, new ObjectAssertFactory<ELEMENT>());
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> contains(ELEMENT... values) {
    return super.contains(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsOnly(ELEMENT... values) {
    return super.containsOnly(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsOnlyOnce(ELEMENT... values) {
    return super.containsOnlyOnce(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsExactly(ELEMENT... values) {
    return super.containsExactly(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsExactlyInAnyOrder(ELEMENT... values) {
    return super.containsExactlyInAnyOrder(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> isSubsetOf(ELEMENT... values) {
    return super.isSubsetOf(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsSequence(ELEMENT... sequence) {
    return super.containsSequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> containsSubsequence(ELEMENT... sequence) {
    return super.containsSubsequence(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> doesNotContain(ELEMENT... values) {
    return super.doesNotContain(values);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> startsWith(ELEMENT... sequence) {
    return super.startsWith(sequence);
  }

  @Override
  @SafeVarargs
  public final ListAssert<ELEMENT> endsWith(ELEMENT... sequence) {
    return super.endsWith(sequence);
  }

}
