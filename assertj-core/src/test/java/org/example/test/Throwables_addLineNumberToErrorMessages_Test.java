/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.example.test;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Throwables.addLineNumberToErrorMessages;

import java.util.List;
import org.junit.jupiter.api.Test;

class Throwables_addLineNumberToErrorMessages_Test {

  @Test
  void should_add_the_line_where_the_error_was_thrown() {
    // GIVEN
    Throwable throwable1 = new Throwable("boom 1");
    Throwable throwable2 = new Throwable("boom 2");
    List<Throwable> errors = list(throwable1, throwable2);
    // WHEN
    List<Throwable> errorsWithLineNumber = addLineNumberToErrorMessages(errors);
    // THEN
    then(errorsWithLineNumber.get(0)).hasMessage(format("boom 1%nat Throwables_addLineNumberToErrorMessages_Test.should_add_the_line_where_the_error_was_thrown(Throwables_addLineNumberToErrorMessages_Test.java:31)"));
    then(errorsWithLineNumber.get(1)).hasMessage(format("boom 2%nat Throwables_addLineNumberToErrorMessages_Test.should_add_the_line_where_the_error_was_thrown(Throwables_addLineNumberToErrorMessages_Test.java:32)"));
  }

}
