/*
 * Created on Jan 15, 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2008-2011 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.assertTrue;

import java.io.Flushable;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for <code>{@link Flushables#flush(Flushable...)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Flushables_flush_Test {

  private static class FlushableStub implements Flushable {
    boolean flushed;
    IOException toThrow;

    public FlushableStub() {}

    public FlushableStub(IOException toThrow) {
      this.toThrow = toThrow;
    }

    @Override
    public void flush() throws IOException {
      flushed = true;
      if (toThrow != null) throw toThrow;
    }
  }

  @Test
  public void should_flush_Flushables() {
    FlushableStub[] toFlush = new FlushableStub[] { new FlushableStub(), new FlushableStub() };
    Flushables.flush(toFlush);
    assertFlushed(toFlush);
  }

  @Test
  public void should_ignore_thrown_errors() {
    FlushableStub[] toFlush = new FlushableStub[] { new FlushableStub(new IOException("")), new FlushableStub() };
    Flushables.flush(toFlush);
    assertFlushed(toFlush);
  }

  @Test
  public void should_ignore_null_Flushables() {
    FlushableStub c = new FlushableStub();
    FlushableStub[] toFlush = new FlushableStub[] { null, c };
    Flushables.flush(toFlush);
    assertFlushed(c);
  }

  private void assertFlushed(FlushableStub... toFlush) {
    for (FlushableStub c : toFlush)
      assertTrue(c.flushed);
  }
}
