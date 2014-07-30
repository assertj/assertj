package org.assertj.core.api.iterable;

public interface Extractor<F, T> {
  T extract(F input);
}
