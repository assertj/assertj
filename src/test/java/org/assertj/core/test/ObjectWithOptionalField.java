package org.assertj.core.test;

import java.util.Optional;

public class ObjectWithOptionalField {
    private Optional<String> optString;

    public ObjectWithOptionalField(Optional<String> o) {
      optString = o;
    }

    public Optional<String> getOptString() {
      return optString;
    }
}
