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
package org.assertj.core.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tests two objects for equivalence with a 'deep' comparison.
 *
 * Based on the deep equals implementation of https://github.com/jdereg/java-util
 *
 * @author John DeRegnaucourt (john@cedarsoftware.com)
 * @author Pascal Schumacher
 */
public class DeepEquals {

    private static final Map<Class<?>, Collection<Field>> reflectedFields = new ConcurrentHashMap<Class<?>, Collection<Field>>();
    private static final Map<Class<?>, Boolean> customEquals = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Boolean> customHash = new ConcurrentHashMap<>();
    private static final double doubleEplison = 1e-15;
    private static final double floatEplison = 1e-6;

    private final static class DualKey {

        private final Object key1;
        private final Object key2;

        private DualKey(Object k1, Object k2) {
            key1 = k1;
            key2 = k2;
        }

        public boolean equals(Object other) {
            if (!(other instanceof DualKey)) {
                return false;
            }

            DualKey that = (DualKey) other;
            return key1 == that.key1 && key2 == that.key2;
        }

        public int hashCode() {
            int h1 = key1 != null ? key1.hashCode() : 0;
            int h2 = key2 != null ? key2.hashCode() : 0;
            return h1 + h2;
        }
    }

    /**
     * Compare two objects with a 'deep' comparison. This will traverse the
     * Object graph and perform either a field-by-field comparison on each
     * object (if not .equals() method has been overridden from Object), or it
     * will call the customized .equals() method if it exists.
     * </p>
     *
     * This method handles cycles correctly, for example A-&gt;B-&gt;C-&gt;A.
     * Suppose a and a' are two separate instances of the A with the same values
     * for all fields on A, B, and C. Then a.deepEquals(a') will return true. It
     * uses cycle detection storing visited objects in a Set to prevent endless
     * loops.
     * 
     * @param a Object one to compare
     * @param b Object two to compare
     * @return true if a is equivalent to b, false otherwise. Equivalent means
     *         that all field values of both subgraphs are the same, either at
     *         the field level or via the respectively encountered overridden
     *         .equals() methods during traversal.
     */
    public static boolean deepEquals(Object a, Object b) {
        Set<DualKey> visited = new HashSet<>();
        Deque<DualKey> stack = new LinkedList<>();
        stack.addFirst(new DualKey(a, b));

        while (!stack.isEmpty()) {
            DualKey dualKey = stack.removeFirst();
            visited.add(dualKey);

            Object key1 = dualKey.key1;
            Object key2 = dualKey.key2;
            
            if (key1 == key2) {
                continue;
            }

            if (key1 == null || key2 == null) {
                return false;
            }

            if (key1 instanceof Collection) {
                if (!(key2 instanceof Collection)) {
                    return false;
                }
            } else if (key2 instanceof Collection) {
                return false;
            }

            if (key1 instanceof SortedSet) {
                if (!(key2 instanceof SortedSet)) {
                    return false;
                }
            } else if (key2 instanceof SortedSet) {
                return false;
            }

            if (key1 instanceof SortedMap) {
                if (!(key2 instanceof SortedMap)) {
                    return false;
                }
            } else if (key2 instanceof SortedMap) {
                return false;
            }

            if (key1 instanceof Map) {
                if (!(key2 instanceof Map)) {
                    return false;
                }
            } else if (key2 instanceof Map) {
                return false;
            }

            if (!isContainerType(key1) && !isContainerType(key2)
                    && !key1.getClass().equals(key2.getClass())) {
                return false;
            }

            if (key1 instanceof Double) {
                if (compareFloatingPointNumbers(key1, key2, doubleEplison))
                    continue;
            }

            if (key1 instanceof Float) {
                if (compareFloatingPointNumbers(key1, key2, floatEplison))
                    continue;
            }

            // Handle all [] types. In order to be equal, the arrays must be the
            // same length, be of the same type, be in the same order, and all
            // elements withinthe array must be deeply equivalent.
            if (key1.getClass().isArray()) {
                if (!compareArrays(key1, key2, stack, visited)) {
                    return false;
                }
                continue;
            }

            // Special handle SortedSets because they are fast to compare
            // because their elements must be in the same order to be equivalent Sets.
            if (key1 instanceof SortedSet) {
                if (!compareOrderedCollection((Collection<?>) key1, (Collection<?>) key2, stack, visited)) {
                    return false;
                }
                continue;
            }

            // Handled unordered Sets. This is a slightly more expensive comparison because order cannot 
            // be assumed, a temporary Map must be created, however the comparison still runs in O(N) time.
            if (key1 instanceof Set) {
                if (!compareUnorderedCollection((Collection<?>) key1, (Collection<?>) key2, stack, visited)) {
                    return false;
                }
                continue;
            }

            // Check any Collection that is not a Set. In these cases, element
            // order matters, therefore this comparison is faster than using unordered comparison.
            if (key1 instanceof Collection) {
                if (!compareOrderedCollection((Collection<?>) key1, (Collection<?>) key2, stack, visited)) {
                    return false;
                }
                continue;
            }

            // Compare two SortedMaps. This takes advantage of the fact that these
            // Maps can be compared in O(N) time due to their ordering.
            if (key1 instanceof SortedMap) {
                if (!compareSortedMap((SortedMap<?, ?>) key1, (SortedMap<?, ?>) key2, stack, visited)) {
                    return false;
                }
                continue;
            }

            // Compare two Unordered Maps. This is a slightly more expensive comparison because
            // order cannot be assumed, therefore a temporary Map must be created, however the
            // comparison still runs in O(N) time.
            if (key1 instanceof Map) {
                if (!compareUnorderedMap((Map<?, ?>) key1, (Map<?, ?>) key2, stack, visited)) {
                    return false;
                }
                continue;
            }

            if (hasCustomEquals(key1.getClass())) {
                if (!key1.equals(key2)) {
                    return false;
                }
                continue;
            }

            Collection<Field> fields = getDeepDeclaredFields(key1.getClass());

            for (Field field : fields) {
                try {
                    DualKey dk = new DualKey(field.get(key1), field.get(key2));
                    if (!visited.contains(dk)) {
                        stack.addFirst(dk);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return true;
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
     * @param stack add items to compare to the Stack (Stack versus recursion)
     * @param visited Set of objects already compared (prevents cycles)
     * @return true if the two arrays are the same length and contain deeply
     *         equivalent items.
     */
    private static boolean compareArrays(Object array1, Object array2, Deque<DualKey> stack, Set<DualKey> visited) {
        int len = Array.getLength(array1);
        if (len != Array.getLength(array2)) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            DualKey dk = new DualKey(Array.get(array1, i), Array.get(array2, i));
            if (!visited.contains(dk)) { 
                // push contents for further comparison
                stack.addFirst(dk);
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
     * @param stack add items to compare to the Stack (Stack versus recursion)
     * @param visited
     *            Set of objects already compared (prevents cycles) value of
     *            'true' indicates that the Collections may be equal, and the
     *            sets items will be added to the Stack for further comparison.
     */
    private static <K, V> boolean compareOrderedCollection(Collection<K> col1, Collection<V> col2, Deque<DualKey> stack,
            Set<DualKey> visited) {
        if (col1.size() != col2.size()) {
            return false;
        }

        Iterator<K> i1 = col1.iterator();
        Iterator<V> i2 = col2.iterator();

        while (i1.hasNext()) {
            DualKey dk = new DualKey(i1.next(), i2.next());
            if (!visited.contains(dk)) { // push contents for further comparison
                stack.addFirst(dk);
            }
        }
        return true;
    }

    /**
     * Deeply compare the two sets referenced by dualKey. This method attempts
     * to quickly determine inequality by length, then if lengths match, it
     * places one collection into a temporary Map by deepHashCode(), so that it
     * can walk the other collection and look for each item in the map, which
     * runs in O(N) time, rather than an O(N^2) lookup that would occur if each
     * item from collection one was scanned for in collection two.
     * 
     * @param col1 First collection of items to compare
     * @param col2 Second collection of items to compare
     * @param stack add items to compare to the Stack (Stack versus recursion)
     * @param visited Set containing items that have already been compared, so as to
     *            prevent cycles.
     * @return boolean false if the Collections are for certain not equals. A
     *         value of 'true' indicates that the Collections may be equal, and
     *         the sets items will be added to the Stack for further comparison.
     */
    private static <K, V> boolean compareUnorderedCollection(Collection<K> col1, Collection<V> col2, Deque<DualKey> stack,
            Set<DualKey> visited) {
        if (col1.size() != col2.size()) {
            return false;
        }

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

            DualKey dk = new DualKey(o, other);
            if (!visited.contains(dk)) { 
                // Place items on 'stack' for future equality comparison.
                stack.addFirst(dk);
            }
        }
        return true;
    }

    /**
     * Deeply compare two SortedMap instances. This method walks the Maps in
     * order, taking advantage of the fact that the Maps are SortedMaps.
     * 
     * @param map1 SortedMap one
     * @param map2 SortedMap two
     * @param stack add items to compare to the Stack (Stack versus recursion)
     * @param visited Set containing items that have already been compared, to
     *            prevent cycles.
     * @return false if the Maps are for certain not equals. 'true' indicates
     *         that 'on the surface' the maps are equal, however, it will place
     *         the contents of the Maps on the stack for further comparisons.
     */
    private static <K1, V1, K2, V2> boolean compareSortedMap(SortedMap<K1, V1> map1, SortedMap<K2, V2> map2, Deque<DualKey> stack,
            Set<DualKey> visited) {
        if (map1.size() != map2.size()) {
            return false;
        }

        Iterator<Map.Entry<K1, V1>> i1 = map1.entrySet().iterator();
        Iterator<Map.Entry<K2, V2>> i2 = map2.entrySet().iterator();

        while (i1.hasNext()) {
            Map.Entry<K1, V1> entry1 = i1.next();
            Map.Entry<K2, V2> entry2 = i2.next();

            // Must split the Key and Value so that Map.Entry's equals() method is not used.
            DualKey dk = new DualKey(entry1.getKey(), entry2.getKey());
            if (!visited.contains(dk)) {
                // Push Keys for further comparison
                stack.addFirst(dk);
            }

            dk = new DualKey(entry1.getValue(), entry2.getValue());
            if (!visited.contains(dk)) {
                // Push values for further comparison
                stack.addFirst(dk);
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
     * @param stack add items to compare to the Stack (Stack versus recursion)
     * @param visited Set containing items that have already been compared, to
     *            prevent cycles.
     * @return false if the Maps are for certain not equals. 'true' indicates
     *         that 'on the surface' the maps are equal, however, it will place
     *         the contents of the Maps on the stack for further comparisons.
     */
    private static <K1, V1, K2, V2> boolean compareUnorderedMap(Map<K1, V1> map1, Map<K2, V2> map2, Deque<DualKey> stack,
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

            DualKey dk = new DualKey(entry.getKey(), other.getKey());
            if (!visited.contains(dk)) { // Push keys for further comparison
                stack.addFirst(dk);
            }

            dk = new DualKey(entry.getValue(), other.getValue());
            if (!visited.contains(dk)) { // Push values for further comparison
                stack.addFirst(dk);
            }
        }

        return true;
    }

    /**
     * Compare if two floating point numbers are within a given range
     */
    private static boolean compareFloatingPointNumbers(Object a, Object b, double epsilon) {
        double a1 = a instanceof Double ? (Double) a : (Float) a;
        double b1 = b instanceof Double ? (Double) b : (Float) b;
        return nearlyEqual(a1, b1, epsilon);
    }

    /**
     * Correctly handles floating point comparison. <br>
     * source: http://floating-point-gui.de/errors/comparison/
     *
     * @param a first number
     * @param b second number
     * @param epsilon double tolerance value
     * @return true if a and b are close enough
     */
    private static boolean nearlyEqual(double a, double b, double epsilon) {
        final double absA = Math.abs(a);
        final double absB = Math.abs(b);
        final double diff = Math.abs(a - b);

        if (a == b) { 
            // shortcut, handles infinities
            return true;
        } else if (a == 0 || b == 0 || diff < Double.MIN_NORMAL) {
            // a or b is zero or both are extremely close to it
            // relative error is less meaningful here
            return diff < (epsilon * Double.MIN_NORMAL);
        } else { // use relative error
            return diff / (absA + absB) < epsilon;
        }
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
    protected static boolean hasCustomEquals(Class<?> c) {
        Class<?> origClass = c;
        if (customEquals.containsKey(c)) {
            return customEquals.get(c);
        }

        while (!Object.class.equals(c)) {
            try {
                c.getDeclaredMethod("equals", Object.class);
                customEquals.put(origClass, true);
                return true;
            } catch (Exception ignored) {
            }
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
    protected static int deepHashCode(Object obj) {
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

            Collection<Field> fields = getDeepDeclaredFields(obj.getClass());
            for (Field field : fields) {
                try {
                    stack.addFirst(field.get(obj));
                } catch (Exception ignored) {
                }
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
    protected static boolean hasCustomHashCode(Class<?> c) {
        Class<?> origClass = c;
        if (customHash.containsKey(c)) {
            return customHash.get(c);
        }

        while (!Object.class.equals(c)) {
            try {
                c.getDeclaredMethod("hashCode");
                customHash.put(origClass, true);
                return true;
            } catch (Exception ignored) {
            }
            c = c.getSuperclass();
        }
        customHash.put(origClass, false);
        return false;
    }

    /**
     * Get all non static, non transient, fields of the passed in class,
     * including private fields. Note, the special this$ field is also not
     * returned. The result is cached in a static ConcurrentHashMap to benefit
     * execution performance.
     * 
     * @param c Class instance
     * @return Collection of only the fields in the passed in class that would
     *         need further processing (reference fields). This makes field
     *         traversal on a class faster as it does not need to continually
     *         process known fields like primitives.
     */
    public static Collection<Field> getDeepDeclaredFields(Class<?> c) {
        if (reflectedFields.containsKey(c)) {
            return reflectedFields.get(c);
        }
        Collection<Field> fields = new ArrayList<>();
        Class<?> curr = c;

        while (curr != null) {
            getDeclaredFields(curr, fields);
            curr = curr.getSuperclass();
        }
        reflectedFields.put(c, fields);
        return fields;
    }

    /**
     * Get all non static fields of the passed in class, including private
     * fields. Note, the special this$ field is also not returned. The resulting
     * fields are stored in a Collection.
     * 
     * @param c Class instance that would need further processing (reference fields).
     * This makes field traversal on a class faster as it
     * does not need to continually process known fields like primitives.
     */
    private static void getDeclaredFields(Class<?> c, Collection<Field> fields) {
        try {
            Field[] local = c.getDeclaredFields();

            for (Field field : local) {
                if (!field.isAccessible()) {
                    try {
                        field.setAccessible(true);
                    } catch (Exception ignored) {
                    }
                }

                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && !field.getName().startsWith("this$")) { 
                    fields.add(field);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
