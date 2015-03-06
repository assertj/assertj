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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.VisibleForTesting;

public class IgnoringFieldsComparator extends FieldByFieldComparator {

  private String[] fields;
  private final static StandardRepresentation REPRESENTATION = new StandardRepresentation();

  public IgnoringFieldsComparator(String... fields) {
      this.fields = fields;
  }
  
  @VisibleForTesting
  public String[] getFields() {
	return fields;
  }
  
  @Override
  protected boolean areEqual(Object actualElement, Object otherElement) {
    return Objects.instance().areEqualToIgnoringGivenFields(actualElement, otherElement, fields);
  }
  
  @Override
  public String toString() {
	return "field by field comparator on all fields except " + REPRESENTATION.toStringOf(fields);
  }
}
