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
package org.assertj.core.configuration;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

/**
 * All configuration settings for AssertJ Core.
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
  public static final boolean PRINT_ASSERTIONS_DESCRIPTION_ENABLED = false;

  private boolean comparingPrivateFields = ALLOW_COMPARING_PRIVATE_FIELDS;
  private boolean extractingPrivateFields = ALLOW_EXTRACTING_PRIVATE_FIELDS;
  private boolean bareNamePropertyExtraction = BARE_NAME_PROPERTY_EXTRACTION_ENABLED;
  private boolean removeAssertJRelatedElementsFromStackTrace = REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE;
  private boolean lenientDateParsing = LENIENT_DATE_PARSING;
  private List<DateFormat> additionalDateFormats = emptyList();
  private int maxLengthForSingleLineDescription = MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION;
  private int maxElementsForPrinting = MAX_ELEMENTS_FOR_PRINTING;
  private boolean printAssertionsDescription = PRINT_ASSERTIONS_DESCRIPTION_ENABLED;
  private Consumer<Description> consumerDescription = null;

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
   * @return whether private fields comparison is enabled.
   */
  public boolean comparingPrivateFieldsEnabled() {
    return comparingPrivateFields;
  }

  /**
   * Sets whether private fields comparison is enabled.
   * <p>
   * See {@link Assertions#setAllowComparingPrivateFields(boolean)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param comparingPrivateFields whether private fields comparison is enabled.
   */
  public void setComparingPrivateFields(boolean comparingPrivateFields) {
    this.comparingPrivateFields = comparingPrivateFields;
  }

  /**
   * Returns whether private fields comparison is enabled. Default is {@value #ALLOW_EXTRACTING_PRIVATE_FIELDS}.
   * <p>
   * See {@link Assertions#setAllowExtractingPrivateFields(boolean)} for a detailed description.
   *
   * @return whether private fields comparison is enabled.
   */
  public boolean extractingPrivateFieldsEnabled() {
    return extractingPrivateFields;
  }

  /**
   * Sets whether private fields comparison is enabled.
   * <p>
   * See {@link Assertions#setAllowExtractingPrivateFields(boolean)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param extractingPrivateFields whether private fields comparison is enabled.
   */
  public void setExtractingPrivateFields(boolean extractingPrivateFields) {
    this.extractingPrivateFields = extractingPrivateFields;
  }

  /**
   * Returns whether the extractor considers bare-named property methods like {@code String name()}.
   * Default is {@value #BARE_NAME_PROPERTY_EXTRACTION_ENABLED}.
   * <p>
   * See {@link Assertions#setExtractBareNamePropertyMethods(boolean)} for a detailed description.
   *
   * @return whether the extractor considers bare-named property methods like {@code String name()}.
   */
  public boolean bareNamePropertyExtractionEnabled() {
    return bareNamePropertyExtraction;
  }

  /**
   * Sets whether the extractor considers bare-named property methods like {@code String name()}.
   * <p>
   * See {@link Assertions#setExtractBareNamePropertyMethods(boolean)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param bareNamePropertyExtraction whether the extractor considers bare-named property methods.
   */
  public void setBareNamePropertyExtraction(boolean bareNamePropertyExtraction) {
    this.bareNamePropertyExtraction = bareNamePropertyExtraction;
  }

  /**
   * Returns whether AssertJ related elements are removed from assertion errors stack trace.
   * Default is {@value #REMOVE_ASSERTJ_RELATED_ELEMENTS_FROM_STACK_TRACE}.
   * <p>
   * See {@link Assertions#setRemoveAssertJRelatedElementsFromStackTrace(boolean)} for a detailed description.
   *
   * @return whether AssertJ related elements are removed from assertion errors stack trace.
   */
  public boolean removeAssertJRelatedElementsFromStackTraceEnabled() {
    return removeAssertJRelatedElementsFromStackTrace;
  }

  /**
   * Returns whether AssertJ related elements are removed from assertion errors stack trace.
   * <p>
   * See {@link Assertions#setRemoveAssertJRelatedElementsFromStackTrace(boolean)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param removeAssertJRelatedElementsFromStackTrace whether AssertJ related elements are removed from assertion errors stack trace.
   */
  public void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    this.removeAssertJRelatedElementsFromStackTrace = removeAssertJRelatedElementsFromStackTrace;
  }

  /**
   * Returns whether AssertJ will use lenient parsing mode for default date formats.
   * Default is {@value #LENIENT_DATE_PARSING}.
   * <p>
   * See {@link Assertions#setLenientDateParsing(boolean)} for a detailed description.
   *
   * @return whether AssertJ will use lenient parsing mode for default date formats.
   */
  public boolean lenientDateParsingEnabled() {
    return lenientDateParsing;
  }

  /**
   * Returns whether AssertJ will use lenient parsing mode for default date formats.
   * <p>
   * See {@link Assertions#setLenientDateParsing(boolean)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param lenientDateParsing whether AssertJ will use lenient parsing mode for default date formats.
   */
  public void setLenientDateParsing(boolean lenientDateParsing) {
    this.lenientDateParsing = lenientDateParsing;
  }

  /**
   * AssertJ uses defaults date formats in date assertions, this property let's you register additional ones (default there are no additional date formats).
   * <p>
   * See {@link Assertions#registerCustomDateFormat(java.text.DateFormat)} for a detailed description.
   *
   * @return the date formats AssertJ will use in date assertions in addition the default ones.
   */
  public List<DateFormat> additionalDateFormats() {
    return additionalDateFormats;
  }

  /**
   * Returns the additional date formats AssertJ will use in date assertions.
   * <p>
   * See {@link Assertions#registerCustomDateFormat(java.text.DateFormat)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param additionalDateFormats the date formats AssertJ will use in date assertions in addition the default ones.
   */
  public void setAdditionalDateFormats(List<DateFormat> additionalDateFormats) {
    this.additionalDateFormats = additionalDateFormats;
  }

  /**
   * Add the given date formats AssertJ will use in date assertions.
   * <p>
   * See {@link Assertions#registerCustomDateFormat(java.text.DateFormat)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param additionalDateFormats the date formats AssertJ will use in date assertions in addition the default ones.
   */
  public void addAdditionalDateFormats(DateFormat... additionalDateFormats) {
    Stream.of(additionalDateFormats).forEach(this.additionalDateFormats::add);
  }

  /**
   * Returns the maximum length for an iterable/array to be displayed on one line.
   * Default is {@value #MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION}.
   * <p>
   * See {@link Assertions#setMaxLengthForSingleLineDescription(int)} for a detailed description.
   *
   * @return the maximum length for an iterable/array to be displayed on one line.
   */
  public int maxLengthForSingleLineDescription() {
    return maxLengthForSingleLineDescription;
  }

  /**
   * Sets the maximum length for an iterable/array to be displayed on one line.
   * <p>
   * See {@link Assertions#setMaxLengthForSingleLineDescription(int)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param maxLengthForSingleLineDescription the maximum length for an iterable/array to be displayed on one line.
   */
  public void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
    this.maxLengthForSingleLineDescription = maxLengthForSingleLineDescription;
  }

  /**
   * Returns the maximum length for an iterable/array to be displayed on one line.
   * Default is {@value #MAX_ELEMENTS_FOR_PRINTING}.
   * <p>
   * See {@link Assertions#setMaxLengthForSingleLineDescription(int)} for a detailed description.
   *
   * @return the maximum length for an iterable/array to be displayed on one line.
   */
  public int maxElementsForPrinting() {
    return maxElementsForPrinting;
  }

  /**
   * Returns the maximum number of elements that will be displayed for a group of values (iterables and arrays).
   * <p>
   * See {@link Assertions#setMaxElementsForPrinting(int)} for a detailed description.
   * <p>
   * Note that this change will only be effective once {@link #apply()} or {@link #applyAndDisplay()} is called.
   *
   * @param maxElementsForPrinting the maximum length for an iterable/array to be displayed on one line.
   */
  public void setMaxElementsForPrinting(int maxElementsForPrinting) {
    this.maxElementsForPrinting = maxElementsForPrinting;
  }

  public boolean printAssertionsDescription() {
    return printAssertionsDescription;
  }

  public void setPrintAssertionsDescriptionEnabled(boolean printAssertionsDescription) {
    this.printAssertionsDescription = printAssertionsDescription;
  }

  public Consumer<Description> consumerDescription() {
    return consumerDescription;
  }

  public void setConsumerDescription(Consumer<Description> consumerDescription) {
    this.consumerDescription = consumerDescription;
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
    Assertions.setConsumerDescription(consumerDescription());
    Assertions.setPrintAssertionsDescription(printAssertionsDescription());
    additionalDateFormats().forEach(Assertions::registerCustomDateFormat);
  }

  /**
   * Applies this configuration to AssertJ and prints it.
   */
  public void applyAndDisplay() {
    apply();
    System.out.println(describe());
  }

  public String describe() {
    return format("Applying configuration %s%n" +
                  "- representation .................................. = %s%n" +
                  "- comparingPrivateFieldsEnabled ................... = %s%n" +
                  "- extractingPrivateFieldsEnabled .................. = %s%n" +
                  "- bareNamePropertyExtractionEnabled ............... = %s%n" +
                  "- lenientDateParsingEnabled ....................... = %s%n" +
                  "- additional date formats ......................... = %s%n" +
                  "- maxLengthForSingleLineDescription ............... = %s%n" +
                  "- maxElementsForPrinting .......................... = %s%n" +
                  "- printAssertionsDescription ...................... = %s%n" +
                  "- consumerDescription ............................. = %s%n" +
                  "- removeAssertJRelatedElementsFromStackTraceEnabled = %s%n",
                  getClass().getName(),
                  representation(),
                  comparingPrivateFieldsEnabled(),
                  extractingPrivateFieldsEnabled(),
                  bareNamePropertyExtractionEnabled(),
                  lenientDateParsingEnabled(),
                  describeAdditionalDateFormats(),
                  maxLengthForSingleLineDescription(),
                  maxElementsForPrinting(),
                  printAssertionsDescription(),
                  consumerDescription(),
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
