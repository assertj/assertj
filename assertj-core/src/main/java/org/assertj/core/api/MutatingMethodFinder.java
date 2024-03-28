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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.function.UnaryOperator.identity;

/**
 * Looks for methods change the state of a collection and don't throw {@link
 * UnsupportedOperationException}.
 *
 * <p>The {@code visitXXX()} methods return an arbitrarily selected mutating method description, if
 * a mutating method is found and an empty {@link Optional} otherwise.
 */
final class MutatingMethodFinder implements CollectionVisitor<Optional<String>> {
  /** Mutating list operations. */
  private static final Map<String, Consumer<List>> LIST_OPERATIONS;

  /** Mutating navigable set operations. */
  private static final Map<String, Consumer<NavigableSet<?>>> NAVIGABLE_SET_OPERATIONS;

  /** Mutating map operations. */
  private static final Map<String, Consumer<Map>> MAP_OPERATIONS;

  /** Mutating navigable map operations. */
  private static final Map<String, Consumer<NavigableMap<?, ?>>> NAVIGABLE_MAP_OPERATIONS;

  /** Mutating collection operations. */
  private static final Map<String, Consumer<Collection<?>>> COLLECTION_OPERATIONS;

  static {
    Map<String, Consumer<Collection<?>>> collectionOperations = new LinkedHashMap<>();
    collectionOperations.put("Collection.add(null)", ignoreNullPointerException(c -> c.add(null)));
    collectionOperations.put("Collection.addAll([null])",
                             ignoreNullPointerException(c -> c.addAll(Collections.singletonList(null))));
    collectionOperations.put("Collection.clear()", skipEmpty((c, e) -> c.clear()));
    collectionOperations.put("Collection.iterator().remove()", skipEmpty((c, e) -> {
      Iterator<?> iter = c.iterator();
      iter.next();
      iter.remove();
    }));
    collectionOperations.put("Collection.remove(element)", skipEmpty((c, e) -> c.remove(e)));
    collectionOperations.put("Collection.removeAll([element])", skipEmpty((c, e) -> c.removeAll(Collections.singletonList(e))));
    collectionOperations.put("Collection.removeIf(element -> true)", skipEmpty((c, e) -> c.removeIf(element -> true)));
    collectionOperations.put("Collection.retainAll(emptyCollection())",
                             skipEmpty((c, e) -> c.retainAll(Collections.emptyList())));
    COLLECTION_OPERATIONS = Collections.unmodifiableMap(collectionOperations);

    Map<String, Consumer<List>> listOperations = new LinkedHashMap<>();
    listOperations.put("List.add(0, null)", ignoreNullPointerException(list -> list.add(0, null)));
    listOperations.put("List.addAll(0, [null])",
                       ignoreNullPointerException(list -> list.addAll(0, Collections.singletonList(null))));
    listOperations.put("List.listIterator().add(null)",
                       ignoreNullPointerException(list -> list.listIterator().add(null)));
    listOperations.put("List.listIterator().remove()", skipEmpty((list, e) -> {
      Iterator<?> iter = list.listIterator();
      iter.next();
      iter.remove();
    }));
    listOperations.put("List.listIterator().set(element)", skipEmpty((list, e) -> {
      ListIterator iter = list.listIterator();
      iter.next();
      iter.set(e);
    }));
    listOperations.put("List.remove(0)", skipEmpty((list, e) -> list.remove(0)));
    listOperations.put("List.replaceAll(identity())", skipEmpty((list, e) -> list.replaceAll(identity())));
    listOperations.put("List.set(0, null)", skipEmpty((list, e) -> list.set(0, e)));
    listOperations.put("List.sort((o1, o2) -> 0)", list -> {
      if (list.size() <= 1) throw new UnsupportedOperationException("list already sorted");
      list.sort((o1, o2) -> 0);
    });
    LIST_OPERATIONS = Collections.unmodifiableMap(listOperations);

    Map<String, Consumer<NavigableSet<?>>> navigableSetOperations = new LinkedHashMap<>();
    navigableSetOperations.put("NavigableSet.descendingIterator().remove()", skipEmpty((set, e) -> {
      Iterator<?> iter = set.descendingIterator();
      iter.next();
      iter.remove();
    }));
    navigableSetOperations.put("NavigableSet.pollFirst()", NavigableSet::pollFirst);
    navigableSetOperations.put("NavigableSet.pollLast()", NavigableSet::pollLast);
    NAVIGABLE_SET_OPERATIONS = Collections.unmodifiableMap(navigableSetOperations);

    Map<String, Consumer<Map>> mapOperations = new LinkedHashMap<>();
    mapOperations.put("Map.clear()", skipEmptyMap((m, k) -> m.clear()));
    mapOperations.put("Map.compute(null, (k, v) -> v)",
                      ignoreNullPointerException(map -> map.compute(null, (k, v) -> v)));
    mapOperations.put("Map.computeIfAbsent(null, k -> null)",
                      ignoreNullPointerException(map -> map.computeIfAbsent(null, k -> null)));
    mapOperations.put("Map.computeIfPresent(null, (k, v) -> v)",
                      ignoreNullPointerException(map -> map.computeIfPresent(null, (k, v) -> v)));
    mapOperations.put("Map.merge(null, null, (v1, v2) -> v1))",
                      ignoreNullPointerException(map -> map.merge(null, null, (v1, v2) -> v1)));
    mapOperations.put("Map.put(null, null)", ignoreNullPointerException(map -> map.put(null, null)));
    mapOperations.put("Map.putAll(otherMap)", ignoreNullPointerException(map -> {
      Map<Object, Object> singletonMap = new HashMap<>();
      singletonMap.put(null, null);
      map.putAll(singletonMap);
    }));
    mapOperations.put("Map.putIfAbsent(null, null)",
                      ignoreNullPointerException(map -> map.putIfAbsent(null, null)));
    mapOperations.put("Map.replace(key, original, replacement)",
                      skipEmptyMap((map, key) -> map.replace(key, map.get(key), map.get(key))));
    mapOperations.put("Map.replace(key, value)", skipEmptyMap((map, key) -> map.replace(key, map.get(key))));
    mapOperations.put("Map.remove(key)", skipEmptyMap((map, k) -> map.remove(k)));
    mapOperations.put("Map.remove(key, value)", skipEmptyMap((map, k) -> map.remove(k, map.get(k))));
    mapOperations.put("Map.replaceAll((k, v) -> v)", skipEmptyMap((map, key) -> map.replaceAll((k, v) -> v)));
    MAP_OPERATIONS = Collections.unmodifiableMap(mapOperations);

    Map<String, Consumer<NavigableMap<?, ?>>> navigableMapOperations = new LinkedHashMap<>();
    navigableMapOperations.put("NavigableMap.pollFirstEntry()", NavigableMap::pollFirstEntry);
    navigableMapOperations.put("NavigableMap.pollLastEntry()", NavigableMap::pollLastEntry);
    NAVIGABLE_MAP_OPERATIONS = Collections.unmodifiableMap(navigableMapOperations);
  }

  @Override
  public Optional<String> visitCollection(final Collection<?> target) {
    Objects.requireNonNull(target, "target");
    if (target instanceof List) return visitList((List<?>) target);
    if (target instanceof Set) return visitSet((Set<?>) target);
    return findSupportedMethod(COLLECTION_OPERATIONS, target);
  }

  @Override
  public Optional<String> visitList(final List<?> target) {
    Objects.requireNonNull(target, "target");
    Optional<String> collectionMethod = findSupportedMethod(COLLECTION_OPERATIONS, target);
    return or(collectionMethod, () -> findSupportedMethod(LIST_OPERATIONS, target));
  }

  @Override
  public Optional<String> visitSet(final Set<?> target) {
    Objects.requireNonNull(target, "target");

    return or(findSupportedMethod(COLLECTION_OPERATIONS, target), () -> target instanceof NavigableSet
        ? findSupportedMethod(NAVIGABLE_SET_OPERATIONS, (NavigableSet<?>) target)
        : Optional.empty());
  }

  @Override
  public Optional<String> visitMap(final Map<?, ?> target) {
    Objects.requireNonNull(target, "target");

    return or(findSupportedMethod(MAP_OPERATIONS, target), () -> target instanceof NavigableMap
        ? findSupportedMethod(NAVIGABLE_MAP_OPERATIONS, (NavigableMap<?, ?>) target)
        : Optional.empty());
  }

  /**
   * Looks for a mutating method that doesn't throw {@link UnsupportedOperationException}.
   *
   * @param target the collection to check
   * @param operations methods that shouldn't be supported
   * @return a description of a mutating method, if one is present in the target
   */
  private <C> Optional<String> findSupportedMethod(final Map<String, Consumer<C>> operations, final C target) {
    return operations.entrySet().stream()
                     .map(entry -> expectUnsupported(target, entry.getValue(), entry.getKey()))
                     .filter(Objects::nonNull)
                     .findFirst();
  }

  /**
   * Calls a method, expecting it to throw an {@code UnsupportedOperationException}.
   *
   * @param target the collection to check
   * @param operation calls a method that should be unsupported
   * @param description a description of the operation to include in an error message if the method
   *     turns out to be supported
   * @return the description, if the method is supported, otherwise {@code null}
   */
  private <C> String expectUnsupported(final C target, final Consumer<? super C> operation, final String description) {
    try {
      operation.accept(target);
      return description;
    } catch (UnsupportedOperationException e) {
      // happy path
      return null;
    } catch (RuntimeException e) {
      throw new TargetException(description, e);
    }
  }

  /**
   * Collections that don't allow {@code null} are expected to throw {@link NullPointerException}s
   * when trying to add {@code null}. This means the collection is mutable because it tried to add
   * {@code null} instead of throwing a {@link UnsupportedOperationException}.
   *
   * @param operation an operation that might throw a {@link NullPointerException} for some
   *     collection implementations
   */
  private static <T> Consumer<T> ignoreNullPointerException(final Consumer<T> operation) {
    return target -> {
      try {
        operation.accept(target);
      } catch (NullPointerException e) {
        // attempted to do something that might reasonably throw a NPE
      }
    };
  }

  /**
   * Test an operation that will only have an effect when a collection has at least one element.
   *
   * @param operation an operation that consumes the target collection and an element in the
   *   collection
   */
  private static <C extends Collection> Consumer<C> skipEmpty(final BiConsumer<C, Object> operation) {
    return target -> {
      if (target.isEmpty()) throw new UnsupportedOperationException("empty collection");
      operation.accept(target, target.iterator().next());
    };
  }

  /**
   * Test an operation that will only have an effect when a map has at least one element.
   *
   * @param operation an operation that consumes the target map and a key in the map
   */
  private static <M extends Map> Consumer<M> skipEmptyMap(final BiConsumer<M, Object> operation) {
    return target -> {
      if (target.isEmpty()) throw new UnsupportedOperationException("empty map");
      operation.accept(target, target.keySet().iterator().next());
    };
  }

  /** Return the primary, if present, otherwise the secondary. */
  private static Optional<String> or(final Optional<String> primary, final Supplier<Optional<String>> secondary) {
    return primary.isPresent() ? primary : secondary.get();
  }
}
