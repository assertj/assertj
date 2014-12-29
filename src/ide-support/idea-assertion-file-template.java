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
 * Copyright 2012-2014 the original author or authors.
 */
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import org.assertj.core.api.AbstractAssert;

#parse("File Header.java")
public class ${NAME} extends AbstractAssert<${NAME}, ${Class_being_asserted}> {

  private ${NAME}(${Class_being_asserted} actual) {
    super(actual, ${NAME}.class);
  }

  public static ${NAME} assertThat(${Class_being_asserted} actual) {
    return new ${NAME}(actual);
  }
}
