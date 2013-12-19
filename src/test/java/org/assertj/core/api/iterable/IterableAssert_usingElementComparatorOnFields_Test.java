package org.assertj.core.api.iterable;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.OnFieldsComparisonStrategy;
import org.junit.Before;

import static org.junit.Assert.*;

public class IterableAssert_usingElementComparatorOnFields_Test extends IterableAssertBaseTest {

    private Iterables iterablesBefore;

    @Before
    public void before() {
        iterablesBefore = getIterables(assertions);
    }

    @Override
    protected ConcreteIterableAssert<Object> invoke_api_method() {
        return assertions.usingElementComparatorOnFields("field");
    }

    @Override
    protected void verify_internal_effects() {
        assertNotSame(getIterables(assertions), iterablesBefore);
        assertTrue(getIterables(assertions).getComparisonStrategy() instanceof OnFieldsComparisonStrategy);
        assertArrayEquals(new String[]{"field"}, ((OnFieldsComparisonStrategy) getIterables(assertions).getComparisonStrategy()).getFields());
    }

}
