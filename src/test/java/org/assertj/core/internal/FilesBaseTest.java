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

import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.File;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.diff.Delta;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for testing <code>{@link Files}</code>, set up diff and failures attributes (which is why it is in
 * <code>org.assertj.core.internal</code> package.
 * 
 * @author Joel Costigliola
 */
public class FilesBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected File actual;
  protected Failures failures;
  protected Files files;
  protected Files unMockedFiles;
  protected Diff diff;
  protected Delta<String> delta;
  protected BinaryDiff binaryDiff;

  @SuppressWarnings("unchecked")
  @Before
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
  }

}
