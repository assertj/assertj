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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.future;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;
import static org.assertj.core.util.Strings.concat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.StackTraceUtils;

class FailedCompletableFutureRepresentation extends StandardRepresentation {

  @Override
  protected String toStringOf(CompletableFuture<?> future) {
    String className = future.getClass().getSimpleName();
    try {
      future.join();
      return EMPTY_STRING;
    } catch (CompletionException e) {
      return concat(className, "\\[Failed: ", toStringOf(e.getCause()), "\\]\n", StackTraceUtils.getStackTraceRegex());
    }
  }
}
