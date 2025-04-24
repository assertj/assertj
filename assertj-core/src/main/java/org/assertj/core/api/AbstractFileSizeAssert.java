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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Base class for file size assertions.
 * 
 * @since 3.22.0
 */
public abstract class AbstractFileSizeAssert<SELF extends AbstractFileAssert<SELF>>
    extends AbstractLongAssert<AbstractFileSizeAssert<SELF>> {

  protected AbstractFileSizeAssert(Long actualFileSize, Class<?> selfType) {
    super(actualFileSize, selfType);
  }

  /**
   * Returns to the file on which we ran size assertions on.
   * <p>
   * Example:
   * <pre><code class='java'> File file = File.createTempFile(&quot;tmp&quot;, &quot;bin&quot;);
   * Files.write(file.toPath(), new byte[] {1, 1});
   *
   * assertThat(file).size().isGreaterThan(1L).isLessThan(5L)
   *                 .returnToFile().hasBinaryContent(new byte[] {1, 1});</code></pre>
   *    
   * @return file assertions. 
   */
  public abstract AbstractFileAssert<SELF> returnToFile();
}
