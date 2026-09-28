package org.assertj.core.api.recursive.comparison;

import java.util.Map;

// not using Entry because as a jdk type, it would be compared with equals and not recursively
// no need to parameterized KeyValue as the recursive comparison ignores type parameters.
record KeyValue(Object key, Object value) {

  // regular getters to be compatible with getter driven recursive comparison
  public Object getKey() {
    return key;
  }

  public Object getValue() {
    return value;
  }

  static KeyValue from(Map.Entry<?, ?> entry) {
    return new KeyValue(entry.getKey(), entry.getValue());
  }
}
