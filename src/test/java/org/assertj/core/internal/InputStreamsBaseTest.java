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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.assertj.core.api.AssertionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;


/**
 * Base class for {@link InputStreams} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link InputStreams} attributes appropriately.
 *
 * @author Joel Costigliola
 *
 */
public class InputStreamsBaseTest {

  protected static final AssertionInfo INFO = someInfo();

  protected Diff diff;
  protected Failures failures;
  protected InputStreams inputStreams;

  protected static InputStream actual;
  protected static InputStream expected;
  protected static String expectedString;

  @BeforeAll
  public static void setUpOnce() {
    actual = new ByteArrayInputStream(new byte[0]);
    expected = new ByteArrayInputStream(new byte[0]);
    expectedString = "";
  }

  @BeforeEach
  public void setUp() {
    diff = mock(Diff.class);
    failures = spy(new Failures());
    inputStreams = new InputStreams();
    inputStreams.diff = diff;
    inputStreams.failures = failures;
  }

  protected static void failIfStreamIsOpen(InputStream stream) {
    try {
      assertThat(stream.read()).as("Stream should be closed").isNegative();
    } catch (IOException e) {
      assertThat(e).hasNoCause().hasMessage("Stream closed");
    }
  }

}
