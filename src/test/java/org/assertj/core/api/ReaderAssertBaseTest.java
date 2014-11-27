/*
 * Created on Aug 02, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

import static org.mockito.Mockito.mock;

import java.io.Reader;
import java.io.StringReader;

import org.assertj.core.internal.Readers;

/**
 * Base class for {@link org.assertj.core.api.ReaderAssert} tests.
 *
 * @author Joel Costigliola
 * @author Bartosz Bierkowski
 */
public abstract class ReaderAssertBaseTest extends BaseTestTemplate<ReaderAssert, Reader> {
  protected Readers readers;

  @Override
  protected ReaderAssert create_assertions() {
    return new ReaderAssert(new StringReader("a"));
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    readers = mock(Readers.class);
    assertions.readers = readers;
  }

  protected Readers getReaders(ReaderAssert someAssertions) {
    return someAssertions.readers;
  }

}
