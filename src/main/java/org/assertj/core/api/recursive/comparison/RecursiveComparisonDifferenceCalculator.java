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
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.recursive.comparison.ComparisonDifference.rootComparisonDifference;
import static org.assertj.core.api.recursive.comparison.DualValue.DEFAULT_ORDERED_COLLECTION_TYPES;
import static org.assertj.core.internal.Objects.getDeclaredFieldsIncludingInherited;
import static org.assertj.core.internal.Objects.getFieldsNames;
import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.IterableUtil.toCollection;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.assertj.core.internal.DeepDifference;
import org.assertj.core.util.Objects;

/**
 * Based on {@link DeepDifference} but takes a {@link RecursiveComparisonConfiguration}, {@link DeepDifference}
 * being itself based on the deep equals implementation of https://github.com/jdereg/java-util
 *
 * @author John DeRegnaucourt (john@cedarsoftware.com)
 * @author Pascal Schumacher
 */
public class RecursiveComparisonDifferenceCalculator {

  private static final String DIFFERENT_ACTUAL_AND_EXPECTED_FIELD_TYPES = "expected field is %s but actual field is not (%s)";
  private static final String ACTUAL_NOT_ORDERED_COLLECTION = "expected field is an ordered collection but actual field is not (%s), ordered collections are: "
                                                              + describeOrderedCollectionTypes();

  private static final String VALUE_FIELD_NAME = "value";
  private static final String STRICT_TYPE_ERROR = "the fields are considered different since the comparison enforces strict type check and %s is not a subtype of %s";
  private static final String DIFFERENT_SIZE_ERROR = "actual and expected values are %s of different size, actual size=%s when expected size=%s";
  private static final String MISSING_FIELDS = "%s can't be compared to %s as %s does not declare all %s fields, it lacks these: %s";
  private static final Map<Class<?>, Boolean> customEquals = new ConcurrentHashMap<>();
  private static final Map<Class<?>, Boolean> customHash = new ConcurrentHashMap<>();

  private static class ComparisonState {
    // Not using a Set as we want to precisely track visited values, a set would remove duplicates
    List<DualValue> visitedDualValues;
    List<ComparisonDifference> differences = new ArrayList<>();
    DualValueDeque dualValuesToCompare;
    RecursiveComparisonConfiguration recursiveComparisonConfiguration;

    public ComparisonState(List<DualValue> visited, RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
      this.visitedDualValues = visited;
      this.dualValuesToCompare = new DualValueDeque(recursiveComparisonConfiguration);
      this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    }

    void addDifference(DualValue dualValue) {
      differences.add(new ComparisonDifference(dualValue.getPath(), dualValue.actual, dualValue.expected));
    }

    void addDifference(DualValue dualValue, String description, Object... args) {
      differences.add(new ComparisonDifference(dualValue.getPath(), dualValue.actual, dualValue.expected,
                                               format(description, args)));
    }

    public List<ComparisonDifference> getDifferences() {
      Collections.sort(differences);
      return differences;
    }

    public boolean hasDualValuesToCompare() {
      return !dualValuesToCompare.isEmpty();
    }

    public DualValue pickDualValueToCompare() {
      final DualValue dualValue = dualValuesToCompare.removeFirst();
      if (dualValue.hasPotentialCyclingValues()) {
        // visited dual values are here to avoid cycle, java types don't have cycle, there is no need to track them.
        // moreover this would make should_fix_1854_minimal_test to fail (see the test for a detailed explanation)
        visitedDualValues.add(dualValue);
      }
      return dualValue;
    }

    private void registerForComparison(DualValue dualValue) {
      if (!visitedDualValues.contains(dualValue)) dualValuesToCompare.addFirst(dualValue);
    }

    private void initDualValuesToCompare(Object actual, Object expected, List<String> parentPath, boolean isRootObject) {
      DualValue dualValue = new DualValue(parentPath, actual, expected);
      boolean mustCompareFieldsRecursively = mustCompareFieldsRecursively(isRootObject, dualValue);
      if (dualValue.hasNoNullValues() && dualValue.hasNoContainerValues() && mustCompareFieldsRecursively) {
        // disregard the equals method and start comparing fields
        Set<String> nonIgnoredActualFieldsNames = recursiveComparisonConfiguration.getNonIgnoredActualFieldNames(dualValue);
        if (!nonIgnoredActualFieldsNames.isEmpty()) {
          // fields to ignore are evaluated when adding their corresponding dualValues to dualValuesToCompare which filters
          // ignored fields according to recursiveComparisonConfiguration
          Set<String> expectedFieldsNames = getFieldsNames(expected.getClass());
          if (expectedFieldsNames.containsAll(nonIgnoredActualFieldsNames)) {
            // we compare actual fields vs expected, ingoring expected additional fields
            for (String nonIgnoredActualFieldName : nonIgnoredActualFieldsNames) {
              DualValue fieldDualValue = new DualValue(parentPath, nonIgnoredActualFieldName,
                                                       COMPARISON.getSimpleValue(nonIgnoredActualFieldName, actual),
                                                       COMPARISON.getSimpleValue(nonIgnoredActualFieldName, expected));
              dualValuesToCompare.addFirst(fieldDualValue);
            }
          } else {
            dualValuesToCompare.addFirst(dualValue);
          }
        } else {
          dualValuesToCompare.addFirst(dualValue);
        }
      } else {
        dualValuesToCompare.addFirst(dualValue);
      }
      // We need to remove already visited fields pair to avoid infinite recursion in case
      // parent -> set{child} with child having a reference back to parent
      // it occurs to unordered collection where we compare all possible combination of the collection elements recursively
      // --
      // remove visited values one by one, DualValue.equals correctly compare respective actual and expected fields by reference
      visitedDualValues.forEach(visitedDualValue -> {
        dualValuesToCompare.stream()
                           .filter(dualValueToCompare -> dualValueToCompare.equals(visitedDualValue))
                           .findFirst()
                           .ifPresent(dualValuesToCompare::remove);
      });
    }

    private boolean mustCompareFieldsRecursively(boolean isRootObject, DualValue dualValue) {
      boolean noCustomComparisonForDualValue = !recursiveComparisonConfiguration.hasCustomComparator(dualValue)
                                               && !shouldHonorOverriddenEquals(dualValue, recursiveComparisonConfiguration);
      return isRootObject || noCustomComparisonForDualValue;
    }

  }

  /**
   * Compare two objects for differences by doing a 'deep' comparison. This will traverse the
   * Object graph and perform either a field-by-field comparison on each
   * object (if not .equals() method has been overridden from Object), or it
   * will call the customized .equals() method if it exists.
   * <p>
   *
   * This method handles cycles correctly, for example A-&gt;B-&gt;C-&gt;A.
   * Suppose a and a' are two separate instances of the A with the same values
   * for all fields on A, B, and C. Then a.deepEquals(a') will return an empty list. It
   * uses cycle detection storing visited objects in a Set to prevent endless
   * loops.
   *
   * @param actual Object one to compare
   * @param expected Object two to compare
   * @param recursiveComparisonConfiguration the recursive comparison configuration
   * @return the list of differences found or an empty list if objects are equivalent.
   *         Equivalent means that all field values of both subgraphs are the same,
   *         either at the field level or via the respectively encountered overridden
   *         .equals() methods during traversal.
   */
  public List<ComparisonDifference> determineDifferences(Object actual, Object expected,
                                                         RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    if (recursiveComparisonConfiguration.isInStrictTypeCheckingMode() && expectedTypeIsNotSubtypeOfActualType(actual, expected)) {
      return list(expectedAndActualTypeDifference(actual, expected));
    }
    List<String> rootPath = list();
    List<DualValue> visited = list();
    return determineDifferences(actual, expected, rootPath, true, visited, recursiveComparisonConfiguration);
  }

  // TODO keep track of ignored fields in an RecursiveComparisonExecution class ?

  private static List<ComparisonDifference> determineDifferences(Object actual, Object expected, List<String> parentPath,
                                                                 boolean isRootObject, List<DualValue> visited,
                                                                 RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    ComparisonState comparisonState = new ComparisonState(visited, recursiveComparisonConfiguration);
    comparisonState.initDualValuesToCompare(actual, expected, parentPath, isRootObject);

    while (comparisonState.hasDualValuesToCompare()) {
      final DualValue dualValue = comparisonState.pickDualValueToCompare();

      final List<String> currentPath = dualValue.getPath();
      final Object actualFieldValue = dualValue.actual;
      final Object expectedFieldValue = dualValue.expected;

      if (actualFieldValue == expectedFieldValue) continue;

      // Custom comparators take precedence over all other types of comparison
      if (recursiveComparisonConfiguration.hasCustomComparator(dualValue)) {
        if (!propertyOrFieldValuesAreEqual(dualValue, recursiveComparisonConfiguration)) comparisonState.addDifference(dualValue);
        // since we used a custom comparator we don't need to inspect the nested fields any further
        continue;
      }

      if (actualFieldValue == null || expectedFieldValue == null) {
        // one of the value is null while the other is not as we already know that actualFieldValue != expectedFieldValue
        comparisonState.addDifference(dualValue);
        continue;
      }

      if (dualValue.isExpectedAnEnum()) {
        compareAsEnums(dualValue, comparisonState, recursiveComparisonConfiguration);
        continue;
      }
      // TODO move hasFieldTypesDifference check into each compareXXX

      if (dualValue.isExpectedFieldAnArray()) {
        compareArrays(dualValue, comparisonState);
        continue;
      }

      // we compare ordered collections specifically as to be matching, each pair of elements at a given index must match.
      // concretely we compare: (col1[0] vs col2[0]), (col1[1] vs col2[1])...(col1[n] vs col2[n])
      if (dualValue.isExpectedFieldAnOrderedCollection()
          && !recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualValue)) {
        compareOrderedCollections(dualValue, comparisonState);
        continue;
      }

      if (dualValue.isExpectedFieldAnIterable()) {
        compareUnorderedIterables(dualValue, comparisonState);
        continue;
      }

      if (dualValue.isExpectedFieldAnOptional()) {
        compareOptional(dualValue, comparisonState);
        continue;
      }

      // Compare two SortedMaps taking advantage of the fact that these Maps can be compared in O(N) time due to their ordering
      if (dualValue.isExpectedFieldASortedMap()) {
        compareSortedMap(dualValue, comparisonState);
        continue;
      }

      // Compare two Unordered Maps. This is a slightly more expensive comparison because order cannot be assumed, therefore a
      // temporary Map must be created, however the comparison still runs in O(N) time.
      if (dualValue.isExpectedFieldAMap()) {
        compareUnorderedMap(dualValue, comparisonState);
        continue;
      }

      if (shouldCompareDualValue(recursiveComparisonConfiguration, dualValue)) {
        if (!actualFieldValue.equals(expectedFieldValue)) comparisonState.addDifference(dualValue);
        continue;
      }

      Class<?> actualFieldValueClass = actualFieldValue.getClass();
      Class<?> expectedFieldClass = expectedFieldValue.getClass();
      if (recursiveComparisonConfiguration.isInStrictTypeCheckingMode() && expectedTypeIsNotSubtypeOfActualType(dualValue)) {
        comparisonState.addDifference(dualValue, STRICT_TYPE_ERROR, expectedFieldClass.getName(),
                                      actualFieldValueClass.getName());
        continue;
      }

      Set<String> actualNonIgnoredFieldsNames = recursiveComparisonConfiguration.getNonIgnoredActualFieldNames(dualValue);
      Set<String> expectedFieldsNames = getFieldsNames(expectedFieldClass);
      // Check if expected has more fields than actual, in that case the additional fields are reported as difference
      if (!expectedFieldsNames.containsAll(actualNonIgnoredFieldsNames)) {
        // report missing fields in actual
        Set<String> actualFieldsNamesNotInExpected = newHashSet(actualNonIgnoredFieldsNames);
        actualFieldsNamesNotInExpected.removeAll(expectedFieldsNames);
        String missingFields = actualFieldsNamesNotInExpected.toString();
        String expectedClassName = expectedFieldClass.getName();
        String actualClassName = actualFieldValueClass.getName();
        String missingFieldsDescription = format(MISSING_FIELDS, actualClassName, expectedClassName,
                                                 expectedFieldClass.getSimpleName(), actualFieldValueClass.getSimpleName(),
                                                 missingFields);
        comparisonState.addDifference(dualValue, missingFieldsDescription);
      } else { // TODO remove else to report more diff
        // compare actual's fields against expected :
        // - if actual has more fields than expected, the additional fields are ignored as expected is the reference
        for (String actualFieldName : actualNonIgnoredFieldsNames) {
          if (expectedFieldsNames.contains(actualFieldName)) {
            DualValue newDualValue = new DualValue(currentPath, actualFieldName,
                                                   COMPARISON.getSimpleValue(actualFieldName, actualFieldValue),
                                                   COMPARISON.getSimpleValue(actualFieldName, expectedFieldValue));
            comparisonState.registerForComparison(newDualValue);
          }
        }
      }
    }
    return comparisonState.getDifferences();
  }

  private static boolean shouldCompareDualValue(RecursiveComparisonConfiguration recursiveComparisonConfiguration,
                                                final DualValue dualValue) {
    return !recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualValue)
           && hasOverriddenEquals(dualValue.actual.getClass());
  }

  // avoid comparing enum recursively since they contain static fields which are ignored in recursive comparison
  // this would make different field enum value to be considered the same!
  private static void compareAsEnums(final DualValue dualValue,
                                     ComparisonState comparisonState,
                                     RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    if (recursiveComparisonConfiguration.isInStrictTypeCheckingMode()) {
      // we can use == for comparison which checks both actual and expected values and types are the same
      if (dualValue.actual != dualValue.expected) comparisonState.addDifference(dualValue);
      return;
    }
    if (!dualValue.isActualAnEnum()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an enum"));
      return;
    }
    // both actual and expected are enums
    Enum<?> actualEnum = (Enum<?>) dualValue.actual;
    Enum<?> expectedEnum = (Enum<?>) dualValue.expected;
    // we must only compare actual and expected enum by value but not by type
    if (!actualEnum.name().equals(expectedEnum.name())) comparisonState.addDifference(dualValue);
  }

  private static boolean shouldHonorOverriddenEquals(DualValue dualValue,
                                                     RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    boolean shouldNotIgnoreOverriddenEqualsIfAny = !recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualValue);
    return shouldNotIgnoreOverriddenEqualsIfAny && dualValue.actual != null && hasOverriddenEquals(dualValue.actual.getClass());
  }

  private static void compareArrays(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnArray()) {
      // at the moment we only allow comparing arrays with arrays but we might allow comparing to collections later on
      // but only if we are not in strict type mode.
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an array"));
      return;
    }
    // both values in dualValue are arrays
    int actualArrayLength = Array.getLength(dualValue.actual);
    int expectedArrayLength = Array.getLength(dualValue.expected);
    if (actualArrayLength != expectedArrayLength) {
      comparisonState.addDifference(dualValue, DIFFERENT_SIZE_ERROR, "arrays", actualArrayLength, expectedArrayLength);
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register each pair of actual/expected elements for recursive comparison
    List<String> arrayFieldPath = dualValue.getPath();
    for (int i = 0; i < actualArrayLength; i++) {
      Object actualElement = Array.get(dualValue.actual, i);
      Object expectedElement = Array.get(dualValue.expected, i);
      // TODO add [i] to the path ?
      comparisonState.registerForComparison(new DualValue(arrayFieldPath, actualElement, expectedElement));
    }
  }

  /*
   * Deeply compare two Collections that must be same length and in same order.
   */
  private static void compareOrderedCollections(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnOrderedCollection()) {
      // at the moment if expected is an ordered collection then actual should also be one
      comparisonState.addDifference(dualValue, ACTUAL_NOT_ORDERED_COLLECTION, dualValue.actual.getClass().getCanonicalName());
      return;
    }

    Collection<?> actualCollection = (Collection<?>) dualValue.actual;
    Collection<?> expectedCollection = (Collection<?>) dualValue.expected;
    if (actualCollection.size() != expectedCollection.size()) {
      comparisonState.addDifference(dualValue, DIFFERENT_SIZE_ERROR,
                                    "collections", actualCollection.size(), expectedCollection.size());
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register pair of elements with same index for later comparison as we compare elements in order
    Iterator<?> expectedIterator = expectedCollection.iterator();
    List<String> path = dualValue.getPath();
    actualCollection.stream()
                    .map(element -> new DualValue(path, element, expectedIterator.next()))
                    .forEach(comparisonState::registerForComparison);
  }

  private static String differentTypeErrorMessage(DualValue dualValue, String actualTypeDescription) {
    return format(DIFFERENT_ACTUAL_AND_EXPECTED_FIELD_TYPES,
                  actualTypeDescription, dualValue.actual.getClass().getCanonicalName());
  }

  private static void compareUnorderedIterables(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnIterable()) {
      // at the moment we only compare iterable with iterables (but we might allow arrays too)
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an iterable"));
      return;
    }
    Iterable<?> actual = (Iterable<?>) dualValue.actual;
    Iterable<?> expected = (Iterable<?>) dualValue.expected;
    int actualSize = sizeOf(actual);
    int expectedSize = sizeOf(expected);
    if (actualSize != expectedSize) {
      comparisonState.addDifference(dualValue, DIFFERENT_SIZE_ERROR, "collections", actualSize, expectedSize);
      // no need to inspect elements, iterables are not equal as they don't have the same size
      return;
      // TODO instead we could register the diff between expected and actual that is:
      // - unexpected actual elements (the ones not matching any expected)
      // - expected elements not found in actual.
    }
    List<String> path = dualValue.getPath();
    // copy expected as we will remove elements found in actual
    Collection<?> expectedCopy = new LinkedList<>(toCollection(expected));
    for (Object actualElement : actual) {
      // compare recursively actualElement to all remaining expected elements
      Iterator<?> expectedIterator = expectedCopy.iterator();
      while (expectedIterator.hasNext()) {
        Object expectedElement = expectedIterator.next();
        // we need to get the currently visited dual values otherwise a cycle would cause an infinite recursion.
        List<ComparisonDifference> differences = determineDifferences(actualElement, expectedElement, path, false,
                                                                      comparisonState.visitedDualValues,
                                                                      comparisonState.recursiveComparisonConfiguration);
        if (differences.isEmpty()) {
          // we found an element in expected matching actualElement, we must remove it as if actual matches expected
          // it means for each actual element there is one and only matching expected element.
          expectedIterator.remove();
          // jump to next actual element check
          break;
        }
      }
    }

    // expectedCopy not empty = there was at least one actual element not matching any expected elements.
    if (!expectedCopy.isEmpty()) comparisonState.addDifference(dualValue);
    // TODO instead we could register the diff between expected and actual that is:
    // - unexpected actual elements (the ones not matching any expected)
    // - expected elements not found in actual.
  }

  private static <K, V> void compareSortedMap(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldASortedMap()) {
      // at the moment we only compare iterable with iterables (but we might allow arrays too)
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "a sorted map"));
      return;
    }

    Map<?, ?> actualMap = (Map<?, ?>) dualValue.actual;
    @SuppressWarnings("unchecked")
    Map<K, V> expectedMap = (Map<K, V>) dualValue.expected;
    if (actualMap.size() != expectedMap.size()) {
      comparisonState.addDifference(dualValue, DIFFERENT_SIZE_ERROR, "sorted maps", actualMap.size(), expectedMap.size());
      // no need to inspect entries, maps are not equal as they don't have the same size
      return;
      // TODO instead we could register the diff between expected and actual that is:
      // - unexpected actual entries (the ones not matching any expected)
      // - expected entries not found in actual.
    }
    List<String> path = dualValue.getPath();
    Iterator<Map.Entry<K, V>> expectedMapEntries = expectedMap.entrySet().iterator();
    for (Map.Entry<?, ?> actualEntry : actualMap.entrySet()) {
      Map.Entry<?, ?> expectedEntry = expectedMapEntries.next();
      // Must split the Key and Value so that Map.Entry's equals() method is not used.
      comparisonState.registerForComparison(new DualValue(path, actualEntry.getKey(), expectedEntry.getKey()));
      comparisonState.registerForComparison(new DualValue(path, actualEntry.getValue(), expectedEntry.getValue()));
    }
  }

  private static void compareUnorderedMap(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAMap()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "a map"));
      return;
    }

    Map<?, ?> actualMap = (Map<?, ?>) dualValue.actual;
    Map<?, ?> expectedMap = (Map<?, ?>) dualValue.expected;
    if (actualMap.size() != expectedMap.size()) {
      comparisonState.addDifference(dualValue, DIFFERENT_SIZE_ERROR, "maps", actualMap.size(), expectedMap.size());
      // no need to inspect entries, maps are not equal as they don't have the same size
      return;
      // TODO instead we could register the diff between expected and actual that is:
      // - unexpected actual entries (the ones not matching any expected)
      // - expected entries not found in actual.
    }

    Map<Integer, Map.Entry<?, ?>> fastLookup = expectedMap.entrySet().stream()
                                                          .collect(toMap(entry -> deepHashCode(entry.getKey()), entry -> entry));
    List<String> path = dualValue.getPath();
    for (Map.Entry<?, ?> actualEntry : actualMap.entrySet()) {
      int deepHashCode = deepHashCode(actualEntry.getKey());
      if (!fastLookup.containsKey(deepHashCode)) {
        // TODO add description of the entry in actual not found in expected.
        comparisonState.addDifference(dualValue);
        return;
      }
      Map.Entry<?, ?> expectedEntry = fastLookup.get(deepHashCode);
      // Must split the Key and Value so that Map.Entry's equals() method is not used.
      comparisonState.registerForComparison(new DualValue(path, actualEntry.getKey(), expectedEntry.getKey()));
      comparisonState.registerForComparison(new DualValue(path, actualEntry.getValue(), expectedEntry.getValue()));
    }
  }

  private static void compareOptional(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnOptional()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an Optional"));
      return;
    }
    Optional<?> actual = (Optional<?>) dualValue.actual;
    Optional<?> expected = (Optional<?>) dualValue.expected;
    if (actual.isPresent() != expected.isPresent()) {
      comparisonState.addDifference(dualValue);
      return;
    }
    // either both are empty or present
    if (!actual.isPresent()) return; // both optional are empty => end of the comparison
    // both are present, we have to compare their values recursively
    Object value1 = actual.get();
    Object value2 = expected.get();
    // we add VALUE_FIELD_NAME to the path since we register Optional.value fields.
    comparisonState.registerForComparison(new DualValue(dualValue.getPath(), VALUE_FIELD_NAME, value1, value2));
  }

  /**
   * Determine if the passed in class has a non-Object.equals() method. This
   * method caches its results in static ConcurrentHashMap to benefit
   * execution performance.
   *
   * @param c Class to check.
   * @return true, if the passed in Class has a .equals() method somewhere
   *         between itself and just below Object in it's inheritance.
   */
  static boolean hasOverriddenEquals(Class<?> c) {
    if (customEquals.containsKey(c)) {
      return customEquals.get(c);
    }

    Class<?> origClass = c;
    while (!Object.class.equals(c)) {
      try {
        c.getDeclaredMethod("equals", Object.class);
        customEquals.put(origClass, true);
        return true;
      } catch (Exception ignored) {}
      c = c.getSuperclass();
    }
    customEquals.put(origClass, false);
    return false;
  }

  /**
   * Get a deterministic hashCode (int) value for an Object, regardless of
   * when it was created or where it was loaded into memory. The problem with
   * java.lang.Object.hashCode() is that it essentially relies on memory
   * location of an object (what identity it was assigned), whereas this
   * method will produce the same hashCode for any object graph, regardless of
   * how many times it is created.<br>
   * <br>
   *
   * This method will handle cycles correctly (A-&gt;B-&gt;C-&gt;A). In this
   * case, Starting with object A, B, or C would yield the same hashCode. If
   * an object encountered (root, subobject, etc.) has a hashCode() method on
   * it (that is not Object.hashCode()), that hashCode() method will be called
   * and it will stop traversal on that branch.
   *
   * @param obj Object who hashCode is desired.
   * @return the 'deep' hashCode value for the passed in object.
   */
  static int deepHashCode(Object obj) {
    Set<Object> visited = new HashSet<>();
    LinkedList<Object> stack = new LinkedList<>();
    stack.addFirst(obj);
    int hash = 0;

    while (!stack.isEmpty()) {
      obj = stack.removeFirst();
      if (obj == null || visited.contains(obj)) {
        continue;
      }

      visited.add(obj);

      if (obj.getClass().isArray()) {
        int len = Array.getLength(obj);
        for (int i = 0; i < len; i++) {
          stack.addFirst(Array.get(obj, i));
        }
        continue;
      }

      if (obj instanceof Collection) {
        stack.addAll(0, (Collection<?>) obj);
        continue;
      }

      if (obj instanceof Map) {
        stack.addAll(0, ((Map<?, ?>) obj).keySet());
        stack.addAll(0, ((Map<?, ?>) obj).values());
        continue;
      }

      if (obj instanceof Double || obj instanceof Float) {
        // just take the integral value for hashcode
        // equality tests things more comprehensively
        stack.add(Math.round(((Number) obj).doubleValue()));
        continue;
      }

      if (hasCustomHashCode(obj.getClass())) {
        // A real hashCode() method exists, call it.
        hash += obj.hashCode();
        continue;
      }

      Collection<Field> fields = getDeclaredFieldsIncludingInherited(obj.getClass());
      for (Field field : fields) {
        stack.addFirst(COMPARISON.getSimpleValue(field.getName(), obj));
      }
    }
    return hash;
  }

  /**
   * Determine if the passed in class has a non-Object.hashCode() method. This
   * method caches its results in static ConcurrentHashMap to benefit
   * execution performance.
   *
   * @param c Class to check.
   * @return true, if the passed in Class has a .hashCode() method somewhere
   *         between itself and just below Object in it's inheritance.
   */
  static boolean hasCustomHashCode(Class<?> c) {
    Class<?> origClass = c;
    if (customHash.containsKey(c)) {
      return customHash.get(c);
    }

    while (!Object.class.equals(c)) {
      try {
        c.getDeclaredMethod("hashCode");
        customHash.put(origClass, true);
        return true;
      } catch (Exception ignored) {}
      c = c.getSuperclass();
    }
    customHash.put(origClass, false);
    return false;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static boolean propertyOrFieldValuesAreEqual(DualValue dualValue,
                                                       RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    final String fieldName = dualValue.getConcatenatedPath();
    final Object actualFieldValue = dualValue.actual;
    final Object expectedFieldValue = dualValue.expected;
    // no need to look into comparators if objects are the same
    if (actualFieldValue == expectedFieldValue) return true;
    // check field comparators as they take precedence over type comparators
    Comparator fieldComparator = recursiveComparisonConfiguration.getComparatorForField(fieldName);
    if (fieldComparator != null) return fieldComparator.compare(actualFieldValue, expectedFieldValue) == 0;
    // check if a type comparators exist for the field type
    Class fieldType = actualFieldValue != null ? actualFieldValue.getClass() : expectedFieldValue.getClass();
    Comparator typeComparator = recursiveComparisonConfiguration.getComparatorForType(fieldType);
    if (typeComparator != null) return typeComparator.compare(actualFieldValue, expectedFieldValue) == 0;
    // default comparison using equals
    return Objects.areEqual(actualFieldValue, expectedFieldValue);
  }

  private static ComparisonDifference expectedAndActualTypeDifference(Object actual, Object expected) {
    String additionalInformation = format("actual and expected are considered different since the comparison enforces strict type check and expected type %s is not a subtype of actual type %s",
                                          expected.getClass().getName(), actual.getClass().getName());
    return rootComparisonDifference(actual, expected, additionalInformation);
  }

  // TODO should be checking actual!
  private static boolean expectedTypeIsNotSubtypeOfActualType(DualValue dualField) {
    return expectedTypeIsNotSubtypeOfActualType(dualField.actual, dualField.expected);
  }

  private static boolean expectedTypeIsNotSubtypeOfActualType(Object actual, Object expected) {
    return !actual.getClass().isAssignableFrom(expected.getClass());
  }

  private static String describeOrderedCollectionTypes() {
    return Stream.of(DEFAULT_ORDERED_COLLECTION_TYPES)
                 .map(Class::getName)
                 .collect(joining(", ", "[", "]"));
  }
}
