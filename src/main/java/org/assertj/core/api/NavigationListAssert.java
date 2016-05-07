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

import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Descriptions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 */
public class NavigationListAssert<T, EA extends AbstractAssert> extends ListAssert<T> {
    private final AssertFactory<T, EA> assertFactory;

    public NavigationListAssert(List<? extends T> actual, AssertFactory<T, EA> assertFactory) {
        super(actual);
        this.assertFactory = assertFactory;
    }

    /**
     * Navigates to the first element in the list if the list is not empty
     *
     * @return the assertion on the first element
     */
    public EA first() {
        isNotEmpty();
        return toAssert(actual.get(0), Descriptions.navigateDescription(this, "first()"));
    }

    /**
     * Navigates to the last element in the list if the list is not empty
     *
     * @return the assertion on the last element
     */
    public EA last() {
        isNotEmpty();
        return toAssert(actual.get(actual.size() - 1), Descriptions.navigateDescription(this, "last()"));
    }

    /**
     * Navigates to the element at the given index if the index is within the range of the list
     *
     * @return the assertion on the given element
     */
    public EA item(int index) {
        isNotEmpty();
        assertThat(index).describedAs(Descriptions.navigateDescription(this, "index")).isGreaterThanOrEqualTo(0).isLessThan(actual.size());
        return toAssert(actual.get(index), Descriptions.navigateDescription(this, "index(" + index + ")"));
    }

    protected EA toAssert(T value, String description) {
        return (EA) assertFactory.createAssert(value).describedAs(description);
    }
}
