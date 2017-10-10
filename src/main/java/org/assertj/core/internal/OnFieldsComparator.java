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
import static org.assertj.core.util.Arrays.isNullOrEmpty;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

public class OnFieldsComparator extends FieldByFieldComparator {

  private String[] fields;

  public OnFieldsComparator(Map<String, Comparator<?>> comparatorByPropertyOrField,
                            TypeComparators comparatorByType, String... fields) {
    super(comparatorByPropertyOrField, comparatorByType);
    checkArgument(!isNullOrEmpty(fields), "No fields/properties specified");
    for (String field : fields) {
      checkArgument(!isNullOrEmpty(field) && !isNullOrEmpty(field.trim()),
                    "Null/blank fields/properties are invalid, fields/properties were %s",
                    CONFIGURATION_PROVIDER.representation().toStringOf(fields));
    }
    this.fields = fields;
  }

  public OnFieldsComparator(String... fields) {
    this(new HashMap<String, Comparator<?>>(), new TypeComparators(), fields);
  }

  @VisibleForTesting
  public String[] getFields() {
    return fields;
  }

  @Override
  protected boolean areEqual(Object actualElement, Object otherElement) {
    try {
      return Objects.instance().areEqualToComparingOnlyGivenFields(actualElement, otherElement,
                                                                   comparatorByPropertyOrField, comparatorByType,
                                                                   fields);
    } catch (IntrospectionError e) {
      return false;
    }
  }

  @Override
  public String toString() {
    if (fields.length == 1) {
      return "single field/property comparator on field/property " + CONFIGURATION_PROVIDER.representation()
                                                                                           .toStringOf(fields[0]);
    }
    return "field/property by field/property comparator on fields/properties "
           + CONFIGURATION_PROVIDER.representation().toStringOf(fields);
  }

}
