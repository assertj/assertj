/*
 * Created on Sep 9, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.formatting;

import java.io.File;

/**
 * Returns the {@code String} representation of a <code>{@link File}</code>.
 * @author Alex Ruiz
 */
class FileToStringRule extends TypeBasedToStringRule<File> {

  @Override String doGetToString(File f) {
    return f.getAbsolutePath();
  }

  @Override Class<File> supportedType() {
    return File.class;
  }
}
