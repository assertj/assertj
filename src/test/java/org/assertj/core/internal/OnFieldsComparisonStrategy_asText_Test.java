package org.assertj.core.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OnFieldsComparisonStrategy_asText_Test {

    @Test
    public void should_return_comparison_strategy_description() throws Exception {
        assertEquals("when comparing elements field by field on the following fields only : telling, thinking\n",
                new OnFieldsComparisonStrategy("telling","thinking").asText());
    }

}