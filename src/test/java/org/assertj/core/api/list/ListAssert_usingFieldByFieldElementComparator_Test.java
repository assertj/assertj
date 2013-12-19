package org.assertj.core.api.list;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.internal.FieldByFieldComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Lists;
import org.junit.Before;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class ListAssert_usingFieldByFieldElementComparator_Test extends ListAssertBaseTest {

    private Lists listsBefore;
    private Iterables iterablesBefore;

    @Before
    public void before() {
        listsBefore = getLists(assertions);
        iterablesBefore = getIterables(assertions);
    }

    @Override
    protected ListAssert<String> invoke_api_method() {
        return assertions.usingFieldByFieldElementComparator();
    }

    @Override
    protected void verify_internal_effects() {
        assertNotSame(getLists(assertions), listsBefore);
        assertNotSame(getIterables(assertions), iterablesBefore);
        assertTrue(getLists(assertions).getComparisonStrategy() instanceof FieldByFieldComparisonStrategy);
        assertTrue(getIterables(assertions).getComparisonStrategy() instanceof FieldByFieldComparisonStrategy);
    }

}
