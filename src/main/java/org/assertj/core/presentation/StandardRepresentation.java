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
package org.assertj.core.presentation;

import static java.lang.Integer.toHexString;
import static java.lang.reflect.Array.getLength;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Arrays.isArrayTypePrimitive;
import static org.assertj.core.util.Arrays.isObjectArray;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.concat;
import static org.assertj.core.util.Strings.quote;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import java.util.function.Function;

import org.assertj.core.data.MapEntry;
import org.assertj.core.groups.Tuple;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Compatibility;
import org.assertj.core.util.DateUtil;
import org.assertj.core.util.diff.ChangeDelta;
import org.assertj.core.util.diff.DeleteDelta;
import org.assertj.core.util.diff.InsertDelta;

/**
 * Standard java object representation.
 * 
 * @author Mariusz Smykula
 */
public class StandardRepresentation implements Representation {

  // can share this as StandardRepresentation has no state
  public static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  private static final String NULL = "null";

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
  public static final String ELEMENT_SEPARATOR_WITH_NEWLINE = ELEMENT_SEPARATOR + Compatibility.System.lineSeparator();

  private static int maxLengthForSingleLineDescription = 80;

  private static final Map<Class<?>, Function<?, String>> customFormatterByType = new HashMap<>();

  private static int maxElementsForPrinting = 1000;

  /**
   * It resets the static defaults for the standard representation.
   * <p>
   * The following defaults will be reapplied:
   * <ul>
   *   <li>{@code maxLengthForSingleLineDescription = 80}</li>
   *   <li>{@code maxElementsForPrinting = 1000}</li>
   * </ul>
   */
  public static void resetDefaults() {
    maxLengthForSingleLineDescription = 80;
    maxElementsForPrinting = 1000;
  }

  public static void setMaxLengthForSingleLineDescription(int value) {
    checkArgument(value > 0, "maxLengthForSingleLineDescription must be > 0 but was %s", value);
    maxLengthForSingleLineDescription = value;
  }

  public static int getMaxLengthForSingleLineDescription() {
    return maxLengthForSingleLineDescription;
  }

  public static void setMaxElementsForPrinting(int value) {
    checkArgument(value >= 1, "maxElementsForPrinting must be >= 1, but was %s", value);
    maxElementsForPrinting = value;
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
    if (object instanceof Calendar) return toStringOf((Calendar) object);
    if (object instanceof Class<?>) return toStringOf((Class<?>) object);
    if (object instanceof Date) return toStringOf((Date) object);
    if (object instanceof AtomicBoolean) return toStringOf((AtomicBoolean) object);
    if (object instanceof AtomicInteger) return toStringOf((AtomicInteger) object);
    if (object instanceof AtomicLong) return toStringOf((AtomicLong) object);
    if (object instanceof AtomicReference) return toStringOf((AtomicReference<?>) object);
    if (object instanceof AtomicMarkableReference) return toStringOf((AtomicMarkableReference<?>) object);
    if (object instanceof AtomicStampedReference) return toStringOf((AtomicStampedReference<?>) object);
    if (object instanceof AtomicIntegerFieldUpdater) return AtomicIntegerFieldUpdater.class.getSimpleName();
    if (object instanceof AtomicLongFieldUpdater) return AtomicLongFieldUpdater.class.getSimpleName();
    if (object instanceof AtomicReferenceFieldUpdater) return AtomicReferenceFieldUpdater.class.getSimpleName();
    if (object instanceof Number) return toStringOf((Number) object);
    if (object instanceof File) return toStringOf((File) object);
    if (object instanceof String) return toStringOf((String) object);
    if (object instanceof Character) return toStringOf((Character) object);
    if (object instanceof Comparator) return toStringOf((Comparator<?>) object);
    if (object instanceof SimpleDateFormat) return toStringOf((SimpleDateFormat) object);
    if (object instanceof PredicateDescription) return toStringOf((PredicateDescription) object);
    if (object instanceof CompletableFuture) return toStringOf((CompletableFuture<?>) object);
    if (isArray(object)) return formatArray(object);
    if (object instanceof Collection<?>) return smartFormat((Collection<?>) object);
    if (object instanceof Map<?, ?>) return toStringOf((Map<?, ?>) object);
    if (object instanceof Tuple) return toStringOf((Tuple) object);
    if (object instanceof MapEntry) return toStringOf((MapEntry<?, ?>) object);
    if (object instanceof Method) return ((Method) object).toGenericString();
    if (object instanceof InsertDelta<?>) return toStringOf((InsertDelta<?>) object);
    if (object instanceof ChangeDelta<?>) return toStringOf((ChangeDelta<?>) object);
    if (object instanceof DeleteDelta<?>) return toStringOf((DeleteDelta<?>) object);
    return object == null ? null : fallbackToStringOf(object);
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

  @Override
  public String unambiguousToStringOf(Object obj) {
    return obj == null ? null
        : String.format("%s (%s@%s)", toStringOf(obj), obj.getClass().getSimpleName(), toHexString(obj.hashCode()));
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

  protected String toStringOf(Comparator<?> comparator) {
    if (!comparator.toString().contains("@")) return quote(comparator.toString());
    String comparatorSimpleClassName = comparator.getClass().getSimpleName();
    if (comparatorSimpleClassName.length() == 0) return quote("anonymous comparator class");
    // if toString has not been redefined, let's use comparator simple class name.
    if (comparator.toString().contains(comparatorSimpleClassName + "@")) return quote(comparatorSimpleClassName);
    return quote(comparator.toString());
  }

  protected String toStringOf(Calendar c) {
    return DateUtil.formatAsDatetime(c);
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

  protected String toStringOf(Date d) {
    return DateUtil.formatAsDatetimeWithMs(d);
  }

  protected String toStringOf(Float f) {
    return String.format("%sf", f);
  }

  protected String toStringOf(Long l) {
    return String.format("%sL", l);
  }

  protected String toStringOf(File f) {
    return f.getAbsolutePath();
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
      return concat(className, "[Failed: ", toStringOf(e.getCause()), "]");
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

  private String formatLines(List<?> lines) {
    return format(lines, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR_WITH_NEWLINE, "   ");
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName();
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
    return isObjectArray(o) ? smartFormat(this, (Object[]) o) : formatPrimitiveArray(o);
  }

  protected String multiLineFormat(Representation representation, Object[] iterable, Set<Object[]> alreadyFormatted) {
    return format(iterable, ELEMENT_SEPARATOR_WITH_NEWLINE, INDENTATION_AFTER_NEWLINE, alreadyFormatted);
  }

  protected String singleLineFormat(Representation representation, Object[] iterable, String start, String end,
                                    Set<Object[]> alreadyFormatted) {
    return format(iterable, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE, alreadyFormatted);
  }

  protected String smartFormat(Representation representation, Object[] iterable) {
    Set<Object[]> alreadyFormatted = new HashSet<>();
    String singleLineDescription = singleLineFormat(representation, iterable, DEFAULT_START, DEFAULT_END,
                                                    alreadyFormatted);
    return doesDescriptionFitOnSingleLine(singleLineDescription) ? singleLineDescription
        : multiLineFormat(representation, iterable, alreadyFormatted);
  }

  protected String format(Object[] array, String elementSeparator,
                          String indentation, Set<Object[]> alreadyFormatted) {
    if (array == null) return null;
    if (array.length == 0) return DEFAULT_START + DEFAULT_END;
    // iterable has some elements
    StringBuilder desc = new StringBuilder();
    desc.append(DEFAULT_START);
    alreadyFormatted.add(array); // used to avoid infinite recursion when array contains itself
    int i = 0;
    while (true) {
      Object element = array[i];
      // do not indent first element
      if (i != 0) desc.append(indentation);
      if (i == maxElementsForPrinting) {
        desc.append(DEFAULT_MAX_ELEMENTS_EXCEEDED);
        alreadyFormatted.remove(array);
        return desc.append(DEFAULT_END).toString();
      }
      // add element representation
      if (!isArray(element)) desc.append(element == null ? NULL : toStringOf(element));
      else if (isArrayTypePrimitive(element)) desc.append(formatPrimitiveArray(element));
      else if (alreadyFormatted.contains(element)) desc.append("(this array)");
      else desc.append(format((Object[]) element, elementSeparator, indentation, alreadyFormatted));
      // manage end description
      if (i == array.length - 1) {
        alreadyFormatted.remove(array);
        return desc.append(DEFAULT_END).toString();
      }
      // there are still elements to describe
      desc.append(elementSeparator);
      i++;
    }
  }

  protected String formatPrimitiveArray(Object o) {
    if (!isArray(o)) return null;
    if (!isArrayTypePrimitive(o)) throw Arrays.notAnArrayOfPrimitives(o);
    int size = getLength(o);
    if (size == 0) return DEFAULT_START + DEFAULT_END;
    StringBuilder buffer = new StringBuilder();
    buffer.append(DEFAULT_START);
    buffer.append(toStringOf(Array.get(o, 0)));
    for (int i = 1; i < size; i++) {
      buffer.append(ELEMENT_SEPARATOR)
            .append(INDENTATION_FOR_SINGLE_LINE);
      if (i == maxElementsForPrinting) {
        buffer.append(DEFAULT_MAX_ELEMENTS_EXCEEDED);
        break;
      }

      buffer.append(toStringOf(Array.get(o, i)));
    }
    buffer.append(DEFAULT_END);
    return buffer.toString();
  }

  public String format(Iterable<?> iterable, String start, String end, String elementSeparator, String indentation) {
    if (iterable == null) return null;
    Iterator<?> iterator = iterable.iterator();
    if (!iterator.hasNext()) return start + end;
    // iterable has some elements
    StringBuilder desc = new StringBuilder(start);
    boolean firstElement = true;
    int printedElements = 0;
    while (true) {
      Object element = iterator.next();
      // do not indent first element
      if (firstElement) firstElement = false;
      else desc.append(indentation);
      // add element representation
      if (printedElements == maxElementsForPrinting) {
        desc.append(DEFAULT_MAX_ELEMENTS_EXCEEDED);
        return desc.append(end).toString();
      }
      desc.append(element == iterable ? "(this Collection)" : toStringOf(element));
      printedElements++;
      // manage end description
      if (!iterator.hasNext()) return desc.append(end).toString();
      // there are still elements to be describe
      desc.append(elementSeparator);
    }
  }

  protected String multiLineFormat(Iterable<?> iterable) {
    return format(iterable, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR_WITH_NEWLINE, INDENTATION_AFTER_NEWLINE);
  }

  protected String singleLineFormat(Iterable<?> iterable, String start, String end) {
    return format(iterable, start, end, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE);
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

  private static boolean doesDescriptionFitOnSingleLine(String singleLineDescription) {
    return singleLineDescription == null || singleLineDescription.length() < maxLengthForSingleLineDescription;
  }

}
