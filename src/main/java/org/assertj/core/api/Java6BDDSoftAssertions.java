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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.groups.Properties.extractProperty;

import java.util.List;

import org.assertj.core.error.AssertionErrorCreator;

/**
 * @deprecated For Android compatible assertions use the latest assertj 2.x version which is based on Java 7 only.
 * <p>
 * BDD-style Android-compatible soft assertions. Duplicated from {@link BDDSoftAssertions}.
 *
 * @see BDDSoftAssertions
 *
 * @since 2.5.0 / 3.5.0
 */
@Deprecated
public class Java6BDDSoftAssertions extends Java6AbstractBDDSoftAssertions {

  private AssertionErrorCreator assertionErrorCreator = new AssertionErrorCreator();

  /**
   * Verifies that no proxied assertion methods have failed.
   *
   * @throws SoftAssertionError if any proxied assertion objects threw
   */
  public void assertAll() {
    List<Throwable> errors = errorsCollected();
    if (!errors.isEmpty()) {
      assertionErrorCreator.tryThrowingMultipleFailuresError(errors);
      throw new SoftAssertionError(extractProperty("message", String.class).from(errors));
    }
  }
}
