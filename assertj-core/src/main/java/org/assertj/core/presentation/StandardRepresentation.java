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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.Integer.toHexString;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Arrays.isArrayTypePrimitive;
import static org.assertj.core.util.Arrays.isObjectArray;
import static org.assertj.core.util.Arrays.notAnArrayOfPrimitives;
import static org.assertj.core.util.DateUtil.formatAsDatetime;
import static org.assertj.core.util.DateUtil.formatAsDatetimeWithMs;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.util.Strings.quote;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;

import org.assertj.core.configuration.Configuration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.data.MapEntry;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.Closeables;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.ChangeDelta;
import org.assertj.core.util.diff.DeleteDelta;
import org.assertj.core.util.diff.InsertDelta;

/**
 * Standard java object representation.
 *
 * @author Mariusz Smykula
 * @author Jack Gough
 */
public class StandardRepresentation implements Representation {

  private static final String NULL = "null";

  // can share this as StandardRepresentation has no state
  public static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  private static final String TUPLE_START = "(";
  private static final String TUPLE_END = ")";

  private static final String DEFAULT_START = "[";
  private static final String DEFAULT_END = "]";

  private static final String DEFAULT_MAX_ELEMENTS_EXCEEDED = "...";

  // 4 spaces indentation : 2 space indentation after new line + '<' + '['
  static final String INDENTATION_AFTER_NEWLINE = "    ";
  // used when formatting iterables to a single line
  static final String INDENTATION_FOR_SINGLE_LINE = " ";

  public static final String ELEMENT_SEPARATOR = ",";
  public static final String ELEMENT_SEPARATOR_WITH_NEWLINE = ELEMENT_SEPARATOR + System.lineSeparator();

  private static int maxLengthForSingleLineDescription = Configuration.MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION;
  private static int maxElementsForPrinting = Configuration.MAX_ELEMENTS_FOR_PRINTING;
  private static int maxStackTraceElementsDisplayed = Configuration.MAX_STACKTRACE_ELEMENTS_DISPLAYED;

  private static final Map<Class<?>, Function<?, ? extends CharSequence>> customFormatterByType = new HashMap<>();
  private static final Class<?>[] TYPE_WITH_UNAMBIGUOUS_REPRESENTATION = { Date.class, LocalDateTime.class, ZonedDateTime.class,
      OffsetDateTime.class, Calendar.class };

  // Iterable types that should be considered to be unsafe to dereference and iterate across (e.g. they may have
  // visible side effects).
  private static final Class<?>[] BLACKLISTED_ITERABLE_CLASSES = {
      // DirectoryStream implementations can choose to only provide a single-use iterator once across their contents.
      // This means we should not try to iterate across them in their representation as this can cause unwanted
      // side effects in test cases.
      DirectoryStream.class,
  };

  protected enum GroupType {
    ITERABLE("iterable"), ARRAY("array");

    private String description;

    GroupType(String value) {
      this.description = value;
    }

    public String description() {
      return description;
    }

  }

  /**
   * It resets the static defaults for the standard representation.
   * <p>
   * The following defaults will be reapplied:
   * <ul>
   *   <li>{@code maxLengthForSingleLineDescription} = {@value org.assertj.core.configuration.Configuration#MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION} </li>
   *   <li>{@code maxElementsForPrinting} = {@value org.assertj.core.configuration.Configuration#MAX_ELEMENTS_FOR_PRINTING} </li>
   * </ul>
   */
  public static void resetDefaults() {
    maxLengthForSingleLineDescription = Configuration.MAX_LENGTH_FOR_SINGLE_LINE_DESCRIPTION;
    maxElementsForPrinting = Configuration.MAX_ELEMENTS_FOR_PRINTING;
  }

  public static void setMaxLengthForSingleLineDescription(int value) {
    ConfigurationProvider.loadRegisteredConfiguration();
    checkArgument(value > 0, "maxLengthForSingleLineDescription must be > 0 but was %s", value);
    maxLengthForSingleLineDescription = value;
  }

  @VisibleForTesting
  public static int getMaxLengthForSingleLineDescription() {
    return maxLengthForSingleLineDescription;
  }

  public static void setMaxElementsForPrinting(int value) {
    ConfigurationProvider.loadRegisteredConfiguration();
    checkArgument(value >= 1, "maxElementsForPrinting must be >= 1, but was %s", value);
    maxElementsForPrinting = value;
  }

  @VisibleForTesting
  public static int getMaxStackTraceElementsDisplayed() {
    return maxStackTraceElementsDisplayed;
  }

  public static void setMaxStackTraceElementsDisplayed(int value) {
    ConfigurationProvider.loadRegisteredConfiguration();
    checkArgument(value >= 0, "maxStackTraceElementsDisplayed  must be >= 0, but was %s", value);
    maxStackTraceElementsDisplayed = value;
  }

  @VisibleForTesting
  public static int getMaxElementsForPrinting() {
    return maxElementsForPrinting;
  }

  /**
   * Registers new formatter for the given type. All instances of the given type will be formatted with the provided formatter.
   *
   * @param <T> the type to register a formatter for
   * @param type the class of the type to register a formatter for
   * @param formatter the formatter
   */
  public static <T> void registerFormatterForType(Class<T> type, Function<T, String> formatter) {
    customFormatterByType.put(type, formatter);
  }

  /**
   * Clear all formatters registered per type with {@link #registerFormatterForType(Class, Function)}.
   */
  public static void removeAllRegisteredFormatters() {
    customFormatterByType.clear();
  }

  /**
   * Returns standard the {@code toString} representation of the given object. It may or not the object's own
   * implementation of {@code toString}.
   *
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (object == null) return null;
    if (hasCustomFormatterFor(object)) return customFormat(object);
    if (object instanceof ComparatorBasedComparisonStrategy) return toStringOf((ComparatorBasedComparisonStrategy) object);
    if (object instanceof Calendar) return toStringOf((Calendar) object);
    if (object instanceof Class<?>) return toStringOf((Class<?>) object);
    if (object instanceof Date) return toStringOf((Date) object);
    if (object instanceof Duration) return toStringOf((Duration) object);
    if (object instanceof LocalDate) return toStringOf((LocalDate) object);
    if (object instanceof YearMonth) return toStringOf((YearMonth) object);
    if (object instanceof LocalDateTime) return toStringOf((LocalDateTime) object);
    if (object instanceof OffsetDateTime) return toStringOf((OffsetDateTime) object);
    if (object instanceof ZonedDateTime) return toStringOf((ZonedDateTime) object);
    if (object instanceof LongAdder) return toStringOf((LongAdder) object);
    // if object was a subtype of any atomic type overriding toString, use it as it's more relevant than our generic
    // representation, if that's not the case (e.g. an AtomicReference subclass not overriding String) we use our representation.
    if (isInstanceOfNotOverridingToString(object, AtomicReference.class)) return toStringOf((AtomicReference<?>) object);
    if (isInstanceOfNotOverridingToString(object, AtomicMarkableReference.class))
      return toStringOf((AtomicMarkableReference<?>) object);
    if (isInstanceOfNotOverridingToString(object, AtomicStampedReference.class))
      return toStringOf((AtomicStampedReference<?>) object);
    if (object instanceof AtomicIntegerFieldUpdater) return AtomicIntegerFieldUpdater.class.getSimpleName();
    if (object instanceof AtomicLongFieldUpdater) return AtomicLongFieldUpdater.class.getSimpleName();
    if (object instanceof AtomicReferenceFieldUpdater) return AtomicReferenceFieldUpdater.class.getSimpleName();
    if (object instanceof File) return toStringOf((File) object);
    if (object instanceof Path) return fallbackToStringOf(object);
    if (isUnquotedString(object)) return toUnquotedStringOf(object);
    if (object instanceof String) return toStringOf((String) object);
    if (object instanceof CharSequence) return toStringOf((CharSequence) object);
    if (object instanceof Character) return toStringOf((Character) object);
    if (object instanceof Comparator) return toStringOf((Comparator<?>) object);
    if (object instanceof SimpleDateFormat) return toStringOf((SimpleDateFormat) object);
    if (object instanceof PredicateDescription) return toStringOf((PredicateDescription) object);
    if (object instanceof Future) return toStringOf((Future<?>) object);
    if (isArray(object)) return formatArray(object);
    if (object instanceof Collection<?>) return smartFormat((Collection<?>) object);
    if (object instanceof Map<?, ?>) return toStringOf((Map<?, ?>) object);
    if (object instanceof Tuple) return toStringOf((Tuple) object);
    if (object instanceof Map.Entry) return toStringOf((Map.Entry<?, ?>) object);
    if (object instanceof Method) return ((Method) object).toGenericString();
    if (object instanceof InsertDelta<?>) return toStringOf((InsertDelta<?>) object);
    if (object instanceof ChangeDelta<?>) return toStringOf((ChangeDelta<?>) object);
    if (object instanceof DeleteDelta<?>) return toStringOf((DeleteDelta<?>) object);
    // Only format Iterables that are not collections and have not overridden toString
    // ex: JsonNode is an Iterable that is best formatted with its own String
    // Path is another example, but we can deal with it specifically as it is part of the JDK.
    if (object instanceof Iterable<?> && !hasOverriddenToString(object.getClass())) return smartFormat((Iterable<?>) object);
    if (object instanceof AtomicInteger) return toStringOf((AtomicInteger) object);
    if (object instanceof AtomicBoolean) return toStringOf((AtomicBoolean) object);
    if (object instanceof AtomicLong) return toStringOf((AtomicLong) object);
    if (object instanceof Number) return toStringOf((Number) object);
    if (object instanceof Throwable) return toStringOf((Throwable) object);
    return fallbackToStringOf(object);
  }

  private static boolean isUnquotedString(Object object) {
    String className = object.getClass().getName();
    return className.contains("org.assertj.core") && className.contains("UnquotedString");
  }

  private static boolean isInstanceOfNotOverridingToString(Object object, Class<?> type) {
    return type.isInstance(object) && !hasOverriddenToStringInSubclassOf(object.getClass(), type);
  }

  private static boolean hasOverriddenToString(Class<?> clazz) {
    try {
      Class<?> classDeclaringToString = clazz.getMethod("toString").getDeclaringClass();
      return !Object.class.equals(classDeclaringToString);
    } catch (NoSuchMethodException | SecurityException e) {
      // NoSuchMethodException should not occur as toString is always defined.
      // if SecurityException occurs, returning false to use our own representation
      return false;
    }
  }

  // this method assumes that objectClass is a subclass of clazz, it checks that toString is not
  // declared in clazz or any superclass of clazz.
  // this typically used to check whether an AtomicReference subclass has overridden toString.
  private static boolean hasOverriddenToStringInSubclassOf(Class<?> objectClass, Class<?> clazz) {
    try {
      Class<?> classDeclaringToString = objectClass.getMethod("toString").getDeclaringClass();
      // check if any classes between objectClass and clazz (excluded) have overridden toString
      Class<?> classToCheck = objectClass;
      while (!classToCheck.equals(clazz)) {
        if (classDeclaringToString.equals(classToCheck)) return true;
        classToCheck = classToCheck.getSuperclass();
      }
    } catch (NoSuchMethodException | SecurityException e) {
      // NoSuchMethodException should not occur as toString is always defined.
      // if SecurityException occurs, returning false to use our own representation
    }
    return false;
  }

  /**
   * Returns the {@code String} representation of the given object with its type and hexadecimal identity hash code so that
   * it can be differentiated from other objects with the same {@link #toStringOf(Object)} representation.
   *
   * @param obj the object to represent.
   * @return the unambiguous {@code toString} representation of the given object.
   */
  @Override
  public String unambiguousToStringOf(Object obj) {
    // some types have already an unambiguous toString, no need to double down
    if (hasAlreadyAnUnambiguousToStringOf(obj)) return toStringOf(obj);
    return obj == null ? null : String.format("%s (%s@%s)", toStringOf(obj), classNameOf(obj), identityHexCodeOf(obj));
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }

  protected <T> String customFormat(T object) {
    if (object == null) return null;
    CharSequence formatted = ((Function<T, ? extends CharSequence>) customFormatterByType.get(object.getClass())).apply(object);
    return formatted != null ? formatted.toString() : null;
  }

  protected boolean hasCustomFormatterFor(Object object) {
    if (object == null) return false;
    return customFormatterByType.containsKey(object.getClass());
  }

  /**
   * Determine whether the given object's type has a representation that is not ambiguous.
   * @param obj the object to check
   * @return true if the given object's type has a representation that is not ambiguous, false otherwise.
   */
  // not static so that it can be overridden
  protected boolean hasAlreadyAnUnambiguousToStringOf(Object obj) {
    for (Class<?> aClass : TYPE_WITH_UNAMBIGUOUS_REPRESENTATION) {
      if (aClass.isInstance(obj)) return true;
    }
    return false;
  }

  /**
   * Returns the {@code String} representation of the given object. This method is used as a last resort if none of
   * the {@link StandardRepresentation} predefined string representations were not called.
   *
   * @param object the object to represent (never {@code null}
   * @return to {@code toString} representation for the given object
   */
  protected String fallbackToStringOf(Object object) {
    return object.toString();
  }

  protected String toStringOf(Number number) {
    if (number instanceof Float) return toStringOf((Float) number);
    if (number instanceof Long) return toStringOf((Long) number);
    // fallback to default formatting
    return number.toString();
  }

  protected String toStringOf(AtomicBoolean atomicBoolean) {
    return String.format("AtomicBoolean(%s)", atomicBoolean.get());
  }

  protected String toStringOf(AtomicInteger atomicInteger) {
    return String.format("AtomicInteger(%s)", atomicInteger.get());
  }

  protected String toStringOf(AtomicLong atomicLong) {
    return String.format("AtomicLong(%s)", atomicLong.get());
  }

  protected String toStringOf(LongAdder longAdder) {
    return String.format("LongAdder(%s)", longAdder.sum());
  }

  protected String toStringOf(Comparator<?> comparator) {
    if (!comparator.toString().contains("@")) return comparator.toString();
    String comparatorSimpleClassName = comparator.getClass().getSimpleName();
    if (comparatorSimpleClassName.isEmpty()) return quote("anonymous comparator class");
    // if toString has not been redefined, let's use comparator simple class name.
    if (comparator.toString().contains(comparatorSimpleClassName + "@")) return comparatorSimpleClassName;
    return comparator.toString();
  }

  protected String toStringOf(ComparatorBasedComparisonStrategy comparatorBasedComparisonStrategy) {
    String comparatorDescription = comparatorBasedComparisonStrategy.getComparatorDescription();
    return comparatorDescription == null ? toStringOf(comparatorBasedComparisonStrategy.getComparator())
        : quote(comparatorDescription);
  }

  protected String toStringOf(Calendar calendar) {
    return formatAsDatetime(calendar) + classNameDisambiguation(calendar);
  }

  protected String toStringOf(Class<?> c) {
    String canonicalName = c.getCanonicalName();
    if (canonicalName != null) return canonicalName;
    if (c.getSimpleName().isEmpty()) return "anonymous class";
    if (c.getSimpleName().equals("[]")) return "anonymous class array";
    return String.format("local class %s", c.getSimpleName());
  }

  protected String toStringOf(String s) {
    return concatWithDoubleQuotes(s);
  }

  protected String toUnquotedStringOf(Object s) {
    return s.toString();
  }

  protected String toStringOf(CharSequence s) {
    return concatWithDoubleQuotes(s);
  }

  private static String concatWithDoubleQuotes(CharSequence s) {
    return concat("\"", s, "\"");
  }

  protected String toStringOf(Character c) {
    return concat("'", c, "'");
  }

  protected String toStringOf(PredicateDescription p) {
    // don't enclose default description with ''
    return p.isDefault() ? String.format("%s", p.description) : String.format("'%s'", p.description);
  }

  protected String toStringOf(Date date) {
    return formatAsDatetimeWithMs(date) + classNameDisambiguation(date);
  }

  protected String toStringOf(LocalDateTime localDateTime) {
    return defaultToStringWithClassNameDisambiguation(localDateTime);
  }

  protected String toStringOf(OffsetDateTime offsetDateTime) {
    return defaultToStringWithClassNameDisambiguation(offsetDateTime);
  }

  protected String toStringOf(ZonedDateTime zonedDateTime) {
    return defaultToStringWithClassNameDisambiguation(zonedDateTime);
  }

  protected String toStringOf(LocalDate localDate) {
    return defaultToStringWithClassNameDisambiguation(localDate);
  }

  protected String toStringOf(YearMonth yearMonth) {
    return defaultToStringWithClassNameDisambiguation(yearMonth);
  }

  protected String classNameDisambiguation(Object o) {
    return String.format(" (%s)", o.getClass().getName());
  }

  protected String toStringOf(Float f) {
    return String.format("%sf", f);
  }

  protected String toStringOf(Long l) {
    return String.format("%sL", l);
  }

  protected String toStringOf(File file) {
    return file.getAbsolutePath();
  }

  protected String toStringOf(SimpleDateFormat dateFormat) {
    return dateFormat.toPattern();
  }

  protected String toStringOf(Future<?> future) {
    String className = future.getClass().getSimpleName();
    if (!future.isDone()) return concat(className, "[Incomplete]");
    try {
      Object joinResult = future.get();
      // avoid stack overflow error if future join on itself or another future that cycles back to the first
      Object joinResultRepresentation = joinResult instanceof Future ? joinResult : toStringOf(joinResult);
      return concat(className, "[Completed: ", joinResultRepresentation, "]");
    } catch (CancellationException e) {
      return concat(className, "[Cancelled]");
    } catch (InterruptedException e) {
      return concat(className, "[Interrupted]");
    } catch (ExecutionException e) {
      // get the stack trace of the cause (if any) to avoid polluting it with the exception from trying to join the future
      String stackTrace = e.getCause() != null ? getStackTrace(e.getCause()) : getStackTrace(e);
      return concat(className, "[Failed with the following stack trace:", String.format("%n%s", stackTrace), "]");
    }
  }

  protected String toStringOf(Tuple tuple) {
    return singleLineFormat(tuple.toList(), TUPLE_START, TUPLE_END);
  }

  protected String toStringOf(MapEntry<?, ?> mapEntry) {
    return String.format("%s=%s", toStringOf(mapEntry.key), toStringOf(mapEntry.value));
  }

  protected String toStringOf(Entry<?, ?> javaMapEntry) {
    return String.format("%s=%s", toStringOf(javaMapEntry.getKey()), toStringOf(javaMapEntry.getValue()));
  }

  protected String toStringOf(Map<?, ?> map) {
    if (map == null) return null;
    Map<?, ?> sortedMap = toSortedMapIfPossible(map);
    Iterator<?> entriesIterator = sortedMap.entrySet().iterator();
    if (!entriesIterator.hasNext()) return "{}";
    StringBuilder builder = new StringBuilder("{");
    int printedElements = 0;
    for (;;) {
      Entry<?, ?> entry = (Entry<?, ?>) entriesIterator.next();
      if (printedElements == maxElementsForPrinting) {
        builder.append(DEFAULT_MAX_ELEMENTS_EXCEEDED);
        return builder.append("}").toString();
      }

      // the entry shouldn't be null in a valid map, but if it is, print it out gracefully instead of throwing a NPE
      if (entry == null) {
        builder.append("null");
      } else {
        builder.append(format(map, entry.getKey())).append('=').append(format(map, entry.getValue()));
      }

      printedElements++;
      if (!entriesIterator.hasNext()) return builder.append("}").toString();
      builder.append(", ");
    }
  }

  protected String toStringOf(Throwable throwable) {
    StackTraceElement[] elements = throwable.getStackTrace();
    // if the line limit is 0, we assume the user don't want to print stack trace
    // the null check is for user convenience when they mock throwable (otherwise elements is not nul)
    if (maxStackTraceElementsDisplayed == 0 || elements == null) return throwable.toString();
    // display the full stack trace
    if (maxStackTraceElementsDisplayed >= elements.length) return getStackTrace(throwable);

    // display a partial stack trace
    StringWriter sw = null;
    PrintWriter pw = null;
    try {
      sw = new StringWriter();
      pw = new PrintWriter(sw, true);
      pw.println(throwable);
      for (int i = 0; i < maxStackTraceElementsDisplayed; i++) {
        pw.println("\tat " + elements[i]);
      }
      pw.print("\t...(" + (elements.length - maxStackTraceElementsDisplayed)
               + " remaining lines not displayed - this can be changed with Assertions.setMaxStackTraceElementsDisplayed)");
      return sw.toString();
    } finally {
      Closeables.closeQuietly(sw, pw);
    }
  }

  protected String toStringOf(AtomicReference<?> atomicReference) {
    return String.format("%s[%s]", atomicReference.getClass().getSimpleName(), toStringOf(atomicReference.get()));
  }

  protected String toStringOf(AtomicMarkableReference<?> atomicMarkableReference) {
    return String.format("%s[marked=%s, reference=%s]", atomicMarkableReference.getClass().getSimpleName(),
                         atomicMarkableReference.isMarked(),
                         toStringOf(atomicMarkableReference.getReference()));
  }

  protected String toStringOf(AtomicStampedReference<?> atomicStampedReference) {
    return String.format("%s[stamp=%s, reference=%s]", atomicStampedReference.getClass().getSimpleName(),
                         atomicStampedReference.getStamp(),
                         toStringOf(atomicStampedReference.getReference()));
  }

  protected String multiLineFormat(Iterable<?> iterable) {
    return format(iterable, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR_WITH_NEWLINE, INDENTATION_AFTER_NEWLINE, iterable);
  }

  protected String singleLineFormat(Iterable<?> iterable, String start, String end) {
    return format(iterable, start, end, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE, iterable);
  }

  /**
   * Returns the {@code String} representation of the given {@code Iterable}, or {@code null} if the given
   * {@code Iterable} is {@code null}.
   * <p>
   * The {@code Iterable} will be formatted to a single line if it does not exceed 100 char, otherwise each element
   * will be formatted on a new line with 4 space indentation.
   *
   * @param iterable the {@code Iterable} to format.
   * @return the {@code String} representation of the given {@code Iterable}.
   */
  protected String smartFormat(Iterable<?> iterable) {
    for (Class<?> blacklistedClass : BLACKLISTED_ITERABLE_CLASSES) {
      if (blacklistedClass.isInstance(iterable)) {
        return fallbackToStringOf(iterable);
      }
    }

    String singleLineDescription = singleLineFormat(iterable, DEFAULT_START, DEFAULT_END);
    return doesDescriptionFitOnSingleLine(singleLineDescription) ? singleLineDescription : multiLineFormat(iterable);
  }

  /**
   * Returns the {@code String} representation of the given array, or {@code null} if the given object is either
   * {@code null} or not an array. This method supports arrays having other arrays as elements.
   *
   * @param o the object that is expected to be an array.
   * @return the {@code String} representation of the given array.
   */
  protected String formatArray(Object o) {
    if (!isArray(o)) return null;
    return isObjectArray(o) ? smartFormat((Object[]) o) : formatPrimitiveArray(o);
  }

  protected String smartFormat(Object[] array) {
    String description = singleLineFormat(array, array);
    return doesDescriptionFitOnSingleLine(description) ? description : multiLineFormat(array, array);
  }

  protected String formatPrimitiveArray(Object o) {
    if (!isArrayTypePrimitive(o)) throw notAnArrayOfPrimitives(o);
    List<Object> objects = new PrimitiveArrayList(o);
    return format(objects, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE, objects);
  }

  protected String multiLineFormat(Object[] array, Object root) {
    return format(array, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR_WITH_NEWLINE, INDENTATION_AFTER_NEWLINE, root);
  }

  protected String singleLineFormat(Object[] array, Object root) {
    return format(array, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE, root);
  }

  protected String format(Object[] array, String start, String end, String elementSeparator, String indentation, Object root) {
    if (array == null) return null;
    // root is used to avoid infinite recursion in case one element refers to it.
    return format(java.util.Arrays.asList(array), start, end, elementSeparator, indentation, root);
  }

  protected String format(List<?> elements, String start, String end, String elementSeparator, String indentation,
                          Object root) {
    if (elements == null) return null;
    if (elements.isEmpty()) return start + end;
    List<String> representedElements = new TransformingList<>(elements, elem -> safeStringOf(elem, start, end, elementSeparator,
                                                                                             indentation, root));
    return representGroup(representedElements, start, end, elementSeparator, indentation);
  }

  protected String format(Iterable<?> iterable, String start, String end, String elementSeparator, String indentation,
                          Object root) {
    if (iterable == null) return null;
    Iterator<?> iterator = iterable.iterator();
    if (!iterator.hasNext()) return start + end;
    List<String> representedElements = representElements(iterable, start, end, elementSeparator, indentation, root);
    return representGroup(representedElements, start, end, elementSeparator, indentation);
  }

  protected String safeStringOf(Object element, String start, String end, String elementSeparator, String indentation,
                                Object root) {
    if (element == root) return isArray(root) ? "(this array)" : "(this instance)";
    // Since potentially self referencing containers have been handled, it is reasonably safe to use toStringOf.
    // What we don't track are cycles like A -> B -> A but that should be rare enough thus this solution is good enough
    // To fully avoid all cycles we would need to track all visited elements but the issue is that:
    // List<Object> innerList = list(1, 2, 3);
    // List<Object> outerList = list(innerList, innerList);
    // outerList would be represented as [[1, 2, 3], (already visited)] instead of [[1, 2, 3], [1, 2, 3]]
    // Final word, the approach used here is the same as the toString implementation in AbstractCollection
    return element == null ? NULL : toStringOf(element);
  }

  // private methods

  private List<String> representElements(Iterable<?> elements, String start, String end, String elementSeparator,
                                         String indentation, Object root) {
    int capacity = maxElementsForPrinting / 2 + 1;
    HeadTailAccumulator<Object> accumulator = new HeadTailAccumulator<>(capacity, capacity);
    elements.forEach(accumulator::add);

    return accumulator.stream().map(element -> safeStringOf(element, start, end, elementSeparator, indentation, root))
                      .collect(toList());
  }

  // this method only deals with max number of elements to display, the elements representation is already computed
  private static String representGroup(List<String> representedElements, String start, String end, String elementSeparator,
                                       String indentation) {
    int size = representedElements.size();
    StringBuilder desc = new StringBuilder(start);
    if (size <= maxElementsForPrinting) {
      // display all elements
      for (int i = 0; i < size; i++) {
        if (i != 0) desc.append(indentation);
        desc.append(representedElements.get(i));
        if (i != size - 1) desc.append(elementSeparator);
      }
      return desc.append(end).toString();
    }
    // we can't display all elements, picks the first and last maxElementsForPrinting/2 elements
    // if maxElementsForPrinting is odd, display one more first elements than last, ex: 9 => display 5 first elements and 4 last
    int maxFirstElementsToPrint = (maxElementsForPrinting + 1) / 2;
    for (int i = 0; i < maxFirstElementsToPrint; i++) {
      desc.append(representedElements.get(i)).append(elementSeparator).append(indentation);
    }
    desc.append(DEFAULT_MAX_ELEMENTS_EXCEEDED);
    // we only append a new line if the separator had one ",\n"
    if (elementSeparator.contains(System.lineSeparator())) {
      // we just want a new line after DEFAULT_MAX_ELEMENTS_EXCEEDED but no char separator ','
      // we want:
      // first elements,
      // ...
      // last elements
      // and not:
      // first elements,
      // ...,
      // last elements
      desc.append(System.lineSeparator());
    }
    // display last elements
    int maxLastElementsToPrint = maxElementsForPrinting / 2;
    for (int i = size - maxLastElementsToPrint; i < size; i++) {
      if (i != size - maxLastElementsToPrint) desc.append(elementSeparator);
      desc.append(indentation).append(representedElements.get(i));
    }
    return desc.append(end).toString();
  }

  private String toStringOf(ChangeDelta<?> changeDelta) {
    return String.format("Changed content at line %s:%nexpecting:%n  %s%nbut was:%n  %s%n",
                         changeDelta.lineNumber(),
                         formatLines(changeDelta.getOriginal().getLines()),
                         formatLines(changeDelta.getRevised().getLines()));
  }

  private String toStringOf(DeleteDelta<?> deleteDelta) {
    return String.format("Missing content at line %s:%n  %s%n", deleteDelta.lineNumber(),
                         formatLines(deleteDelta.getOriginal().getLines()));
  }

  private String toStringOf(InsertDelta<?> insertDelta) {
    return String.format("Extra content at line %s:%n  %s%n", insertDelta.lineNumber(),
                         formatLines(insertDelta.getRevised().getLines()));
  }

  private String toStringOf(Duration duration) {
    return duration.toString().substring(2);
  }

  private String formatLines(List<?> lines) {
    return format(lines, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR_WITH_NEWLINE, "   ", lines);
  }

  private static boolean doesDescriptionFitOnSingleLine(String singleLineDescription) {
    return singleLineDescription == null || singleLineDescription.length() <= maxLengthForSingleLineDescription;
  }

  private static String identityHexCodeOf(Object obj) {
    return toHexString(System.identityHashCode(obj));
  }

  private static Object classNameOf(Object obj) {
    return obj.getClass().isAnonymousClass() ? obj.getClass().getName() : obj.getClass().getSimpleName();
  }

  private String defaultToStringWithClassNameDisambiguation(Object o) {
    return o.toString() + classNameDisambiguation(o);
  }

  private static Map<?, ?> toSortedMapIfPossible(Map<?, ?> map) {
    try {
      return new TreeMap<>(map);
    } catch (ClassCastException | NullPointerException e) {
      return map;
    }
  }

  private String format(Map<?, ?> map, Object o) {
    return o == map ? "(this Map)" : toStringOf(o);
  }
}
