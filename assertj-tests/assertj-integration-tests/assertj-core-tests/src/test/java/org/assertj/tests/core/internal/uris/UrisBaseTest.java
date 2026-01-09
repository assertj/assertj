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
package org.assertj.tests.core.internal.uris;

import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Uris;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base test class for {@link java.net.URI} tests.
 *
 * @author Alexander Bischof
 */
public abstract class UrisBaseTest {

  protected Failures failures;
  protected Uris uris;
  protected AssertionInfo info;

  @BeforeEach
  void setUp() throws IllegalAccessException {
    failures = spy(Failures.instance());
    uris = Uris.instance();
    writeField(uris, "failures", failures, true);
    info = someInfo();
  }

}
