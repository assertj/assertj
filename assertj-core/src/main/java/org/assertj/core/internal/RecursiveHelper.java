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
package org.assertj.core.internal;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static org.assertj.core.util.Arrays.isArray;

public class RecursiveHelper {
  public static boolean isContainer(Object o) {
    return o instanceof Iterable ||
           o instanceof Map ||
           o instanceof Optional ||
           o instanceof AtomicReference ||
           o instanceof AtomicReferenceArray ||
           o instanceof AtomicBoolean ||
           o instanceof AtomicInteger ||
           o instanceof AtomicIntegerArray ||
           o instanceof AtomicLong ||
           o instanceof AtomicLongArray ||
           isArray(o);
  }
}
