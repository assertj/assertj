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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.recursive.comparison.ComparisonDifference.rootComparisonDifference;
import static org.assertj.core.api.recursive.comparison.DualValue.DEFAULT_ORDERED_COLLECTION_TYPES;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.internal.Objects.getFieldsNames;
import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.IterableUtil.toCollection;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.Stream;

import org.assertj.core.internal.DeepDifference;

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
  private static final String ARRAY_FIELD_NAME = "array";
  private static final String STRICT_TYPE_ERROR = "the fields are considered different since the comparison enforces strict type check and %s is not a subtype of %s";
  private static final String DIFFERENT_SIZE_ERROR = "actual and expected values are %s of different size, actual size=%s when expected size=%s";
  private static final String MISSING_FIELDS = "%s can't be compared to %s as %s does not declare all %s fields, it lacks these: %s";
  private static final Map<Class<?>, Boolean> customEquals = new ConcurrentHashMap<>();

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
      differences.add(new ComparisonDifference(dualValue, null, getCustomErrorMessage(dualValue)));
    }

    void addDifference(DualValue dualValue, String description) {
      differences.add(new ComparisonDifference(dualValue, description, getCustomErrorMessage(dualValue)));
    }

    void addKeyDifference(DualValue parentDualValue, Object actualKey, Object expectedKey) {
      differences.add(new ComparisonKeyDifference(parentDualValue, actualKey, expectedKey));
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

    private void initDualValuesToCompare(Object actual, Object expected, FieldLocation fieldLocation) {
      DualValue dualValue = new DualValue(fieldLocation, actual, expected);
      boolean mustCompareFieldsRecursively = mustCompareFieldsRecursively(dualValue);
      if (dualValue.hasNoNullValues() && mustCompareFieldsRecursively) {
        // disregard the equals method and start comparing fields
        // TODO should fail if actual and expected don't have the same fields to compare (taking into account ignored/compared
        // fields)
        Set<String> actualFieldNamesToCompare = recursiveComparisonConfiguration.getActualFieldNamesToCompare(dualValue);
        if (!actualFieldNamesToCompare.isEmpty()) {
          // fields to ignore are evaluated when adding their corresponding dualValues to dualValuesToCompare which filters
          // ignored fields according to recursiveComparisonConfiguration
          Set<String> expectedFieldsNames = getFieldsNames(expected.getClass());
          if (expectedFieldsNames.containsAll(actualFieldNamesToCompare)) {
            // we compare actual fields vs expected, ignoring expected additional fields
            for (String nonIgnoredActualFieldName : actualFieldNamesToCompare) {
              DualValue fieldDualValue = new DualValue(fieldLocation.field(nonIgnoredActualFieldName),
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
      // We need to remove already visited fields pair to avoid infinite recursion in case parent -> set{child} with child having
      // a reference back to its parent but only for complex types can have cycle, this is not the case for primitive or enums.
      // It occurs for unordered collection where we compare all possible combination of the collection elements recursively.
      // --
      // remove visited values one by one, DualValue.equals correctly compare respective actual and expected fields by reference
      visitedDualValues.forEach(visitedDualValue -> dualValuesToCompare.stream()
                                                                       .filter(dualValueToCompare -> dualValueToCompare.equals(visitedDualValue))
                                                                       .findFirst()
                                                                       .ifPresent(dualValuesToCompare::remove));
    }

    private boolean mustCompareFieldsRecursively(DualValue dualValue) {

      return !recursiveComparisonConfiguration.hasCustomComparator(dualValue)
             && !shouldHonorEquals(dualValue, recursiveComparisonConfiguration)
             && dualValue.hasNoContainerValues();
    }

    private String getCustomErrorMessage(DualValue dualValue) {
      String fieldName = dualValue.getConcatenatedPath();
      // field custom messages take precedence over type messages
      if (recursiveComparisonConfiguration.hasCustomMessageForField(fieldName)) {
        return recursiveComparisonConfiguration.getMessageForField(fieldName);
      }
      Class<?> fieldType = dualValue.actual != null ? dualValue.actual.getClass() : dualValue.expected.getClass();
      if (recursiveComparisonConfiguration.hasCustomMessageForType(fieldType)) {
        return recursiveComparisonConfiguration.getMessageForType(fieldType);
      }
      return null;
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
    List<DualValue> visited = list();
    return determineDifferences(actual, expected, rootFieldLocation(), visited, recursiveComparisonConfiguration);
  }

  // TODO keep track of ignored fields in an RecursiveComparisonExecution class ?

  private static List<ComparisonDifference> determineDifferences(Object actual, Object expected, FieldLocation fieldLocation,
                                                                 List<DualValue> visited,
                                                                 RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    ComparisonState comparisonState = new ComparisonState(visited, recursiveComparisonConfiguration);
    comparisonState.initDualValuesToCompare(actual, expected, fieldLocation);

    while (comparisonState.hasDualValuesToCompare()) {
      final DualValue dualValue = comparisonState.pickDualValueToCompare();

      final Object actualFieldValue = dualValue.actual;
      final Object expectedFieldValue = dualValue.expected;

      // Custom comparators take precedence over all other types of comparison
      if (recursiveComparisonConfiguration.hasCustomComparator(dualValue)) {
        if (!areDualValueEqual(dualValue, recursiveComparisonConfiguration)) comparisonState.addDifference(dualValue);
        // since we used a custom comparator we don't need to inspect the nested fields any further
        continue;
      }

      if (actualFieldValue == expectedFieldValue) continue;

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
          && !recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualValue.fieldLocation)) {
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

      // compare Atomic types by value manually as they are container type and we can't use introspection in java 17+
      if (dualValue.isExpectedFieldAnAtomicBoolean()) {
        compareAtomicBoolean(dualValue, comparisonState);
        continue;
      }
      if (dualValue.isExpectedFieldAnAtomicInteger()) {
        compareAtomicInteger(dualValue, comparisonState);
        continue;
      }
      if (dualValue.isExpectedFieldAnAtomicIntegerArray()) {
        compareAtomicIntegerArray(dualValue, comparisonState);
        continue;
      }
      if (dualValue.isExpectedFieldAnAtomicLong()) {
        compareAtomicLong(dualValue, comparisonState);
        continue;
      }
      if (dualValue.isExpectedFieldAnAtomicLongArray()) {
        compareAtomicLongArray(dualValue, comparisonState);
        continue;
      }
      if (dualValue.isExpectedFieldAnAtomicReference()) {
        compareAtomicReference(dualValue, comparisonState);
        continue;
      }
      if (dualValue.isExpectedFieldAnAtomicReferenceArray()) {
        compareAtomicReferenceArray(dualValue, comparisonState);
        continue;
      }

      if (shouldHonorEquals(dualValue, recursiveComparisonConfiguration)) {
        if (!actualFieldValue.equals(expectedFieldValue)) comparisonState.addDifference(dualValue);
        continue;
      }

      Class<?> actualFieldValueClass = actualFieldValue.getClass();
      Class<?> expectedFieldClass = expectedFieldValue.getClass();
      if (recursiveComparisonConfiguration.isInStrictTypeCheckingMode() && expectedTypeIsNotSubtypeOfActualType(dualValue)) {
        comparisonState.addDifference(dualValue,
                                      format(STRICT_TYPE_ERROR, expectedFieldClass.getName(), actualFieldValueClass.getName()));
        continue;
      }

      Set<String> actualNonIgnoredFieldsNames = recursiveComparisonConfiguration.getActualFieldNamesToCompare(dualValue);
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
            DualValue newDualValue = new DualValue(dualValue.fieldLocation.field(actualFieldName),
                                                   COMPARISON.getSimpleValue(actualFieldName, actualFieldValue),
                                                   COMPARISON.getSimpleValue(actualFieldName, expectedFieldValue));
            comparisonState.registerForComparison(newDualValue);
          }
        }
      }
    }
    return comparisonState.getDifferences();
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

  private static boolean shouldHonorEquals(DualValue dualValue,
                                           RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    // since java 17 we can't introspect java types and get their fields so by default we compare them with equals
    // unless for some container like java types: iterables, array, optional, atomic values where we take the contained values
    // through accessors and register them in the recursive comparison.
    boolean shouldHonorJavaTypeEquals = dualValue.hasSomeJavaTypeValue() && !dualValue.isExpectedAContainer();
    return shouldHonorJavaTypeEquals || shouldHonorOverriddenEquals(dualValue, recursiveComparisonConfiguration);
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
      comparisonState.addDifference(dualValue, format(DIFFERENT_SIZE_ERROR, "arrays", actualArrayLength, expectedArrayLength));
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register each pair of actual/expected elements for recursive comparison
    FieldLocation arrayFieldLocation = dualValue.fieldLocation;
    for (int i = 0; i < actualArrayLength; i++) {
      Object actualElement = Array.get(dualValue.actual, i);
      Object expectedElement = Array.get(dualValue.expected, i);
      FieldLocation elementFieldLocation = arrayFieldLocation.field(format("[%d]", i));
      comparisonState.registerForComparison(new DualValue(elementFieldLocation, actualElement, expectedElement));
    }
  }

  /*
   * Deeply compare two Collections that must be same length and in same order.
   */
  private static void compareOrderedCollections(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnOrderedCollection()) {
      // at the moment if expected is an ordered collection then actual should also be one
      comparisonState.addDifference(dualValue,
                                    format(ACTUAL_NOT_ORDERED_COLLECTION, dualValue.actual.getClass().getCanonicalName()));
      return;
    }

    Collection<?> actualCollection = (Collection<?>) dualValue.actual;
    Collection<?> expectedCollection = (Collection<?>) dualValue.expected;
    if (actualCollection.size() != expectedCollection.size()) {
      comparisonState.addDifference(dualValue, format(DIFFERENT_SIZE_ERROR, "collections", actualCollection.size(),
                                                      expectedCollection.size()));
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register pair of elements with same index for later comparison as we compare elements in order
    Iterator<?> expectedIterator = expectedCollection.iterator();
    int i = 0;
    for (Object element : actualCollection) {
      FieldLocation elementFielLocation = dualValue.fieldLocation.field(format("[%d]", i));
      DualValue elementDualValue = new DualValue(elementFielLocation, element, expectedIterator.next());
      comparisonState.registerForComparison(elementDualValue);
      i++;
    }
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
      comparisonState.addDifference(dualValue, format(DIFFERENT_SIZE_ERROR, "collections", actualSize, expectedSize));
      // no need to inspect elements, iterables are not equal as they don't have the same size
      return;
    }
    // copy actual as we will remove elements found in expected
    Collection<?> actualCopy = new LinkedList<>(toCollection(actual));
    List<Object> expectedElementsNotFound = list();
    for (Object expectedElement : expected) {
      boolean expectedElementMatched = false;
      // compare recursively expectedElement to all remaining actual elements
      Iterator<?> actualIterator = actualCopy.iterator();
      while (actualIterator.hasNext()) {
        Object actualElement = actualIterator.next();
        // we need to get the currently visited dual values otherwise a cycle would cause an infinite recursion.
        List<ComparisonDifference> differences = determineDifferences(actualElement, expectedElement, dualValue.fieldLocation,
                                                                      comparisonState.visitedDualValues,
                                                                      comparisonState.recursiveComparisonConfiguration);
        if (differences.isEmpty()) {
          // found an element in actual matching expectedElement, remove it as it can't be used to match other expected elements
          actualIterator.remove();
          expectedElementMatched = true;
          // jump to next actual element check
          break;
        }
      }
      if (!expectedElementMatched) {
        expectedElementsNotFound.add(expectedElement);
      }
    }

    if (!expectedElementsNotFound.isEmpty()) {
      String unmatched = format("The following expected elements were not matched in the actual %s:%n  %s",
                                actual.getClass().getSimpleName(), expectedElementsNotFound);
      comparisonState.addDifference(dualValue, unmatched);
      // TODO could improve the error by listing the actual elements not in expected but that would need
      // another double loop inverting actual and expected to find the actual elements not matched in expected
    }
  }

  // TODO replace by ordered map
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
      comparisonState.addDifference(dualValue, format(DIFFERENT_SIZE_ERROR, "sorted maps", actualMap.size(), expectedMap.size()));
      // no need to inspect entries, maps are not equal as they don't have the same size
      return;
    }
    Iterator<Map.Entry<K, V>> expectedMapEntries = expectedMap.entrySet().iterator();
    for (Map.Entry<?, ?> actualEntry : actualMap.entrySet()) {
      Map.Entry<?, ?> expectedEntry = expectedMapEntries.next();
      // check keys are matched before comparing values as keys represents a field
      if (!java.util.Objects.equals(actualEntry.getKey(), expectedEntry.getKey())) {
        // report a missing key/field.
        comparisonState.addKeyDifference(dualValue, actualEntry.getKey(), expectedEntry.getKey());
      } else {
        // as the key/field match we can simply compare field/key values
        FieldLocation keyFieldLocation = keyFieldLocation(dualValue.fieldLocation, actualEntry.getKey());
        comparisonState.registerForComparison(new DualValue(keyFieldLocation, actualEntry.getValue(), expectedEntry.getValue()));
      }
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
      comparisonState.addDifference(dualValue, format(DIFFERENT_SIZE_ERROR, "maps", actualMap.size(), expectedMap.size()));
      // no need to inspect entries, maps are not equal as they don't have the same size
      return;
    }
    // actual and expected maps same size but do they have the same keys?
    Set<?> expectedKeysNotFound = new LinkedHashSet<>(expectedMap.keySet());
    expectedKeysNotFound.removeAll(actualMap.keySet());
    if (!expectedKeysNotFound.isEmpty()) {
      comparisonState.addDifference(dualValue, format("The following keys were not found in the actual map value:%n  %s",
                                                      expectedKeysNotFound));
      return;
    }
    // actual and expected maps have the same keys, we need now to compare their values
    for (Object key : expectedMap.keySet()) {
      FieldLocation keyFieldLocation = keyFieldLocation(dualValue.fieldLocation, key);
      comparisonState.registerForComparison(new DualValue(keyFieldLocation, actualMap.get(key), expectedMap.get(key)));
    }
  }

  private static FieldLocation keyFieldLocation(FieldLocation parentFieldLocation, Object key) {
    return key == null ? parentFieldLocation : parentFieldLocation.field(key.toString());
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
    comparisonState.registerForComparison(new DualValue(dualValue.fieldLocation.field(VALUE_FIELD_NAME), value1, value2));
  }

  private static void compareAtomicBoolean(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicBoolean()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicBoolean"));
      return;
    }
    AtomicBoolean actual = (AtomicBoolean) dualValue.actual;
    AtomicBoolean expected = (AtomicBoolean) dualValue.expected;
    Object value1 = actual.get();
    Object value2 = expected.get();
    // we add VALUE_FIELD_NAME to the path since we register AtomicBoolean.value fields.
    comparisonState.registerForComparison(new DualValue(dualValue.fieldLocation.field(VALUE_FIELD_NAME), value1, value2));
  }

  private static void compareAtomicInteger(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicInteger()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicInteger"));
      return;
    }
    AtomicInteger actual = (AtomicInteger) dualValue.actual;
    AtomicInteger expected = (AtomicInteger) dualValue.expected;
    Object value1 = actual.get();
    Object value2 = expected.get();
    // we add VALUE_FIELD_NAME to the path since we register AtomicInteger.value fields.
    comparisonState.registerForComparison(new DualValue(dualValue.fieldLocation.field(VALUE_FIELD_NAME), value1, value2));
  }

  private static void compareAtomicIntegerArray(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicIntegerArray()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicIntegerArray"));
      return;
    }
    AtomicIntegerArray actual = (AtomicIntegerArray) dualValue.actual;
    AtomicIntegerArray expected = (AtomicIntegerArray) dualValue.expected;

    // both values in dualValue are arrays
    int actualArrayLength = actual.length();
    int expectedArrayLength = expected.length();
    if (actualArrayLength != expectedArrayLength) {
      comparisonState.addDifference(dualValue,
                                    format(DIFFERENT_SIZE_ERROR, "AtomicIntegerArrays", actualArrayLength, expectedArrayLength));
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register each pair of actual/expected elements for recursive comparison
    FieldLocation arrayFieldLocation = dualValue.fieldLocation;
    for (int i = 0; i < actualArrayLength; i++) {
      Object actualElement = actual.get(i);
      Object expectedElement = expected.get(i);
      FieldLocation elementFieldLocation = arrayFieldLocation.field(format(ARRAY_FIELD_NAME + "[%d]", i));
      comparisonState.registerForComparison(new DualValue(elementFieldLocation, actualElement, expectedElement));
    }
  }

  private static void compareAtomicLong(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicLong()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicLong"));
      return;
    }
    AtomicLong actual = (AtomicLong) dualValue.actual;
    AtomicLong expected = (AtomicLong) dualValue.expected;
    Object value1 = actual.get();
    Object value2 = expected.get();
    // we add VALUE_FIELD_NAME to the path since we register AtomicLong.value fields.
    comparisonState.registerForComparison(new DualValue(dualValue.fieldLocation.field(VALUE_FIELD_NAME), value1, value2));
  }

  private static void compareAtomicLongArray(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicLongArray()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicLongArray"));
      return;
    }
    AtomicLongArray actual = (AtomicLongArray) dualValue.actual;
    AtomicLongArray expected = (AtomicLongArray) dualValue.expected;

    // both values in dualValue are arrays
    int actualArrayLength = actual.length();
    int expectedArrayLength = expected.length();
    if (actualArrayLength != expectedArrayLength) {
      comparisonState.addDifference(dualValue,
                                    format(DIFFERENT_SIZE_ERROR, "AtomicLongArrays", actualArrayLength, expectedArrayLength));
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register each pair of actual/expected elements for recursive comparison
    FieldLocation arrayFieldLocation = dualValue.fieldLocation;
    for (int i = 0; i < actualArrayLength; i++) {
      Object actualElement = actual.get(i);
      Object expectedElement = expected.get(i);
      FieldLocation elementFieldLocation = arrayFieldLocation.field(format(ARRAY_FIELD_NAME + "[%d]", i));
      comparisonState.registerForComparison(new DualValue(elementFieldLocation, actualElement, expectedElement));
    }
  }

  private static void compareAtomicReferenceArray(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicReferenceArray()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicReferenceArray"));
      return;
    }
    AtomicReferenceArray<?> actual = (AtomicReferenceArray<?>) dualValue.actual;
    AtomicReferenceArray<?> expected = (AtomicReferenceArray<?>) dualValue.expected;

    // both values in dualValue are arrays
    int actualArrayLength = actual.length();
    int expectedArrayLength = expected.length();
    if (actualArrayLength != expectedArrayLength) {
      comparisonState.addDifference(dualValue,
                                    format(DIFFERENT_SIZE_ERROR, "AtomicReferenceArrays", actualArrayLength,
                                           expectedArrayLength));
      // no need to inspect elements, arrays are not equal as they don't have the same size
      return;
    }
    // register each pair of actual/expected elements for recursive comparison
    FieldLocation arrayFieldLocation = dualValue.fieldLocation;
    for (int i = 0; i < actualArrayLength; i++) {
      Object actualElement = actual.get(i);
      Object expectedElement = expected.get(i);
      FieldLocation elementFieldLocation = arrayFieldLocation.field(format(ARRAY_FIELD_NAME + "[%d]", i));
      comparisonState.registerForComparison(new DualValue(elementFieldLocation, actualElement, expectedElement));
    }
  }

  private static void compareAtomicReference(DualValue dualValue, ComparisonState comparisonState) {
    if (!dualValue.isActualFieldAnAtomicReference()) {
      comparisonState.addDifference(dualValue, differentTypeErrorMessage(dualValue, "an AtomicReference"));
      return;
    }
    AtomicReference<?> actual = (AtomicReference<?>) dualValue.actual;
    AtomicReference<?> expected = (AtomicReference<?>) dualValue.expected;
    Object value1 = actual.get();
    Object value2 = expected.get();
    // we add VALUE_FIELD_NAME to the path since we register AtomicReference.value fields.
    comparisonState.registerForComparison(new DualValue(dualValue.fieldLocation.field(VALUE_FIELD_NAME), value1, value2));
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

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static boolean areDualValueEqual(DualValue dualValue,
                                           RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    final String fieldName = dualValue.getConcatenatedPath();
    final Object actualFieldValue = dualValue.actual;
    final Object expectedFieldValue = dualValue.expected;
    // check field comparators as they take precedence over type comparators
    Comparator fieldComparator = recursiveComparisonConfiguration.getComparatorForField(fieldName);
    if (fieldComparator != null) return areEqualUsingComparator(actualFieldValue, expectedFieldValue, fieldComparator);
    // check if a type comparators exist for the field type
    Class fieldType = actualFieldValue != null ? actualFieldValue.getClass() : expectedFieldValue.getClass();
    Comparator typeComparator = recursiveComparisonConfiguration.getComparatorForType(fieldType);
    if (typeComparator != null) return areEqualUsingComparator(actualFieldValue, expectedFieldValue, typeComparator);
    // default comparison using equals
    return deepEquals(actualFieldValue, expectedFieldValue);
  }

  private static boolean areEqualUsingComparator(final Object actual, final Object expected, Comparator<Object> comparator) {
    try {
      return comparator.compare(actual, expected) == 0;
    } catch (ClassCastException e) {
      // this occurs when comparing field of different types, Person.id is an int and PersonDto.id is a long
      return false;
    }
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
