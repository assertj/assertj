package org.assertj.core.api.map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link MapAssert#isEqualTo(Object)}</code>.
 *
 * @author Vojislav Marinkovic
 */
public class MapAssert_isEqualTo_Test extends MapAssertBaseTest {

    private Map<String, Integer> expected = new HashMap<String, Integer>();
    @Override
    protected MapAssert<Object, Object> invoke_api_method() {
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        return assertions.isEqualTo(expected);
    }

    @Override
    protected void verify_internal_effects() {
        verify(maps).assertEqual(getInfo(assertions), getActual(assertions), expected, getObjects(assertions).getComparisonStrategy());
    }
}
