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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Types.castClass;

import java.net.URL;
import org.assertj.core.util.Lists;

/**
 * Assertion methods for {@link ClassLoader class loaders}.
 * <p>
 * To create a new instance of this class, invoke
 * <code>{@link Assertions#assertThat(ClassLoader)}</code></p>
 *
 * @author Ashley Scopes
 * @since 3.24.0
 */
public class ClassLoaderAssert extends AbstractClassLoaderAssert<ClassLoaderAssert> {

  /**
   * Initialize this assertion.
   *
   * @param actual the classloader to perform assertions upon.
   */
  protected ClassLoaderAssert(ClassLoader actual) {
    super(actual, ClassLoaderAssert.class);
  }

  @Override
  protected ClassLoaderAssert toAssert(ClassLoader classLoader) {
    return new ClassLoaderAssert(classLoader);
  }

  @Override
  protected ClassAssert toAssert(Class<?> aClass) {
    return new ClassAssert(aClass);
  }

  @Override
  protected UrlAssert toAssert(URL url) {
    return new UrlAssert(url);
  }

  @Override
  protected ByteArrayAssert toAssert(byte[] array) {
    return new ByteArrayAssert(array);
  }

  @Override
  protected ThrowableAssert<? extends Throwable> toAssert(Throwable ex) {
    return new ThrowableAssert<>(ex);
  }

  @Override
  protected AbstractIterableAssert<?, ? extends Iterable<URL>, URL, ? extends UrlAssert> toUrlIterableAssert(
    Iterable<URL> arrays
  ) {
    return new FactoryBasedNavigableIterableAssert<>(
      Lists.newArrayList(arrays),
      castClass(FactoryBasedNavigableIterableAssert.class),
      this::toAssert
    );
  }

  @Override
  protected AbstractIterableAssert<?, ? extends Iterable<byte[]>, byte[], ? extends ByteArrayAssert> toByteArrayIterableAssert(
    Iterable<byte[]> byteArrays
  ) {
    return new FactoryBasedNavigableIterableAssert<>(
      byteArrays,
      castClass(FactoryBasedNavigableIterableAssert.class),
      this::toAssert
    );
  }
}
