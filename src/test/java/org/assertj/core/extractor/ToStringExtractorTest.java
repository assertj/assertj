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
package org.assertj.core.extractor;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.Test;


public class ToStringExtractorTest {

  private ToStringExtractor toStringExtractor = new ToStringExtractor();
  
  @Test
  public void should_extract_toString() {
    Employee someEmployee = new Employee(1, new Name("John Doe"), 28);
    
    String result = toStringExtractor.extract(someEmployee);
    
    assertThat(result).isEqualTo("Employee[id=1, name=Name[first='John Doe', last='null'], age=28]");
  }
}
