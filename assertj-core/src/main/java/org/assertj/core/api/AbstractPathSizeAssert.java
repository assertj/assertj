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

import static java.lang.String.format;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.annotation.CheckReturnValue;

/**
 * Base class for path size assertions.
 *
 * @param <ORIGIN> originating path assertion
 * @since 3.28.0
 */
public abstract class AbstractPathSizeAssert<ORIGIN extends AbstractPathAssert<ORIGIN>>
    extends AbstractLongAssert<AbstractPathSizeAssert<ORIGIN>> {

  private final AbstractPathAssert<ORIGIN> originAssert;

  /**
   * Creates a new instance from an origin {@link AbstractPathAssert} instance.
   *
   * @param originAssert the origin {@link AbstractPathAssert} that initiated the navigation.
   */
  protected AbstractPathSizeAssert(AbstractPathAssert<ORIGIN> originAssert) {
    super(readSize(originAssert.actual), AbstractPathSizeAssert.class);
    this.originAssert = originAssert;
  }

  /**
   * Returns to the origin {@link AbstractPathAssert} instance that initiated the navigation.
   * <p>
   * Example:
   * <pre><code class='java'> Path path = Files.write(Files.createTempFile(&quot;tmp&quot;, &quot;bin&quot;), new byte[] {1, 1});
   *
   * assertThat(path).size().isGreaterThan(1L).isLessThan(5L)
   *                 .returnToPath().hasBinaryContent(new byte[] {1, 1});</code></pre>
   *
   * @return the origin {@link AbstractPathAssert} instance.
   */
  @CheckReturnValue
  public AbstractPathAssert<ORIGIN> returnToPath() {
    return originAssert;
  }

  private static long readSize(Path path) {
    try {
      return Files.size(path);
    } catch (IOException e) {
      throw new UncheckedIOException(format("Failed to read %s size", path), e);
    }
  }

}
