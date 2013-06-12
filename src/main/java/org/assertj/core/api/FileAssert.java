/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
<<<<<<< HEAD
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
=======
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2011-2012 the original author or authors.
>>>>>>> refs/heads/github-71
 */
package org.assertj.core.api;

import java.io.File;
import java.nio.charset.Charset;

import org.assertj.core.internal.Files;
import org.assertj.core.util.FilesException;
import org.assertj.core.util.VisibleForTesting;


/**
 * Assertion methods for {@link File}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(File)}</code>.
 * </p>
 * 
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Olivier Michallat
 * @author Olivier Demeijer
 * @author Mikhail Mazursky
 */
public class FileAssert extends AbstractFileAssert<FileAssert> {

  protected FileAssert(File actual) {
    super(actual, FileAssert.class);
  }
}
