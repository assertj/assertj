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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.configuration;

import static org.assertj.core.presentation.BinaryRepresentation.BINARY_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.assertj.core.presentation.Representation;

class NonDefaultConfiguration extends Configuration {

  private static final SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("yyyy_MM_dd");
  private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy|MM|dd");

  @Override
  public Representation representation() {
    return BINARY_REPRESENTATION;
  }

  @Override
  public boolean bareNamePropertyExtractionEnabled() {
    return !super.bareNamePropertyExtractionEnabled();
  }

  @Override
  public boolean comparingPrivateFieldsEnabled() {
    return !super.comparingPrivateFieldsEnabled();
  }

  @Override
  public boolean extractingPrivateFieldsEnabled() {
    return !super.extractingPrivateFieldsEnabled();
  }

  @Override
  public int maxElementsForPrinting() {
    return super.maxElementsForPrinting() + 1;
  }

  @Override
  public int maxLengthForSingleLineDescription() {
    return super.maxLengthForSingleLineDescription() + 1;
  }

  @Override
  public boolean removeAssertJRelatedElementsFromStackTraceEnabled() {
    return !super.removeAssertJRelatedElementsFromStackTraceEnabled();
  }

  @Override
  public boolean lenientDateParsingEnabled() {
    return !super.lenientDateParsingEnabled();
  }

  @Override
  public List<DateFormat> additionalDateFormats() {
    return list(DATE_FORMAT1, DATE_FORMAT2);
  }
}