package org.assertj.core.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldByFieldComparisonStrategy_asText_Test {

    @Test
    public void should_return_comparison_strategy_description() throws Exception {
        assertEquals("when comparing elements field by field (on all field)\n", new FieldByFieldComparisonStrategy().asText());
    }

}