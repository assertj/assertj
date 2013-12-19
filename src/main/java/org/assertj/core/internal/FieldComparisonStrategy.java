package org.assertj.core.internal;

public abstract class FieldComparisonStrategy extends StandardComparisonStrategy {

    @Override
    public boolean areEqual(Object actual, Object other) {
        if(actual == null && other == null)
            return true;
        if(actual == null || other == null)
            return false;
        return actual.getClass().isInstance(other) && areFieldsEqual(actual, other);
    }

    protected abstract boolean areFieldsEqual(Object actual, Object other);
}
