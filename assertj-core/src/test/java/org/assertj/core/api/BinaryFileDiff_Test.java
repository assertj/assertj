/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;

import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BinaryFileDiff_Test {

  private final Path actual = mock(Path.class);
  private final Path expected = mock(Path.class);
  private final FileSystemProvider provider = mock(FileSystemProvider.class);
  private final InputStream actualStream = mock(InputStream.class);
  private final InputStream expectedStream = mock(InputStream.class);

  @BeforeEach
  void set_up() throws IOException {
    FileSystem fileSystem = mock(FileSystem.class);
    given(actual.getFileSystem()).willReturn(fileSystem);
    given(expected.getFileSystem()).willReturn(fileSystem);
    given(fileSystem.provider()).willReturn(provider);
    FileStore fileStore = mock(FileStore.class);
    given(fileStore.getTotalSpace()).willReturn(1024L);
    given(provider.getFileStore(actual)).willReturn(fileStore);
    given(provider.getFileStore(expected)).willReturn(fileStore);
    given(provider.newInputStream(actual)).willReturn(actualStream);
    given(provider.newInputStream(expected)).willReturn(expectedStream);
  }

  @ParameterizedTest
  @CsvSource({ "1, 4294967296", "4294967296, 1" })
  void should_fail_on_unequal_regular_file_sizes_without_reading_contents(long actualSize, long expectedSize) throws IOException {
    // GIVEN
    givenFileSizes(actualSize, expectedSize);
    // WHEN
    ErrorMessageFactory mismatch = BinaryFileDiff.compare(actual, expected, Path.of("actual"));
    // THEN
    then(mismatch.create()).contains("expected: %s bytes".formatted(expectedSize),
                                     "actual: %s bytes".formatted(actualSize));
    verify(actualStream).close();
    verify(expectedStream).close();
    verify(provider).newInputStream(actual);
    verify(provider).newInputStream(expected);
    verify(provider).readAttributes(actual, BasicFileAttributes.class);
    verify(provider).readAttributes(expected, BasicFileAttributes.class);
    verify(provider).getFileStore(actual);
    verify(provider).getFileStore(expected);
    verifyNoMoreInteractions(actualStream, expectedStream, provider);
  }

  @ParameterizedTest
  @CsvSource({ "0, true", "0, false", "4096, true", "4096, false" })
  void should_pass_when_a_file_store_without_capacity_reports_a_nominal_size(long reportedSize,
                                                                             boolean virtualFileIsActual) throws IOException {
    // GIVEN
    givenFileSizes(virtualFileIsActual ? reportedSize : 1, virtualFileIsActual ? 1 : reportedSize);
    FileStore virtualStore = mock(FileStore.class);
    given(virtualStore.getTotalSpace()).willReturn(0L);
    Path virtualFile = virtualFileIsActual ? actual : expected;
    given(provider.getFileStore(virtualFile)).willReturn(virtualStore);
    givenMatchingFileContents();
    // WHEN
    ErrorMessageFactory mismatch = BinaryFileDiff.compare(actual, expected, Path.of("actual"));
    // THEN
    then(mismatch).isNull();
  }

  @ParameterizedTest
  @ValueSource(classes = { IOException.class, UnsupportedOperationException.class, SecurityException.class })
  void should_compare_contents_when_the_file_store_cannot_be_identified(Class<? extends Throwable> failure) throws IOException {
    // GIVEN
    givenFileSizes(0, 1);
    given(provider.getFileStore(expected)).willThrow(failure);
    givenMatchingFileContents();
    // WHEN
    ErrorMessageFactory mismatch = BinaryFileDiff.compare(actual, expected, Path.of("actual"));
    // THEN
    then(mismatch).isNull();
  }

  @Test
  void should_pass_and_close_both_streams_when_contents_are_equal() throws IOException {
    // GIVEN
    givenFileSizes(0, 0);
    // WHEN
    ErrorMessageFactory mismatch = BinaryFileDiff.compare(actual, expected, Path.of("actual"));
    // THEN
    then(mismatch).isNull();
    verify(actualStream).close();
    verify(expectedStream).close();
  }

  @ParameterizedTest
  @CsvSource({ "0, 1, 0x0, EOF", "1, 0, EOF, 0x0" })
  void should_fail_if_stream_lengths_differ_after_equal_sizes_were_reported(int actualLength,
                                                                            int expectedLength,
                                                                            String expectedByte,
                                                                            String actualByte) throws IOException {
    // GIVEN
    givenFileSizes(1, 1);
    given(provider.newInputStream(actual)).willReturn(new ByteArrayInputStream(new byte[actualLength]));
    given(provider.newInputStream(expected)).willReturn(new ByteArrayInputStream(new byte[expectedLength]));
    // WHEN
    ErrorMessageFactory mismatch = BinaryFileDiff.compare(actual, expected, Path.of("actual"));
    // THEN
    then(mismatch.create()).contains("at offset 0, expecting:%n  \"%s\"%nbut was:%n  \"%s\"".formatted(expectedByte,
                                                                                                       actualByte));
  }

  @Test
  void should_fail_and_close_both_streams_if_reading_fails() throws IOException {
    // GIVEN
    IOException failure = new IOException("read failed");
    givenFileSizes(1, 1);
    given(expectedStream.readNBytes(any(byte[].class), eq(0), anyInt())).willThrow(failure);
    // WHEN
    Throwable thrown = catchThrowable(() -> BinaryFileDiff.compare(actual, expected, Path.of("actual")));
    // THEN
    then(thrown).isSameAs(failure);
    verify(actualStream).close();
    verify(expectedStream).close();
  }

  @Test
  void should_fail_and_close_the_expected_stream_if_opening_the_actual_stream_fails() throws IOException {
    // GIVEN
    IOException failure = new IOException("open failed");
    given(provider.newInputStream(actual)).willThrow(failure);
    // WHEN
    Throwable thrown = catchThrowable(() -> BinaryFileDiff.compare(actual, expected, Path.of("actual")));
    // THEN
    then(thrown).isSameAs(failure);
    verify(expectedStream).close();
    verifyNoMoreInteractions(expectedStream, actualStream);
  }

  @Test
  void should_fail_and_close_both_streams_if_getting_file_attributes_fails() throws IOException {
    // GIVEN
    IOException failure = new IOException("attributes unavailable");
    given(provider.readAttributes(expected, BasicFileAttributes.class)).willThrow(failure);
    // WHEN
    Throwable thrown = catchThrowable(() -> BinaryFileDiff.compare(actual, expected, Path.of("actual")));
    // THEN
    then(thrown).isSameAs(failure);
    verify(actualStream).close();
    verify(expectedStream).close();
    verifyNoMoreInteractions(actualStream, expectedStream);
  }

  @Test
  void should_fail_and_close_the_expected_stream_if_closing_the_actual_stream_fails() throws IOException {
    // GIVEN
    IOException actualCloseFailure = new IOException("actual close failed");
    IOException expectedCloseFailure = new IOException("expected close failed");
    givenFileSizes(0, 0);
    willThrow(actualCloseFailure).given(actualStream).close();
    willThrow(expectedCloseFailure).given(expectedStream).close();
    // WHEN
    Throwable thrown = catchThrowable(() -> BinaryFileDiff.compare(actual, expected, Path.of("actual")));
    // THEN
    then(thrown).isSameAs(actualCloseFailure);
    then(thrown.getSuppressed()).containsExactly(expectedCloseFailure);
    verify(actualStream).close();
    verify(expectedStream).close();
  }

  private void givenMatchingFileContents() throws IOException {
    given(provider.newInputStream(actual)).willReturn(new ByteArrayInputStream(new byte[] { 1 }));
    given(provider.newInputStream(expected)).willReturn(new ByteArrayInputStream(new byte[] { 1 }));
  }

  private void givenFileSizes(long actualSize, long expectedSize) throws IOException {
    BasicFileAttributes actualAttributes = regularFile(actualSize);
    BasicFileAttributes expectedAttributes = regularFile(expectedSize);
    given(provider.readAttributes(actual, BasicFileAttributes.class)).willReturn(actualAttributes);
    given(provider.readAttributes(expected, BasicFileAttributes.class)).willReturn(expectedAttributes);
  }

  private static BasicFileAttributes regularFile(long size) {
    BasicFileAttributes attributes = mock(BasicFileAttributes.class);
    given(attributes.isRegularFile()).willReturn(true);
    given(attributes.size()).willReturn(size);
    return attributes;
  }
}
