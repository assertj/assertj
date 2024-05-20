/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SecureDirectoryStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.IterableAssert;
import org.junit.jupiter.api.Test;

;

/**
 * Checks that we can fail an assertion on DirectoryStream types without accidentally
 * dereferencing the items in the iterable. This is important for this class as the
 * iterators provided by directory streams can only be used once. API contracts
 * allow this class to throw exceptions if {@code #iterator()} is called multiple times.
 *
 * @author Ashley Scopes
 */
public class IterableAssert_doNotBreakOnFailingTestsForDirectoryStreams {

  @Test
  void canPerformAssertionsOnDirectoryStream() {
    // Given
    OneShotDirectoryStream stream = new OneShotDirectoryStream();
    IterableAssert<Path> assertions = new IterableAssert<>(stream);

    // Then
    thenNoException().isThrownBy(assertions::isNotNull);
    then(stream.iteratorCount).hasValue(0);
    then(stream.closed).isFalse();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Test
  void canFailAssertionsOnIterableWithoutRecallingCallingIterator() {
    // Given
    OneShotDirectoryStream stream = new OneShotDirectoryStream();
    IterableAssert<Path> assertions = new IterableAssert<>(stream);

    // When
    // Some code that makes use of the iterator() method prior to running the assertions
    stream.iterator();

    // Then
    expectAssertionError(() -> assertions.isInstanceOf(SecureDirectoryStream.class));
    then(stream.iteratorCount).hasValue(1);
    then(stream.closed).isFalse();
  }

  static class OneShotDirectoryStream implements DirectoryStream<Path> {
    final AtomicInteger iteratorCount = new AtomicInteger(0);
    final AtomicBoolean closed = new AtomicBoolean(false);

    @Override
    public Iterator<Path> iterator() {
      if (closed.get()) {
        throw new IllegalStateException("Stream is already closed");
      }

      if (iteratorCount.getAndIncrement() >= 1) {
        throw new IllegalStateException("You cant call #iterator() multiple times");
      }

      List<Path> paths = Arrays.asList(
                                       Paths.get("foo", "bar"),
                                       Paths.get("baz", "bork"),
                                       Paths.get("qux", "quxx"));

      return paths.iterator();
    }

    @Override
    public void close() throws IOException {
      closed.set(true);
    }
  }
}
