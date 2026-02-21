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
package org.assertj.core.api.soft;

import java.util.Optional;

/**
 * {@link DefaultSoftAssertFactory} instances for Java types.
 */
public interface SoftAssertFactories {

  DefaultSoftAssertFactory<Object, SoftObjectAssert<?>> OBJECT = new DefaultSoftAssertFactory<>(Object.class,
                                                                                                SoftObjectAssert::new);
  DefaultSoftAssertFactory<String, SoftStringAssert> STRING = new DefaultSoftAssertFactory<>(String.class, SoftStringAssert::new);

  DefaultSoftAssertFactory<Optional, SoftOptionalAssert<Object>> OPTIONAL = optional(Object.class);

  static <VALUE> DefaultSoftAssertFactory<Optional, SoftOptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new DefaultSoftAssertFactory<>(SoftOptionalAssert<VALUE>::new, Optional.class, resultType);
  }
}
