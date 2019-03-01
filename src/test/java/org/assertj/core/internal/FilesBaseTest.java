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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.BeforeEach;


/**
 * Base class for testing <code>{@link Files}</code>, set up diff and failures attributes (which is why it is in
 * <code>org.assertj.core.internal</code> package.
 *
 * @author Joel Costigliola
 */
public class FilesBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected File actual;
  protected Failures failures;
  protected Files files;
  protected Files unMockedFiles;
  protected Diff diff;
  protected Delta<String> delta;
  protected BinaryDiff binaryDiff;
  protected NioFilesWrapper nioFilesWrapper;

  @BeforeEach
  public void setUp() {
    actual = mock(File.class);
    failures = spy(new Failures());
    files = new Files();
    unMockedFiles = new Files();
    files.failures = failures;
    diff = mock(Diff.class);
    delta = mock(Delta.class);
    when(delta.toString()).thenReturn("Extra lines at line 2 : [line1a, line1b]");
    files.diff = diff;
    binaryDiff = mock(BinaryDiff.class);
    files.binaryDiff = binaryDiff;
    nioFilesWrapper = mock(NioFilesWrapper.class);
    files.nioFilesWrapper = nioFilesWrapper;
  }

  protected static void failIfStreamIsOpen(InputStream stream) {
    try {
      assertThat(stream.read()).as("Stream should be closed").isNegative();
    } catch (IOException e) {
      assertThat(e).hasNoCause().hasMessage("Stream closed");
    }
  }

  protected File mockPath(String... names) {
    String name = names[names.length - 1];
    File file = mock(File.class);
    given(file.getName()).willReturn(name);

    Path path = mock(Path.class);
    given(path.getFileName()).willReturn(path);
    given(path.toString()).willReturn(name);

    given(file.toPath()).willReturn(path);
    given(path.toFile()).willReturn(file);

    FileSystem fileSystem = mock(FileSystem.class);
    given(path.getFileSystem()).willReturn(fileSystem);
    return file;
  }

  protected File mockRegularFile(String... names) {
    File path = mockPath(names);
    given(path.exists()).willReturn(true);
    given(path.isFile()).willReturn(true);
    given(path.list()).willReturn(null);
    try {
      given(nioFilesWrapper.newInputStream(path.toPath())).willReturn(new ByteArrayInputStream(new byte[0]));
    } catch (IOException e) {
      assertThat(e).describedAs("Should not happen").isNull();
    }
    return path;
  }

  protected File mockDirectory(List<File> directoryItems, String... names) {
    File path = mockPath(names);
    given(path.exists()).willReturn(true);
    given(path.isDirectory()).willReturn(true);
    String[] listNames = directoryItems.stream().map(File::getName).toArray(String[]::new);
    Map<String, File> list = Stream
      .of(listNames)
      .distinct()
      .collect(Collectors.toMap(
        Function.identity(),
        name -> {
          File inner = mockRegularFile(name);
          given(inner.getParentFile()).willReturn(path);
          return inner;
        }
      ));
    given(path.list()).willReturn(listNames);
    given(path.listFiles(any(FileFilter.class))).will(inv -> {
      FileFilter filter = inv.getArgument(0);
      List<File> result = new ArrayList<>(listNames.length);
      for (String name : listNames) {
        File file = list.get(name);
        if (filter.accept(file)) {
          result.add(file);
        }
      }
      return result.toArray(new File[0]);
    });
    given(path.listFiles(any(FilenameFilter.class))).will(inv -> {
      FilenameFilter filter = inv.getArgument(0);
      List<File> result = new ArrayList<>(listNames.length);
      for (String name : listNames) {
        if (filter.accept(path, name)) {
          result.add(list.get(name));
        }
      }
      return result.toArray(new File[0]);
    });
    return path;
  }
}
