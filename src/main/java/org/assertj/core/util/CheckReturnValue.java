/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Findbugs handles any annotation with name "CheckReturnValue" in return value check.
 *
 * @see <a href="https://github.com/findbugsproject/findbugs/blob/264ae7baf890d2b347d91805c90057062b5dcb1e/findbugs/src/java/edu/umd/cs/findbugs/detect/BuildCheckReturnAnnotationDatabase.java#L120">Findbugs source code</a>
 */
@Target({
        ElementType.CONSTRUCTOR,
        ElementType.METHOD,
        ElementType.PACKAGE,
        ElementType.TYPE,
})
@Retention(RetentionPolicy.CLASS)
public @interface CheckReturnValue {
}