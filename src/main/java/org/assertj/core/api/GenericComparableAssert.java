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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Concrete generic <code>{@link ComparableAssert}</code> to be used through {@link Assertions#assertThat(java.lang.Comparable)}.
 * @param <A> the type of the "actual" value.
 */
public class GenericComparableAssert<A extends Comparable<? super A>>
        extends AbstractComparableAssert<GenericComparableAssert<A>, A> {

    public GenericComparableAssert(A actual) {
        super(actual, GenericComparableAssert.class);
    }
}
