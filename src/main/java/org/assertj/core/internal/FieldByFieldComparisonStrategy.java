package org.assertj.core.internal;

/**
* Created by pragmatists on 12/19/13.
*/
public class FieldByFieldComparisonStrategy extends StandardComparisonStrategy {

    @Override
    public boolean areEqual(Object actual, Object other) {
        if(actual == null && other == null)
            return true;
        if(actual == null || other == null)
            return false;
        return actual.getClass().isInstance(other) && Objects.instance().areEqualToIgnoringGivenFields(actual,other);
    }
}
