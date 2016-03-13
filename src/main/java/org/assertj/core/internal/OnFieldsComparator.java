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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Collections.EMPTY_MAP;
import static org.assertj.core.util.Arrays.isNullOrEmpty;
import static org.assertj.core.util.Strings.isNullOrEmpty;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

public class OnFieldsComparator extends FieldByFieldComparator {

  private final static StandardRepresentation REPRESENTATION = new StandardRepresentation();

  private String[] fields;

  public OnFieldsComparator(String... fields) {
	if (isNullOrEmpty(fields)) throw new IllegalArgumentException("No fields specified");
	for (String field : fields) {
	  if (isNullOrEmpty(field) || isNullOrEmpty(field.trim()))
		throw new IllegalArgumentException("Null/blank fields are invalid, fields were "
		                                   + REPRESENTATION.toStringOf(fields));
	}
	this.fields = fields;
  }

  @VisibleForTesting
  public String[] getFields() {
	return fields;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected boolean areEqual(Object actualElement, Object otherElement) {
    try {
      return Objects.instance().areEqualToComparingOnlyGivenFields(actualElement, otherElement, EMPTY_MAP, EMPTY_MAP, fields);
    } catch (IntrospectionError e) {
      return false;
    }
  }

  @Override
  public String toString() {
	if (fields.length == 1) return "single field comparator on field " + REPRESENTATION.toStringOf(fields[0]);
	return "field by field comparator on fields " + REPRESENTATION.toStringOf(fields);
  }

}
