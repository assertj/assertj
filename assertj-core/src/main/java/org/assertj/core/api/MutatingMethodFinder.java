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

import static java.util.function.UnaryOperator.identity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Looks for methods change the state of a collection and don't throw {@link
 * UnsupportedOperationException}.
 *
 * <p>The {@code visitXXX()} methods return an arbitrarily selected mutating method description, if
 * a mutating method is found and an empty {@link Optional} otherwise.
 */
final class MutatingMethodFinder implements CollectionVisitor<Optional<String>> {
  /** Mutating list operations. */
  private static final Map<String, Consumer<List<?>>> LIST_OPERATIONS;

  /** Mutating navigable set operations. */
  private static final Map<String, Consumer<NavigableSet<?>>> NAVIGABLE_SET_OPERATIONS;

  /** Mutating map operations. */
  private static final Map<String, Consumer<Map<?, ?>>> MAP_OPERATIONS;

  /** Mutating navigable map operations. */
  private static final Map<String, Consumer<NavigableMap<?, ?>>> NAVIGABLE_MAP_OPERATIONS;

  /** Mutating collection operations. */
  private static final Map<String, Consumer<Collection<?>>> COLLECTION_OPERATIONS;

  /** Known immutable set classes. */
  private static final Set<String> IMMUTABLE_SETS;

  /** Known immutable map classes. */
  private static final Set<String> IMMUTABLE_MAPS;

  /** Known immutable list classes. */
  private static final Set<String> IMMUTABLE_LISTS;

  /** Known immutable navigable set classes. */
  private static final Set<String> IMMUTABLE_NAVIGABLE_SETS;

  /** Known immutable navigable map classes. */
  private static final Set<String> IMMUTABLE_NAVIGABLE_MAPS;

  static {
    Set<String> immutableSets = new HashSet<>();
    immutableSets.add("java.util.Collections$EmptySet");
    immutableSets.add("java.util.Collections$EmptySortedSet");
    immutableSets.add("java.util.Collections$SingletonSet");
    IMMUTABLE_SETS = Collections.unmodifiableSet(immutableSets);

    Set<String> immutableMaps = new HashSet<>();
    immutableMaps.add("java.util.Collections$EmptyMap");
    immutableMaps.add("java.util.Collections$EmptySortedMap");
    immutableMaps.add("java.util.Collections$SingletonMap");
    IMMUTABLE_MAPS = Collections.unmodifiableSet(immutableMaps);

    Set<String> immutableLists = new HashSet<>();
    immutableLists.add("java.util.Collections$EmptyList");
    immutableLists.add("java.util.Collections$SingletonList");
    IMMUTABLE_LISTS = Collections.unmodifiableSet(immutableLists);

    Set<String> immutableNavigableSets = new HashSet<>();
    immutableNavigableSets.add("java.util.Collections$EmptyNavigableSet");
    IMMUTABLE_NAVIGABLE_SETS = Collections.unmodifiableSet(immutableNavigableSets);

    Set<String> immutableNavigableMaps = new HashSet<>();
    immutableNavigableMaps.add("java.util.Collections$EmptyNavigableMap");
    IMMUTABLE_NAVIGABLE_MAPS = Collections.unmodifiableSet(immutableNavigableMaps);

    Map<String, Consumer<Collection<?>>> collectionOperations = new LinkedHashMap<>();
    collectionOperations.put("Collection.add(null)", ignoreNullPointerException(c -> c.add(null)));
    collectionOperations.put("Collection.addAll(emptyCollection())", c -> c.addAll(Collections.emptyList()));
    collectionOperations.put("Collection.clear()", Collection::clear);
    collectionOperations.put("Collection.iterator().remove()", c -> c.iterator().remove());
    collectionOperations.put("Collection.remove(null)", ignoreNullPointerException(c -> c.remove(null)));
    collectionOperations.put("Collection.removeAll(emptyCollection())", c -> c.removeAll(Collections.emptyList()));
    collectionOperations.put("Collection.removeIf(element -> true)", c -> c.removeIf(element -> true));
    collectionOperations.put("Collection.retainAll(emptyCollection())", c -> c.retainAll(Collections.emptyList()));
    COLLECTION_OPERATIONS = Collections.unmodifiableMap(collectionOperations);

    Map<String, Consumer<List<?>>> listOperations = new LinkedHashMap<>();
    listOperations.put("List.add(0, null)", ignoreNullPointerException(list -> list.add(0, null)));
    listOperations.put("List.addAll(0, emptyCollection())", list -> list.addAll(0, Collections.emptyList()));
    listOperations.put("List.listIterator().add(null)",
                       ignoreNullPointerException(list -> list.listIterator().add(null)));
    listOperations.put("List.listIterator().remove()", list -> list.listIterator().remove());
    listOperations.put("List.listIterator().set(null)",
                       ignoreNullPointerException(list -> list.listIterator().set(null)));
    listOperations.put("List.remove(0)", list -> list.remove(0));
    listOperations.put("List.replaceAll(identity())", list -> list.replaceAll(identity()));
    listOperations.put("List.set(0, null)",
                       list -> {
                         try {
                           list.set(0, null);
                         } catch (IndexOutOfBoundsException | NullPointerException e) {
                           // mutating operation
                         }
                       });
    listOperations.put("List.sort((o1, o2) -> 0)", list -> list.sort((o1, o2) -> 0));
    LIST_OPERATIONS = Collections.unmodifiableMap(listOperations);

    Map<String, Consumer<NavigableSet<?>>> navigableSetOperations = new LinkedHashMap<>();
    navigableSetOperations.put("NavigableSet.descendingIterator().remove()", set -> set.descendingIterator().remove());
    navigableSetOperations.put("NavigableSet.pollFirst()", NavigableSet::pollFirst);
    navigableSetOperations.put("NavigableSet.pollLast()", NavigableSet::pollLast);
    NAVIGABLE_SET_OPERATIONS = Collections.unmodifiableMap(navigableSetOperations);

    Map<String, Consumer<Map<?, ?>>> mapOperations = new LinkedHashMap<>();
    mapOperations.put("Map.clear()", Map::clear);
    mapOperations.put("Map.compute(null, (k, v) -> v)",
                      ignoreNullPointerException(map -> map.compute(null, (k, v) -> v)));
    mapOperations.put("Map.computeIfAbsent(null, k -> null)",
                      ignoreNullPointerException(map -> map.computeIfAbsent(null, k -> null)));
    mapOperations.put("Map.computeIfPresent(null, (k, v) -> v)",
                      ignoreNullPointerException(map -> map.computeIfPresent(null, (k, v) -> v)));
    mapOperations.put("Map.merge(null, null, (v1, v2) -> v1))",
                      ignoreNullPointerException(map -> map.merge(null, null, (v1, v2) -> v1)));
    mapOperations.put("Map.put(null, null)", ignoreNullPointerException(map -> map.put(null, null)));
    mapOperations.put("Map.putAll(new HashMap<>())", map -> map.putAll(new HashMap<>()));
    mapOperations.put("Map.putIfAbsent(null, null)",
                      ignoreNullPointerException(map -> map.putIfAbsent(null, null)));
    mapOperations.put("Map.replace(null, null, null)",
                      ignoreNullPointerException(map -> map.replace(null, null, null)));
    mapOperations.put("Map.replace(null, null)", ignoreNullPointerException(map -> map.replace(null, null)));
    mapOperations.put("Map.remove(null)", ignoreNullPointerException(map -> map.remove(null)));
    mapOperations.put("Map.remove(null, null)", ignoreNullPointerException(map -> map.remove(null, null)));
    mapOperations.put("Map.replaceAll((k, v) -> v)", map -> map.replaceAll((k, v) -> v));
    MAP_OPERATIONS = Collections.unmodifiableMap(mapOperations);

    Map<String, Consumer<NavigableMap<?, ?>>> navigableMapOperations = new LinkedHashMap<>();
    navigableMapOperations.put("NavigableMap.pollFirstEntry()", NavigableMap::pollFirstEntry);
    navigableMapOperations.put("NavigableMap.pollLastEntry()", NavigableMap::pollLastEntry);
    NAVIGABLE_MAP_OPERATIONS = Collections.unmodifiableMap(navigableMapOperations);
  }

  @Override
  public Optional<String> visitCollection(final Collection<?> target) {
    if (target instanceof List) return visitList((List<?>) target);
    if (target instanceof Set) return visitSet((Set<?>) target);
    return findSupportedMethod(COLLECTION_OPERATIONS, target);
  }

  @Override
  public Optional<String> visitList(final List<?> target) {
    if (IMMUTABLE_LISTS.contains(target.getClass().getName())) return Optional.empty();
    Optional<String> collectionMethod = findSupportedMethod(COLLECTION_OPERATIONS, target);
    return or(collectionMethod, () -> findSupportedMethod(LIST_OPERATIONS, target));
  }

  @Override
  public Optional<String> visitSet(final Set<?> target) {
    if (IMMUTABLE_SETS.contains(target.getClass().getName())) return Optional.empty();
    if (target instanceof NavigableSet) return visitNavigableSet((NavigableSet<?>) target);
    return findSupportedMethod(COLLECTION_OPERATIONS, target);
  }

  @Override
  public Optional<String> visitMap(final Map<?, ?> target) {
    if (IMMUTABLE_MAPS.contains(target.getClass().getName())) return Optional.empty();
    if (target instanceof NavigableMap) return visitNavigableMap((NavigableMap<?, ?>) target);
    return findSupportedMethod(MAP_OPERATIONS, target);
  }

  private Optional<String> visitNavigableSet(final NavigableSet<?> target) {
    if (IMMUTABLE_NAVIGABLE_SETS.contains(target.getClass().getName())) return Optional.empty();
    Optional<String> collectionMethod = findSupportedMethod(COLLECTION_OPERATIONS, target);
    return or(collectionMethod, () -> findSupportedMethod(NAVIGABLE_SET_OPERATIONS, target));
  }

  private Optional<String> visitNavigableMap(final NavigableMap<?, ?> target) {
    if (IMMUTABLE_NAVIGABLE_MAPS.contains(target.getClass().getName())) return Optional.empty();
    Optional<String> mapMethod = findSupportedMethod(MAP_OPERATIONS, target);
    return or(mapMethod, () -> findSupportedMethod(NAVIGABLE_MAP_OPERATIONS, target));
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

  /** This method is first included in Java 9. */
  private static Optional<String> or(final Optional<String> primary, final Supplier<Optional<String>> secondary) {
    return primary.isPresent() ? primary : secondary.get();
  }
}
