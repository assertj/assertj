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
package org.assertj.core.internal;

import static org.assertj.core.internal.Objects.getDeclaredFieldsIncludingInherited;
import static org.assertj.core.internal.Objects.propertyOrFieldValuesAreEqual;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.util.Strings.join;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tests two objects for differences by doing a 'deep' comparison.
 *
 * Based on the deep equals implementation of https://github.com/jdereg/java-util
 *
 * @author John DeRegnaucourt (john@cedarsoftware.com)
 * @author Pascal Schumacher
 */
public class DeepDifference {

  private static final Map<Class<?>, Boolean> customEquals = new ConcurrentHashMap<>();
  private static final Map<Class<?>, Boolean> customHash = new ConcurrentHashMap<>();

  private final static class DualKey {

    private final List<String> path;
    private final Object key1;
    private final Object key2;

    private DualKey(List<String> path, Object key1, Object key2) {
      this.path = path;
      this.key1 = key1;
      this.key2 = key2;
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof DualKey)) {
        return false;
      }

      DualKey that = (DualKey) other;
      return key1 == that.key1 && key2 == that.key2;
    }

    @Override
    public int hashCode() {
      int h1 = key1 != null ? key1.hashCode() : 0;
      int h2 = key2 != null ? key2.hashCode() : 0;
      return h1 + h2;
    }

    @Override
    public String toString() {
      return "DualKey [key1=" + key1 + ", key2=" + key2 + "]";
    }

    public List<String> getPath() {
      return path;
    }

    public String getConcatenatedPath() {
      return join(path).with(".");
    }
  }

  public static class Difference {

    List<String> path;
    Object actual;
    Object other;

    public Difference(List<String> path, Object actual, Object other) {
      this.path = path;
      this.actual = actual;
      this.other = other;
    }

    public List<String> getPath() {
      return path;
    }

    public Object getActual() {
      return actual;
    }

    public Object getOther() {
      return other;
    }

    @Override
    public String toString() {
      return "Difference [path=" + path + ", actual=" + actual + ", other=" + other + "]";
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
   * @param a Object one to compare
   * @param b Object two to compare
   * @param comparatorByPropertyOrField comparators to compare properties or fields with the given names
   * @param comparatorByType comparators to compare properties or fields with the given types
   * @return the list of differences found or an empty list if objects are equivalent.
   *         Equivalent means that all field values of both subgraphs are the same,
   *         either at the field level or via the respectively encountered overridden
   *         .equals() methods during traversal.
   */
  public static List<Difference> determineDifferences(Object a, Object b,
                                                      Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                      TypeComparators comparatorByType) {
    // replace null comparators groups by empty one to simplify code afterwards
    comparatorByPropertyOrField = comparatorByPropertyOrField == null
        ? new TreeMap<String, Comparator<?>>()
        : comparatorByPropertyOrField;
    comparatorByType = comparatorByType == null ? defaultTypeComparators() : comparatorByType;
    return determineDifferences(a, b, null, comparatorByPropertyOrField, comparatorByType);
  }

  private static List<Difference> determineDifferences(Object a, Object b, List<String> parentPath,
                                                       Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                       TypeComparators comparatorByType) {
    final Set<DualKey> visited = new HashSet<>();
    final Deque<DualKey> toCompare = initStack(a, b, parentPath, comparatorByPropertyOrField, comparatorByType);
    final List<Difference> differences = new ArrayList<>();

    while (!toCompare.isEmpty()) {
      final DualKey dualKey = toCompare.removeFirst();
      visited.add(dualKey);

      final List<String> currentPath = dualKey.getPath();
      final Object key1 = dualKey.key1;
      final Object key2 = dualKey.key2;

      if (key1 == key2) {
        continue;
      }

      if (hasCustomComparator(dualKey, comparatorByPropertyOrField, comparatorByType)) {
        if (propertyOrFieldValuesAreEqual(key1, key2, dualKey.getConcatenatedPath(),
                                          comparatorByPropertyOrField, comparatorByType))
          continue;
      }

      if (key1 == null || key2 == null) {
        differences.add(new Difference(currentPath, key1, key2));
        continue;
      }

      if (key1 instanceof Collection) {
        if (!(key2 instanceof Collection)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
      } else if (key2 instanceof Collection) {
        differences.add(new Difference(currentPath, key1, key2));
        continue;
      }

      if (key1 instanceof SortedSet) {
        if (!(key2 instanceof SortedSet)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
      } else if (key2 instanceof SortedSet) {
        differences.add(new Difference(currentPath, key1, key2));
        continue;
      }

      if (key1 instanceof SortedMap) {
        if (!(key2 instanceof SortedMap)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
      } else if (key2 instanceof SortedMap) {
        differences.add(new Difference(currentPath, key1, key2));
        continue;
      }

      if (key1 instanceof Map) {
        if (!(key2 instanceof Map)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
      } else if (key2 instanceof Map) {
        differences.add(new Difference(currentPath, key1, key2));
        continue;
      }

      // Handle all [] types. In order to be equal, the arrays must be the
      // same length, be of the same type, be in the same order, and all
      // elements within the array must be deeply equivalent.
      if (key1.getClass().isArray()) {
        if (!compareArrays(key1, key2, currentPath, toCompare, visited)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      // Special handle SortedSets because they are fast to compare
      // because their elements must be in the same order to be equivalent Sets.
      if (key1 instanceof SortedSet) {
        if (!compareOrderedCollection((Collection<?>) key1, (Collection<?>) key2, currentPath, toCompare, visited)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      // Check List, as element order matters this comparison is faster than using unordered comparison.
      if (key1 instanceof List) {
        if (!compareOrderedCollection((Collection<?>) key1, (Collection<?>) key2, currentPath, toCompare, visited)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      // Handle unordered Collection.
      if (key1 instanceof Collection) {
        if (!compareUnorderedCollection((Collection<?>) key1, (Collection<?>) key2, currentPath, toCompare,
                                        visited, comparatorByPropertyOrField, comparatorByType)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      // Compare two SortedMaps. This takes advantage of the fact that these
      // Maps can be compared in O(N) time due to their ordering.
      if (key1 instanceof SortedMap) {
        if (!compareSortedMap((SortedMap<?, ?>) key1, (SortedMap<?, ?>) key2, currentPath, toCompare, visited)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      // Compare two Unordered Maps. This is a slightly more expensive comparison because
      // order cannot be assumed, therefore a temporary Map must be created, however the
      // comparison still runs in O(N) time.
      if (key1 instanceof Map) {
        if (!compareUnorderedMap((Map<?, ?>) key1, (Map<?, ?>) key2, currentPath, toCompare, visited)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      if (hasCustomEquals(key1.getClass())) {
        if (!key1.equals(key2)) {
          differences.add(new Difference(currentPath, key1, key2));
          continue;
        }
        continue;
      }

      Set<String> key1FieldsNames = getFieldsNames(getDeclaredFieldsIncludingInherited(key1.getClass()));
      Set<String> key2FieldsNames = getFieldsNames(getDeclaredFieldsIncludingInherited(key2.getClass()));
      if (!key2FieldsNames.containsAll(key1FieldsNames)) {
        differences.add(new Difference(currentPath, key1, key2));
      } else {
        for (String fieldName : key1FieldsNames) {
          List<String> path = new ArrayList<>(currentPath);
          path.add(fieldName);
          DualKey dk = new DualKey(path,
                                   COMPARISON.getSimpleValue(fieldName, key1),
                                   COMPARISON.getSimpleValue(fieldName, key2));
          if (!visited.contains(dk)) {
            toCompare.addFirst(dk);
          }
        }
      }
    }

    return differences;
  }

  private static boolean hasCustomComparator(DualKey dualKey, Map<String, Comparator<?>> comparatorByPropertyOrField,
                                             TypeComparators comparatorByType) {
    String fieldName = dualKey.getConcatenatedPath();
    if (comparatorByPropertyOrField.containsKey(fieldName)) return true;
    // we know that dualKey.key1 != dualKey.key2 at this point, so one the key is not null
    Class<?> keyType = dualKey.key1 != null ? dualKey.key1.getClass() : dualKey.key2.getClass();
    return comparatorByType.get(keyType) != null;
  }

  private static Deque<DualKey> initStack(Object a, Object b, List<String> parentPath,
                                          Map<String, Comparator<?>> comparatorByPropertyOrField,
                                          TypeComparators comparatorByType) {
    Deque<DualKey> stack = new LinkedList<>();
    boolean isRootObject = parentPath == null;
    List<String> currentPath = isRootObject ? new ArrayList<String>() : parentPath;
    DualKey basicDualKey = new DualKey(currentPath, a, b);
    if (a != null && b != null && !isContainerType(a) && !isContainerType(b)
        && (isRootObject || !hasCustomComparator(basicDualKey, comparatorByPropertyOrField, comparatorByType))) {
      // disregard the equals method and start comparing fields
      Set<String> aFieldsNames = getFieldsNames(getDeclaredFieldsIncludingInherited(a.getClass()));
      if (!aFieldsNames.isEmpty()) {
        Set<String> bFieldsNames = getFieldsNames(getDeclaredFieldsIncludingInherited(b.getClass()));
        if (!bFieldsNames.containsAll(aFieldsNames)) {
          stack.addFirst(basicDualKey);
        } else {
          for (String fieldName : aFieldsNames) {
            List<String> fieldPath = new ArrayList<>(currentPath);
            fieldPath.add(fieldName);
            DualKey dk = new DualKey(fieldPath,
                                     COMPARISON.getSimpleValue(fieldName, a),
                                     COMPARISON.getSimpleValue(fieldName, b));
            stack.addFirst(dk);
          }
        }
      } else {
        stack.addFirst(basicDualKey);
      }
    } else {
      stack.addFirst(basicDualKey);
    }
    return stack;
  }

  private static Set<String> getFieldsNames(Collection<Field> fields) {
    Set<String> fieldNames = new LinkedHashSet<>();
    for (Field field : fields) {
      fieldNames.add(field.getName());
    }
    return fieldNames;
  }

  private static boolean isContainerType(Object o) {
    return o instanceof Collection || o instanceof Map;
  }

  /**
   * Deeply compare to Arrays []. Both arrays must be of the same type, same
   * length, and all elements within the arrays must be deeply equal in order
   * to return true.
   * 
   * @param array1 [] type (Object[], String[], etc.)
   * @param array2 [] type (Object[], String[], etc.)
   * @param path the path to the arrays to compare
   * @param toCompare add items to compare to the Stack (Stack versus recursion)
   * @param visited Set of objects already compared (prevents cycles)
   * @return true if the two arrays are the same length and contain deeply
   *         equivalent items.
   */
  private static boolean compareArrays(Object array1, Object array2, List<String> path, Deque<DualKey> toCompare,
                                       Set<DualKey> visited) {
    int len = Array.getLength(array1);
    if (len != Array.getLength(array2)) {
      return false;
    }

    for (int i = 0; i < len; i++) {
      DualKey dk = new DualKey(path, Array.get(array1, i), Array.get(array2, i));
      if (!visited.contains(dk)) {
        toCompare.addFirst(dk);
      }
    }
    return true;
  }

  /**
   * Deeply compare two Collections that must be same length and in same
   * order.
   * 
   * @param col1 First collection of items to compare
   * @param col2 Second collection of items to compare
   * @param path The path to the collections
   * @param toCompare add items to compare to the Stack (Stack versus recursion)
   * @param visited
   *          Set of objects already compared (prevents cycles) value of
   *          'true' indicates that the Collections may be equal, and the
   *          sets items will be added to the Stack for further comparison.
   */
  private static <K, V> boolean compareOrderedCollection(Collection<K> col1, Collection<V> col2,
                                                         List<String> path, Deque<DualKey> toCompare,
                                                         Set<DualKey> visited) {
    if (col1.size() != col2.size()) return false;

    Iterator<V> i2 = col2.iterator();
    for (K k : col1) {
      DualKey dk = new DualKey(path, k, i2.next());
      if (!visited.contains(dk)) toCompare.addFirst(dk);
    }
    return true;
  }

  /**
   * It places one collection into a temporary Map by deepHashCode(), so that it
   * can walk the other collection and look for each item in the map, which
   * runs in O(N) time, rather than an O(N^2) lookup that would occur if each
   * item from collection one was scanned for in collection two.
   * 
   * @param col1 First collection of items to compare
   * @param col2 Second collection of items to compare
   * @param path the path to the collections to compare
   * @param toCompare add items to compare to the Stack (Stack versus recursion)
   * @param visited Set containing items that have already been compared, so as to
   *          prevent cycles.
   * @return boolean false if the Collections are for certain not equals. A
   *         value of 'true' indicates that the Collections may be equal, and
   *         the sets items will be added to the Stack for further comparison.
   */
  private static <K, V> boolean compareUnorderedCollectionByHashCodes(Collection<K> col1, Collection<V> col2,
                                                                      List<String> path, Deque<DualKey> toCompare,
                                                                      Set<DualKey> visited) {
    Map<Integer, Object> fastLookup = new HashMap<>();
    for (Object o : col2) {
      fastLookup.put(deepHashCode(o), o);
    }

    for (Object o : col1) {
      Object other = fastLookup.get(deepHashCode(o));
      if (other == null) {
        // Item not even found in other Collection, no need to continue.
        return false;
      }

      DualKey dk = new DualKey(path, o, other);
      if (!visited.contains(dk)) {
        toCompare.addFirst(dk);
      }
    }
    return true;
  }

  /**
   * Deeply compares two collections referenced by dualKey. This method attempts
   * to quickly determine inequality by length, then if lengths match, in case of
   * collection type is Set and there are passed no custom comparators, there is used
   * comparison on hashcodes basis, otherwise each element from one collection is checked
   * for existence in another one using 'deep' comparison.
   */
  private static <K, V> boolean compareUnorderedCollection(Collection<K> col1, Collection<V> col2,
                                                           List<String> path, Deque<DualKey> toCompare,
                                                           Set<DualKey> visited,
                                                           Map<String, Comparator<?>> comparatorByPropertyOrField,
                                                           TypeComparators comparatorByType) {
    if (col1.size() != col2.size()) return false;

    boolean noCustomComparators = comparatorByPropertyOrField.isEmpty() && comparatorByType.isEmpty();
    if (noCustomComparators && col1 instanceof Set) {
      // this comparison is used for performance optimization reasons
      return compareUnorderedCollectionByHashCodes(col1, col2, path, toCompare, visited);
    }

    Collection<V> col2Copy = new LinkedList<>(col2);
    for (Object o1 : col1) {
      Iterator<V> iterator = col2Copy.iterator();
      while (iterator.hasNext()) {
        Object o2 = iterator.next();
        if (determineDifferences(o1, o2, path, comparatorByPropertyOrField, comparatorByType).isEmpty()) {
          iterator.remove();
          break;
        }
      }
    }

    return col2Copy.isEmpty();
  }

  /**
   * Deeply compare two SortedMap instances. This method walks the Maps in
   * order, taking advantage of the fact that the Maps are SortedMaps.
   * 
   * @param map1 SortedMap one
   * @param map2 SortedMap two
   * @param path the path to the maps to compare
   * @param toCompare add items to compare to the Stack (Stack versus recursion)
   * @param visited Set containing items that have already been compared, to
   *          prevent cycles.
   * @return false if the Maps are for certain not equals. 'true' indicates
   *         that 'on the surface' the maps are equal, however, it will place
   *         the contents of the Maps on the stack for further comparisons.
   */
  private static <K1, V1, K2, V2> boolean compareSortedMap(SortedMap<K1, V1> map1, SortedMap<K2, V2> map2,
                                                           List<String> path, Deque<DualKey> toCompare,
                                                           Set<DualKey> visited) {
    if (map1.size() != map2.size()) {
      return false;
    }

    Iterator<Map.Entry<K2, V2>> i2 = map2.entrySet().iterator();
    for (Map.Entry<K1, V1> entry1 : map1.entrySet()) {
      Map.Entry<K2, V2> entry2 = i2.next();

      // Must split the Key and Value so that Map.Entry's equals() method is not used.
      DualKey dk = new DualKey(path, entry1.getKey(), entry2.getKey());
      if (!visited.contains(dk)) {
        toCompare.addFirst(dk);
      }

      dk = new DualKey(path, entry1.getValue(), entry2.getValue());
      if (!visited.contains(dk)) {
        toCompare.addFirst(dk);
      }
    }
    return true;
  }

  /**
   * Deeply compare two Map instances. After quick short-circuit tests, this
   * method uses a temporary Map so that this method can run in O(N) time.
   * 
   * @param map1 Map one
   * @param map2 Map two
   * @param path the path to the maps to compare
   * @param toCompare add items to compare to the Stack (Stack versus recursion)
   * @param visited Set containing items that have already been compared, to
   *          prevent cycles.
   * @return false if the Maps are for certain not equals. 'true' indicates
   *         that 'on the surface' the maps are equal, however, it will place
   *         the contents of the Maps on the stack for further comparisons.
   */
  private static <K1, V1, K2, V2> boolean compareUnorderedMap(Map<K1, V1> map1, Map<K2, V2> map2,
                                                              List<String> path, Deque<DualKey> toCompare,
                                                              Set<DualKey> visited) {
    if (map1.size() != map2.size()) {
      return false;
    }

    Map<Integer, Map.Entry<K2, V2>> fastLookup = new HashMap<>();

    for (Map.Entry<K2, V2> entry : map2.entrySet()) {
      fastLookup.put(deepHashCode(entry.getKey()), entry);
    }

    for (Map.Entry<K1, V1> entry : map1.entrySet()) {
      Map.Entry<K2, V2> other = fastLookup.get(deepHashCode(entry.getKey()));
      if (other == null) {
        return false;
      }

      DualKey dk = new DualKey(path, entry.getKey(), other.getKey());
      if (!visited.contains(dk)) {
        toCompare.addFirst(dk);
      }

      dk = new DualKey(path, entry.getValue(), other.getValue());
      if (!visited.contains(dk)) {
        toCompare.addFirst(dk);
      }
    }

    return true;
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
  static boolean hasCustomEquals(Class<?> c) {
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
}
