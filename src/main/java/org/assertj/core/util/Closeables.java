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

import java.io.Closeable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods related to {@link Closeable}.
 * 
 * @author Yvonne Wang
 */
public final class Closeables {
  private static final Logger logger = Logger.getLogger(Closeables.class.getCanonicalName());

  /**
   * Closes the given {@link Closeable}s, ignoring any thrown exceptions.
   * 
   * @param closeables the {@code Closeable}s to close.
   */
  public static void closeQuietly(Closeable... closeables) {
    for (Closeable c : closeables) {
      closeCloseable(c);
    }
  }

  private static void closeCloseable(Closeable c) {
    if (c == null) {
      return;
    }
    try {
      c.close();
    } catch (Throwable t) {
      logger.log(Level.WARNING, "Error ocurred while closing " + c, t);
    }
  }

  private Closeables() {}
}
