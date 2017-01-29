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
package org.assertj.core.test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

// dummy stream for testing purpose
public class StringStream implements Stream<String> {

  @Override
  public Iterator<String> iterator() {
    return null;
  }

  @Override
  public Spliterator<String> spliterator() {
    return null;
  }

  @Override
  public boolean isParallel() {
    return false;
  }

  @Override
  public Stream<String> sequential() {
    return null;
  }

  @Override
  public Stream<String> parallel() {
    return null;
  }

  @Override
  public Stream<String> unordered() {
    return null;
  }

  @Override
  public Stream<String> onClose(Runnable closeHandler) {
    return null;
  }

  @Override
  public void close() {}

  @Override
  public Stream<String> filter(Predicate<? super String> predicate) {
    return null;
  }

  @Override
  public <R> Stream<R> map(Function<? super String, ? extends R> mapper) {
    return null;
  }

  @Override
  public IntStream mapToInt(ToIntFunction<? super String> mapper) {
    return null;
  }

  @Override
  public LongStream mapToLong(ToLongFunction<? super String> mapper) {
    return null;
  }

  @Override
  public DoubleStream mapToDouble(ToDoubleFunction<? super String> mapper) {
    return null;
  }

  @Override
  public <R> Stream<R> flatMap(Function<? super String, ? extends Stream<? extends R>> mapper) {
    return null;
  }

  @Override
  public IntStream flatMapToInt(Function<? super String, ? extends IntStream> mapper) {
    return null;
  }

  @Override
  public LongStream flatMapToLong(Function<? super String, ? extends LongStream> mapper) {
    return null;
  }

  @Override
  public DoubleStream flatMapToDouble(Function<? super String, ? extends DoubleStream> mapper) {
    return null;
  }

  @Override
  public Stream<String> distinct() {
    return null;
  }

  @Override
  public Stream<String> sorted() {
    return null;
  }

  @Override
  public Stream<String> sorted(Comparator<? super String> comparator) {
    return null;
  }

  @Override
  public Stream<String> peek(Consumer<? super String> action) {
    return null;
  }

  @Override
  public Stream<String> limit(long maxSize) {
    return null;
  }

  @Override
  public Stream<String> skip(long n) {
    return null;
  }

  @Override
  public void forEach(Consumer<? super String> action) {}

  @Override
  public void forEachOrdered(Consumer<? super String> action) {}

  @Override
  public Object[] toArray() {
    return null;
  }

  @Override
  public <A> A[] toArray(IntFunction<A[]> generator) {
    return null;
  }

  @Override
  public String reduce(String identity, BinaryOperator<String> accumulator) {
    return null;
  }

  @Override
  public Optional<String> reduce(BinaryOperator<String> accumulator) {
    return null;
  }

  @Override
  public <U> U reduce(U identity, BiFunction<U, ? super String, U> accumulator, BinaryOperator<U> combiner) {
    return null;
  }

  @Override
  public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super String> accumulator, BiConsumer<R, R> combiner) {
    return null;
  }

  @Override
  public <R, A> R collect(Collector<? super String, A, R> collector) {
    return null;
  }

  @Override
  public Optional<String> min(Comparator<? super String> comparator) {
    return null;
  }

  @Override
  public Optional<String> max(Comparator<? super String> comparator) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public boolean anyMatch(Predicate<? super String> predicate) {
    return false;
  }

  @Override
  public boolean allMatch(Predicate<? super String> predicate) {
    return false;
  }

  @Override
  public boolean noneMatch(Predicate<? super String> predicate) {
    return false;
  }

  @Override
  public Optional<String> findFirst() {
    return null;
  }

  @Override
  public Optional<String> findAny() {
    return null;
  }
}