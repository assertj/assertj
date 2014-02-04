package org.assertj.core.api.iterable;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.FieldByFieldComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.junit.Before;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class IterableAssert_usingFieldByFieldElementComparator_Test extends IterableAssertBaseTest {

    private Iterables iterablesBefore;

    @Before
    public void before() {
        iterablesBefore = getIterables(assertions);
    }

    @Override
    protected ConcreteIterableAssert<Object> invoke_api_method() {
        return assertions.usingFieldByFieldElementComparator();
    }

    @Override
    protected void verify_internal_effects() {
        assertNotSame(getIterables(assertions), iterablesBefore);
        assertTrue(getIterables(assertions).getComparisonStrategy() instanceof FieldByFieldComparisonStrategy);
    }

}
