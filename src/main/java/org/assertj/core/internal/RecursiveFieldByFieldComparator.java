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

import static org.assertj.core.internal.DeepDifference.determineDifferences;

import java.util.Comparator;
import java.util.Map;

import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Compares objects field/property by field/property recursively.
 */
public class RecursiveFieldByFieldComparator extends FieldByFieldComparator {

  public RecursiveFieldByFieldComparator(Map<String, Comparator<?>> comparatorByPropertyOrField,
                                         TypeComparators comparatorByType) {
    super(comparatorByPropertyOrField, comparatorByType);
  }

  @Override
  protected boolean areEqual(Object actual, Object other) {
    try {
      return determineDifferences(actual, other, comparatorsByPropertyOrField, comparatorsByType).isEmpty();
    } catch (IntrospectionError e) {
      return false;
    }
  }

  @Override
  protected String description() {
    return "recursive field/property by field/property comparator on all fields/properties";
  }
}
