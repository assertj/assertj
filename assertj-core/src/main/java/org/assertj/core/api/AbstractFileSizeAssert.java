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

import org.assertj.core.annotation.CheckReturnValue;

/**
 * Base class for file size assertions.
 * 
 * @since 3.22.0
 */
public abstract class AbstractFileSizeAssert<SELF extends AbstractFileAssert<SELF>>
    extends AbstractLongAssert<AbstractFileSizeAssert<SELF>> {

  private final AbstractFileAssert<SELF> originAssert;

  /**
   * Creates a new instance from an origin {@link AbstractFileAssert} instance.
   *
   * @param originAssert the origin {@link AbstractFileAssert} that initiated the navigation.
   * @since 3.28.0
   */
  protected AbstractFileSizeAssert(AbstractFileAssert<SELF> originAssert) {
    super(originAssert.actual.length(), AbstractFileSizeAssert.class);
    this.originAssert = originAssert;
  }

  /**
   * @deprecated use {@link #AbstractFileSizeAssert(AbstractFileAssert)} instead.
   */
  @Deprecated
  protected AbstractFileSizeAssert(Long actualFileSize, Class<?> selfType) {
    super(actualFileSize, selfType);
    this.originAssert = null;
  }

  /**
   * Returns to the origin {@link AbstractFileAssert} instance that initiated the navigation.
   * <p>
   * Example:
   * <pre><code class='java'> File file = File.createTempFile(&quot;tmp&quot;, &quot;bin&quot;);
   * Files.write(file.toPath(), new byte[] {1, 1});
   *
   * assertThat(file).size().isGreaterThan(1L).isLessThan(5L)
   *                 .returnToFile().hasBinaryContent(new byte[] {1, 1});</code></pre>
   *
   * @return the origin {@link AbstractFileAssert} instance.
   */
  @CheckReturnValue
  public AbstractFileAssert<SELF> returnToFile() {
    if (originAssert == null) {
      throw new IllegalStateException("No origin available. Was this assert created from its deprecated constructor?");
    }
    return originAssert;
  }

}
