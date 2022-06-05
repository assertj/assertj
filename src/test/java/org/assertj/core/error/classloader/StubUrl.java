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

package org.assertj.core.error.classloader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Facility to create URLs safely without them having to resolve to specific runtime protocols.
 *
 * <p>This maps URI methods to URL methods of the same name and parameter types where possible,
 * returning a smart null if not found and nothing else is stubbed.
 *
 * @author Ashley Scopes
 */
final class StubUrl {

  private StubUrl() {
  }

  static URL create(String content) {
    URI uri = URI.create(content);
    return mock(
      URL.class,
      withSettings()
        .stubOnly()
        .defaultAnswer(new UriToUrlDefaultAnswer<>(uri))
    );
  }

  private static final class UriToUrlDefaultAnswer<T> implements Answer<T> {

    private final URI uri;

    private UriToUrlDefaultAnswer(URI uri) {
      this.uri = uri;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T answer(InvocationOnMock invocation) throws Throwable {
      try {
        Method method = uri.getClass().getMethod(
          invocation.getMethod().getName(),
          invocation.getMethod().getParameterTypes()
        );

        if (invocation.getMethod().getReturnType().isAssignableFrom(method.getReturnType())) {
          return (T) method.invoke(uri, invocation.getArguments());
        }

      } catch (NoSuchMethodException ex) {
        // Ignore
      }

      return (T) Answers.RETURNS_SMART_NULLS.answer(invocation);
    }
  }
}
