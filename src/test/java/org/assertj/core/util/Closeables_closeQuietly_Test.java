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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Closeable;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for {@link Closeables#closeQuietly(Closeable...)}.
 * 
 * @author Yvonne Wang
 */
public class Closeables_closeQuietly_Test {
  @Test
  public void should_close_Closeables() {
    CloseableStub[] toClose = new CloseableStub[] { new CloseableStub(), new CloseableStub() };
    Closeables.closeQuietly(toClose);
    assertClosed(toClose);
  }

  @Test
  public void should_ignore_thrown_errors() {
    CloseableStub[] toClose = new CloseableStub[] { new CloseableStub(new IOException("")), new CloseableStub() };
    Closeables.closeQuietly(toClose);
    assertClosed(toClose);
  }

  @Test
  public void should_ignore_null_Closeables() {
    CloseableStub c = new CloseableStub();
    CloseableStub[] toClose = new CloseableStub[] { null, c };
    Closeables.closeQuietly(toClose);
    assertClosed(c);
  }

  private void assertClosed(CloseableStub... supposelyClosed) {
    for (CloseableStub c : supposelyClosed) {
      assertThat(c.closed).isTrue();
    }
  }

  private static class CloseableStub implements Closeable {
    boolean closed;
    IOException toThrow;

    public CloseableStub() {}

    public CloseableStub(IOException toThrow) {
      this.toThrow = toThrow;
    }

    @Override
    public void close() throws IOException {
      closed = true;
      if (toThrow != null) {
        throw toThrow;
      }
    }
  }
}
