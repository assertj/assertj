/*
 * Created on May 7, 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2013 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Assertion methods for {@code Class}es.
 * <p>
 * To create a new instance of this class, invoke <code>{@link org.assertj.core.api.Assertions#assertThat(Class)}</code>
 * </p>
 * 
 * @author William Delanoue
 * @author Mikhail Mazursky
 */
public class ClassAssert extends AbstractClassAssert<ClassAssert> {

  protected ClassAssert(Class<?> actual) {
    super(actual, ClassAssert.class);
  }
}
