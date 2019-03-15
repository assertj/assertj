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

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.presentation.Representation;

/**
 * Provider for all the configuration settings / parameters within AssertJ.
 * <p>
 * All the configuration possibilities are registered via an SPI.
 *
 * @since 3.13.0
 */
public class Configuration {

  // default values
  public static final Configuration DEFAULT_CONFIGURATION = new Configuration();

  public static final int MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION = 80;
  public static final int MAX_ELEMENTS_FOR_PRINTING = 1000;
  public static final boolean REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE = true;
  public static final boolean ALLOW_COMPARING_PRIVATE_FIELDS = true;
  public static final boolean ALLOW_EXTRACTING_PRIVATE_FIELDS = true;
  public static final boolean BARE_NAME_PROPERTY_EXTRACTION_ENABLED = true;
  public static final boolean LENIENT_DATE_PARSING = false;

  /**
   * @return the default {@link Representation} that is used within AssertJ.
   */
  public Representation representation() {
    return STANDARD_REPRESENTATION;
  }

  boolean hasCustomRepresentation() {
    return representation() != STANDARD_REPRESENTATION;
  }

  /**
   * Returns whether private fields comparison is enabled. Default is {@value #ALLOW_COMPARING_PRIVATE_FIELDS}.
   * <p>
   * See {@link Assertions#setAllowComparingPrivateFields(boolean)} for a detailed description.
   *
   * @return whether private fields comparison is enabled. Default is {@value #ALLOW_COMPARING_PRIVATE_FIELDS}.
   */
  public boolean comparingPrivateFieldsEnabled() {
    return ALLOW_COMPARING_PRIVATE_FIELDS;
  }

  /**
   * Returns whether private fields comparison is enabled. Default is {@value #ALLOW_EXTRACTING_PRIVATE_FIELDS}.
   * <p>
   * See {@link Assertions#setAllowExtractingPrivateFields(boolean)} for a detailed description.
   *
   * @return whether private fields comparison is enabled. Default is {@value #ALLOW_EXTRACTING_PRIVATE_FIELDS}.
   *
   */
  public boolean extractingPrivateFieldsEnabled() {
    return ALLOW_EXTRACTING_PRIVATE_FIELDS;
  }

  /**
   * Returns whether the extractor considers bare-named property methods like {@code String name()}.
   * <p>
   * See {@link Assertions#setExtractBareNamePropertyMethods(boolean)} for a detailed description.
   *
   * @return Whether the extractor considers bare-named property methods like {@code String name()}. Default is {@value #BARE_NAME_PROPERTY_EXTRACTION_ENABLED}.
   */
  public boolean bareNamePropertyExtractionEnabled() {
    return true;
  }

  /**
   * Returns whether AssertJ related elements are removed from assertion errors stack trace.
   * <p>
   * See {@link Assertions#setRemoveAssertJRelatedElementsFromStackTrace(boolean)} for a detailed description.
   *
   * @return whether AssertJ related elements are removed from assertion errors stack trace. Default is {@value #REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE}.
   */
  public boolean removeAssertJRelatedElementsFromStackTraceEnabled() {
    return REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE;
  }

  /**
   * Returns whether AssertJ will use lenient parsing mode for default date formats.
   * <p>
   * See {@link Assertions#setLenientDateParsing(boolean)} for a detailed description.
   *
   * @return whether AssertJ will use lenient parsing mode for default date formats. Default is {@value #LENIENT_DATE_PARSING}.
   */
  public boolean lenientDateParsingEnabled() {
    return LENIENT_DATE_PARSING;
  }

  /**
   * Returns the additional date formats AssertJ will use in date assertions.
   * <p>
   * See {@link Assertions#registerCustomDateFormat(java.text.DateFormat)} for a detailed description.
   *
   * @return the additional date formats AssertJ will use in date assertions. Default is none.
   */
  public List<DateFormat> additionalDateFormats() {
    return emptyList();
  }

  /**
   * Returns the maximum length for an iterable/array to be displayed on one line.
   * <p>
   * See {@link Assertions#setMaxLengthForSingleLineDescription(int)} for a detailed description.
   *
   * @return the maximum length for an iterable/array to be displayed on one line. Default is {@value #MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION}.
   */
  public int maxLengthForSingleLineDescription() {
    return MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION;
  }

  /**
   * Returns the maximum length for an iterable/array to be displayed on one line.
   * <p>
   * See {@link Assertions#setMaxLengthForSingleLineDescription(int)} for a detailed description.
   *
   * @return the maximum length for an iterable/array to be displayed on one line. Default is {@value #MAX_ELEMENTS_FOR_PRINTING}.
   */
  public int maxElementsForPrinting() {
    return MAX_ELEMENTS_FOR_PRINTING;
  }

  /**
   * Applies this configuration to AssertJ.
   */
  public void apply() {
    Assertions.setAllowComparingPrivateFields(comparingPrivateFieldsEnabled());
    Assertions.setAllowExtractingPrivateFields(extractingPrivateFieldsEnabled());
    Assertions.setExtractBareNamePropertyMethods(bareNamePropertyExtractionEnabled());
    Assertions.setLenientDateParsing(lenientDateParsingEnabled());
    Assertions.setMaxElementsForPrinting(maxElementsForPrinting());
    Assertions.setMaxLengthForSingleLineDescription(maxLengthForSingleLineDescription());
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTraceEnabled());
    Assertions.useRepresentation(representation());
    additionalDateFormats().forEach(Assertions::registerCustomDateFormat);
  }

  public String describe() {
    return format("- representation .................................. = %s%n" +
                  "- comparingPrivateFieldsEnabled ................... = %s%n" +
                  "- extractingPrivateFieldsEnabled .................. = %s%n" +
                  "- bareNamePropertyExtractionEnabled ............... = %s%n" +
                  "- lenientDateParsingEnabled ....................... = %s%n" +
                  "- additionnal date formats ........................ = %s%n" +
                  "- maxLengthForSingleLineDescription ............... = %s%n" +
                  "- maxElementsForPrinting .......................... = %s%n" +
                  "- removeAssertJRelatedElementsFromStackTraceEnabled = %s%n",
                  representation(),
                  comparingPrivateFieldsEnabled(),
                  extractingPrivateFieldsEnabled(),
                  bareNamePropertyExtractionEnabled(),
                  lenientDateParsingEnabled(),
                  describeAdditionalDateFormats(),
                  maxLengthForSingleLineDescription(),
                  maxElementsForPrinting(),
                  removeAssertJRelatedElementsFromStackTraceEnabled());
  }

  private String describeAdditionalDateFormats() {
    return additionalDateFormats().stream()
                                  .map(this::describe)
                                  .collect(toList())
                                  .toString();
  }

  private String describe(DateFormat dateFormat) {
    return dateFormat instanceof SimpleDateFormat ? ((SimpleDateFormat) dateFormat).toPattern() : dateFormat.toString();
  }

}
