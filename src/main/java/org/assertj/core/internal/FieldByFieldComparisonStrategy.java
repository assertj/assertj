package org.assertj.core.internal;

public class FieldByFieldComparisonStrategy extends FieldComparisonStrategy {

    @Override
    protected boolean areFieldsEqual(Object actual, Object other) {
        return Objects.instance().areEqualToIgnoringGivenFields(actual,other);
    }
}
