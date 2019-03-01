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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.PathsBaseTest;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;

import com.google.common.collect.Iterators;

public class MockPathsBaseTest extends PathsBaseTest {

  static final AssertionInfo INFO = someInfo();

  Path actual;
  Path other;

  @BeforeEach
  public void init() {
    actual = mock(Path.class);
    other = mock(Path.class);
  }

  static void failIfStreamIsOpen(InputStream stream) {
    try {
      assertThat(stream.read()).as("Stream should be closed").isNegative();
    } catch (IOException e) {
      assertThat(e).hasNoCause().hasMessage("Stream closed");
    }
  }

  static <T> void failIfStreamIsOpen(DirectoryStream<T> stream) {
    try {
      long openCount = mockingDetails(stream).getInvocations().stream()
                                             .filter(inv -> inv.getMethod().getName().equals("iterator"))
                                             .count();
      verify(stream, times((int) openCount)).close();
    } catch (IOException e) {
      fail("Should not happen");
    }
  }

  static DirectoryStream<Path> directoryStream(List<Path> directoryItems) {
    DirectoryStream<Path> stream = mock(DirectoryStream.class);
    given(stream.iterator()).will(inv -> directoryItems.iterator());
    given(stream.spliterator()).willCallRealMethod();
    return stream;
  }

  private DirectoryStream<Path> filterStream(Predicate<Path> filter, DirectoryStream<Path> source) throws IOException {
    DirectoryStream<Path> stream = mock(DirectoryStream.class);
    given(stream.iterator()).will(inv -> Iterators.filter(source.iterator(), filter::test));
    given(stream.spliterator()).willCallRealMethod();
    willAnswer(inv -> {
      source.close();
      return null;
    }).given(stream).close();
    return stream;
  }

  static Path mockPath(String... names) {
    Path path = mock(Path.class);
    given(path.toString()).willReturn(Strings.join(names).with(File.separator));
    if (names.length > 1) {
      Path filename = mockPath(names[names.length - 1]);
      given(path.getFileName()).willReturn(filename);
      given(path.getParent()).will(inv -> mockPath(Arrays.copyOf(names, names.length - 1)));
    } else {
      given(path.getFileName()).willReturn(path);
      given(path.getParent()).willReturn(null);
    }
    given(path.getNameCount()).willReturn(names.length);
    given(path.getName(anyInt())).will(inv -> names[(int) inv.getArgument(0)]);
    return path;
  }

  Path mockRegularFile(String... names) {
    Path path = mockPath(names);
    given(nioFilesWrapper.exists(path)).willReturn(true);
    given(nioFilesWrapper.isRegularFile(path)).willReturn(true);
    try {
      given(nioFilesWrapper.newInputStream(path)).willReturn(new ByteArrayInputStream(new byte[0]));
    } catch (IOException e) {
      fail("Should not happen");
    }
    return path;
  }

  Path mockDirectory(String name, DirectoryStream<Path> directoryItems) {
    Path path = mockPath(name);
    given(nioFilesWrapper.exists(path)).willReturn(true);
    given(nioFilesWrapper.isDirectory(path)).willReturn(true);
    try {
      given(nioFilesWrapper.newDirectoryStream(eq(path), any())).will(inv -> filterStream(inv.getArgument(1), directoryItems));
    } catch (IOException e) {
      fail("Should not happen");
    }
    return path;
  }

  Path mockDirectory(String name, List<Path> paths) {
    DirectoryStream<Path> directoryItems = directoryStream(paths);
    Path path = mockPath(name);
    given(nioFilesWrapper.exists(path)).willReturn(true);
    given(nioFilesWrapper.isDirectory(path)).willReturn(true);
    try {
      given(nioFilesWrapper.newDirectoryStream(eq(path), any())).will(inv -> filterStream(inv.getArgument(1), directoryItems));
    } catch (IOException e) {
      fail("Should not happen");
    }
    return path;
  }
}
