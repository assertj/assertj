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

/**
 * A stubbed class loader to use in tests.
 *
 * @author Ashley Scopes
 */
final class StubClassLoader extends ClassLoader {

  StubClassLoader(ClassLoader parent) {
    super(parent);
  }

  StubClassLoader() {
    super(null);
  }

  String getExpectedStandardRepresentation() {
    String hashCode = Integer.toHexString(System.identityHashCode(this));
    return getClass().getSimpleName() + "[id=" + hashCode + "]";
  }
}
