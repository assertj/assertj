package org.assertj.core.internal;

import org.assertj.core.util.VisibleForTesting;

public class OnFieldsComparisonStrategy extends FieldComparisonStrategy {

    private String[] fieldsNames;

    public OnFieldsComparisonStrategy(String... fieldsNames) {
        this.fieldsNames = fieldsNames;
    }

    @Override
    protected boolean areFieldsEqual(Object actual, Object other) {
        return Objects.instance().areEqualToComparingOnlyGivenFields(actual, other, fieldsNames);
    }

    @VisibleForTesting
    public String[] getFieldsNames() {
        return fieldsNames;
    }
}