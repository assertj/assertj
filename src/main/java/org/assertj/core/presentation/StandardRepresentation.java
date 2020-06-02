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
package org.assertj.core.presentation;

import static java.lang.Integer.toHexString;
import static java.lang.reflect.Array.get;
import static java.lang.reflect.Array.getLength;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Arrays.isArrayTypePrimitive;
import static org.assertj.core.util.Arrays.isObjectArray;
import static org.assertj.core.util.Arrays.notAnArrayOfPrimitives;
import static org.assertj.core.util.DateUtil.formatAsDatetime;
import static org.assertj.core.util.DateUtil.formatAsDatetimeWithMs;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Streams.stream;
import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.util.Strings.quote;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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
import java.util.stream.Stream;

import org.assertj.core.configuration.Configuration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.data.MapEntry;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.diff.ChangeDelta;
import org.assertj.core.util.diff.DeleteDelta;
import org.assertj.core.util.diff.InsertDelta;

/**
 * Standard java object representation.
 *
 * @author Mariusz Smykula
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

  private static final Map<Class<?>, Function<?, String>> customFormatterByType = new HashMap<>();
  private static final Class<?>[] TYPE_WITH_UNAMBIGUOUS_REPRESENTATION = { Date.class, LocalDateTime.class, ZonedDateTime.class,
      OffsetDateTime.class, Calendar.class };

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
    if (object instanceof LocalDateTime) return toStringOf((LocalDateTime) object);
    if (object instanceof OffsetDateTime) return toStringOf((OffsetDateTime) object);
    if (object instanceof ZonedDateTime) return toStringOf((ZonedDateTime) object);
    if (object instanceof AtomicBoolean) return toStringOf((AtomicBoolean) object);
    if (object instanceof AtomicInteger) return toStringOf((AtomicInteger) object);
    if (object instanceof AtomicLong) return toStringOf((AtomicLong) object);
    if (object instanceof LongAdder) return toStringOf((LongAdder) object);
    if (object instanceof AtomicReference) return toStringOf((AtomicReference<?>) object);
    if (object instanceof AtomicMarkableReference) return toStringOf((AtomicMarkableReference<?>) object);
    if (object instanceof AtomicStampedReference) return toStringOf((AtomicStampedReference<?>) object);
    if (object instanceof AtomicIntegerFieldUpdater) return AtomicIntegerFieldUpdater.class.getSimpleName();
    if (object instanceof AtomicLongFieldUpdater) return AtomicLongFieldUpdater.class.getSimpleName();
    if (object instanceof AtomicReferenceFieldUpdater) return AtomicReferenceFieldUpdater.class.getSimpleName();
    if (object instanceof Number) return toStringOf((Number) object);
    if (object instanceof File) return toStringOf((File) object);
    if (object instanceof Path) return fallbackToStringOf(object);
    if (object instanceof String) return toStringOf((String) object);
    if (object instanceof Character) return toStringOf((Character) object);
    if (object instanceof Comparator) return toStringOf((Comparator<?>) object);
    if (object instanceof SimpleDateFormat) return toStringOf((SimpleDateFormat) object);
    if (object instanceof PredicateDescription) return toStringOf((PredicateDescription) object);
    if (object instanceof CompletableFuture) return toStringOf((CompletableFuture<?>) object);
    if (isArray(object)) return formatArray(object);
    if (object instanceof Iterable<?>) return smartFormat((Iterable<?>) object);
    if (object instanceof Map<?, ?>) return toStringOf((Map<?, ?>) object);
    if (object instanceof Tuple) return toStringOf((Tuple) object);
    if (object instanceof MapEntry) return toStringOf((MapEntry<?, ?>) object);
    if (object instanceof Method) return ((Method) object).toGenericString();
    if (object instanceof InsertDelta<?>) return toStringOf((InsertDelta<?>) object);
    if (object instanceof ChangeDelta<?>) return toStringOf((ChangeDelta<?>) object);
    if (object instanceof DeleteDelta<?>) return toStringOf((DeleteDelta<?>) object);
    return fallbackToStringOf(object);
  }

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

  @SuppressWarnings("unchecked")
  protected <T> String customFormat(T object) {
    if (object == null) return null;
    return ((Function<T, String>) customFormatterByType.get(object.getClass())).apply(object);
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
    for (int i = 0; i < TYPE_WITH_UNAMBIGUOUS_REPRESENTATION.length; i++) {
      if (TYPE_WITH_UNAMBIGUOUS_REPRESENTATION[i].isInstance(obj)) return true;
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
    if (comparatorSimpleClassName.length() == 0) return quote("anonymous comparator class");
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
    return c.getCanonicalName();
  }

  protected String toStringOf(String s) {
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

  protected String toStringOf(CompletableFuture<?> future) {
    String className = future.getClass().getSimpleName();
    if (!future.isDone()) return concat(className, "[Incomplete]");
    try {
      Object joinResult = future.join();
      // avoid stack overflow error if future join on itself or another future that cycles back to the first
      Object joinResultRepresentation = joinResult instanceof CompletableFuture ? joinResult : toStringOf(joinResult);
      return concat(className, "[Completed: ", joinResultRepresentation, "]");
    } catch (CompletionException e) {
      // get the stack trace of the cause (if any) to avoid polluting it with the exception from trying to join the future
      String stackTrace = e.getCause() != null ? getStackTrace(e.getCause()) : getStackTrace(e);
      return concat(className, "[Failed with the following stack trace:", String.format("%n%s", stackTrace), "]");
    } catch (CancellationException e) {
      return concat(className, "[Cancelled]");
    }
  }

  protected String toStringOf(Tuple tuple) {
    return singleLineFormat(tuple.toList(), TUPLE_START, TUPLE_END);
  }

  protected String toStringOf(MapEntry<?, ?> mapEntry) {
    return String.format("MapEntry[key=%s, value=%s]", toStringOf(mapEntry.key), toStringOf(mapEntry.value));
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
      builder.append(format(map, entry.getKey())).append('=').append(format(map, entry.getValue()));
      printedElements++;
      if (!entriesIterator.hasNext()) return builder.append("}").toString();
      builder.append(", ");
    }
  }

  protected String toStringOf(AtomicReference<?> atomicReference) {
    return String.format("AtomicReference[%s]", toStringOf(atomicReference.get()));
  }

  protected String toStringOf(AtomicMarkableReference<?> atomicMarkableReference) {
    return String.format("AtomicMarkableReference[marked=%s, reference=%s]", atomicMarkableReference.isMarked(),
                         toStringOf(atomicMarkableReference.getReference()));
  }

  protected String toStringOf(AtomicStampedReference<?> atomicStampedReference) {
    return String.format("AtomicStampedReference[stamp=%s, reference=%s]", atomicStampedReference.getStamp(),
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
   * The {@code Iterable} will be formatted to a single line if it does not exceed 100 char, otherwise each elements
   * will be formatted on a new line with 4 space indentation.
   *
   * @param iterable the {@code Iterable} to format.
   * @return the {@code String} representation of the given {@code Iterable}.
   */
  protected String smartFormat(Iterable<?> iterable) {
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
    if (!isArray(o)) return null;
    if (!isArrayTypePrimitive(o)) throw notAnArrayOfPrimitives(o);
    Object[] array = toObjectArray(o);
    return format(array, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE, array);
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
    List<String> representedElements = representElements(Stream.of(array), start, end, elementSeparator, indentation, root);
    return representGroup(representedElements, start, end, elementSeparator, indentation);
  }

  protected String format(Iterable<?> iterable, String start, String end, String elementSeparator, String indentation,
                          Object root) {
    if (iterable == null) return null;
    Iterator<?> iterator = iterable.iterator();
    if (!iterator.hasNext()) return start + end;
    // alreadyVisited is used to avoid infinite recursion when one element is a container already visited
    List<String> representedElements = representElements(stream(iterable), start, end, elementSeparator, indentation, root);
    return representGroup(representedElements, start, end, elementSeparator, indentation);
  }

  protected String safeStringOf(Object element, String start, String end, String elementSeparator, String indentation,
                                Object root) {
    if (element == root) return isArray(root) ? "(this array)" : "(this iterable)";
    // primitive array elements can't cycle back to already represented containers
    if (isArrayTypePrimitive(element)) return formatPrimitiveArray(element);
    // object array/iterable elements can cycle back to root, we pass the latter to check for it
    if (isArray(element)) return format((Object[]) element, start, end, elementSeparator, indentation, root);
    if (element instanceof Iterable && !(element instanceof Path))
      return format((Iterable) element, start, end, elementSeparator, indentation, root);
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

  private List<String> representElements(Stream<?> elements, String start, String end, String elementSeparator,
                                         String indentation, Object root) {
    return elements.map(element -> safeStringOf(element, start, end, elementSeparator, indentation, root))
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

  private static Object[] toObjectArray(Object o) {
    int length = getLength(o);
    Object[] array = new Object[length];
    for (int i = 0; i < length; i++) {
      array[i] = get(o, i);
    }
    return array;
  }

}
