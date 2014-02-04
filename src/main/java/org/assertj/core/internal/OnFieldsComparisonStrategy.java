package org.assertj.core.internal;

import org.assertj.core.util.VisibleForTesting;

public class OnFieldsComparisonStrategy extends FieldComparisonStrategy {

    private String[] fields;

    public OnFieldsComparisonStrategy(String... fields) {
        this.fields = fields;
    }

    @Override
    protected boolean areFieldsEqual(Object actual, Object other) {
        return Objects.instance().areEqualToComparingOnlyGivenFields(actual, other, fields);
    }

    @VisibleForTesting
    public String[] getFields() {
        return fields;
    }

    @Override
    public String asText() {
        return "when comparing elements field by field on the following fields only : " + fieldsAsText() + "\n";
    }

    private String fieldsAsText() {
        return org.assertj.core.util.Strings.join(fields).with(", ");
    }
}