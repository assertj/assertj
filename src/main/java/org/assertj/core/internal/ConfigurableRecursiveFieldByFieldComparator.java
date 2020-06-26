/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.internal.ComparatorBasedComparisonStrategy.NOT_EQUAL;

import java.util.Comparator;
import java.util.Objects;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonDifferenceCalculator;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Compares objects field/property by field/property recursively based on the given {@link RecursiveComparisonConfiguration} allowing fine tuning of the comparison.
 */
public class ConfigurableRecursiveFieldByFieldComparator implements Comparator<Object> {

  private RecursiveComparisonConfiguration configuration;
  private RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;

  // for testing
  ConfigurableRecursiveFieldByFieldComparator(RecursiveComparisonConfiguration configuration,
                                              RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator) {
    requireNonNull(configuration, "RecursiveComparisonConfiguration must not be null");
    this.configuration = configuration;
    this.recursiveComparisonDifferenceCalculator = recursiveComparisonDifferenceCalculator;
  }

  public ConfigurableRecursiveFieldByFieldComparator(RecursiveComparisonConfiguration configuration) {
    this(configuration, new RecursiveComparisonDifferenceCalculator());
  }

  @Override
  public int compare(Object actual, Object other) {
    if (actual == null && other == null) return 0;
    if (actual == null || other == null) return NOT_EQUAL;
    return areEqual(actual, other) ? 0 : NOT_EQUAL;
  }

  protected boolean areEqual(Object actual, Object other) {
    try {
      return recursiveComparisonDifferenceCalculator.determineDifferences(actual, other, configuration).isEmpty();
    } catch (@SuppressWarnings("unused") IntrospectionError e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return format("recursive field/property by field/property comparator on all fields/properties using the following configuration:%n%s",
                  configuration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configuration);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    ConfigurableRecursiveFieldByFieldComparator other = (ConfigurableRecursiveFieldByFieldComparator) obj;
    return Objects.equals(configuration, other.configuration);
  }

}
