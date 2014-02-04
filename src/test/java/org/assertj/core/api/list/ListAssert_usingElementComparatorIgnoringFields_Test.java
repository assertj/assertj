package org.assertj.core.api.list;

import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ListAssertBaseTest;
import org.assertj.core.internal.IgnoringFieldsComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.Lists;
import org.junit.Before;

import static org.junit.Assert.*;

public class ListAssert_usingElementComparatorIgnoringFields_Test extends ListAssertBaseTest {

    private Lists listsBefore;
    private Iterables iterablesBefore;

    @Before
    public void before() {
        listsBefore = getLists(assertions);
        iterablesBefore = getIterables(assertions);
    }

    @Override
    protected ListAssert<String> invoke_api_method() {
        return assertions.usingElementComparatorIgnoringFields("field");
    }

    @Override
    protected void verify_internal_effects() {
        assertNotSame(getLists(assertions), listsBefore);
        assertNotSame(getIterables(assertions), iterablesBefore);
        assertTrue(getLists(assertions).getComparisonStrategy() instanceof IgnoringFieldsComparisonStrategy);
        assertArrayEquals(new String[]{"field"}, ((IgnoringFieldsComparisonStrategy) getLists(assertions).getComparisonStrategy()).getFields());
        assertTrue(getIterables(assertions).getComparisonStrategy() instanceof IgnoringFieldsComparisonStrategy);
        assertArrayEquals(new String[]{"field"}, ((IgnoringFieldsComparisonStrategy) getIterables(assertions).getComparisonStrategy()).getFields());
    }

}
