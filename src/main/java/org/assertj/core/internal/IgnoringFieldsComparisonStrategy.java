package org.assertj.core.internal;

import org.assertj.core.util.VisibleForTesting;

public class IgnoringFieldsComparisonStrategy extends FieldComparisonStrategy {

    private String[] fields;

    public IgnoringFieldsComparisonStrategy(String... fields) {
        this.fields = fields;
    }

    @Override
    protected boolean areFieldsEqual(Object actual, Object other) {
        return Objects.instance().areEqualToIgnoringGivenFields(actual, other, fields);
    }

    @VisibleForTesting
    public String[] getFields() {
        return fields;
    }
}
