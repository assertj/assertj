/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import java.util.Comparator;
import java.util.Map;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

public class IgnoringFieldsComparator extends FieldByFieldComparator {

  private String[] fields;

  public IgnoringFieldsComparator(Map<String, Comparator<?>> comparatorByPropertyOrField,
                                  TypeComparators comparatorByType, String... fields) {
    super(comparatorByPropertyOrField, comparatorByType);
    this.fields = fields;
  }

  public IgnoringFieldsComparator(String... fields) {
    super();
    this.fields = fields;
  }

  @VisibleForTesting
  public String[] getFields() {
	return fields;
  }

  @Override
  protected boolean areEqual(Object actualElement, Object otherElement) {
    try {
      return Objects.instance().areEqualToIgnoringGivenFields(actualElement, otherElement, comparatorByPropertyOrField,
                                                              comparatorByType, fields);
    } catch (IntrospectionError e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return "field/property by field/property comparator on all fields/properties except "
           + CONFIGURATION_PROVIDER.representation().toStringOf(fields);
  }
}
