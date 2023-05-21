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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.description;

import static org.assertj.core.util.Preconditions.checkState;

import java.util.function.Supplier;

/**
 * A text-based description that is evaluated lazily.
 */
public class LazyTextDescription extends Description {

  private Supplier<String> descriptionSupplier;

  public LazyTextDescription(Supplier<String> descriptionSupplier) {
    this.descriptionSupplier = descriptionSupplier;
  }

  @Override
  public String value() {
    checkState(descriptionSupplier != null, "the descriptionSupplier should not be null");
    return descriptionSupplier.get();
  }
}
