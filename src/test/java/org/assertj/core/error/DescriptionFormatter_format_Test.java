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
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * Tests for <code>{@link DescriptionFormatter#format(Description)}</code>.
 * 
 * @author Alex Ruiz
 * @author Dan Corder
 */
@RunWith(DataProviderRunner.class)
public class DescriptionFormatter_format_Test {

  private static DescriptionFormatter formatter;

  @BeforeClass
  public static void setUpOnce() {
    formatter = DescriptionFormatter.instance();
  }

  @Test
  public void should_format_description_if_value_is_not_empty_or_null() {
    assertThat(formatter.format(new TestDescription("Leia"))).isEqualTo("[Leia] ");
  }

  @Test
  @UseDataProvider("descriptionGeneratorDataProvider")
  public void should_return_empty_String(TestDescription testDescription) {
    assertThat(formatter.format(testDescription)).isEmpty();
  }

  @DataProvider
  public static Object[][] descriptionGeneratorDataProvider() {
    return new Object[][] {
        { null },
        { new TestDescription(null) },
        { new TestDescription("") }
    };
  }
}
