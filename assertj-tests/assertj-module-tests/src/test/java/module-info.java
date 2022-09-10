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
 * Copyright 2012-2022 the original author or authors.
 */
module org.assertj.moduletest {
    // AssertJ Core's package API
    requires org.assertj.core;
    requires org.junit.jupiter.api;
    requires org.junit.platform.launcher;

    requires static org.hamcrest;

    opens org.assertj.moduletest.api to org.junit.platform.commons;
    //provides org.assertj.core.configuration.Configuration with org.assertj.core.test.ConfigurationForTests;
}
  