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
package org.assertj.core.error;

import org.assertj.core.util.VisibleForTesting;

import java.io.File;
import java.nio.file.Path;

/**
 * Creates an error message indicating that an assertion that verifies that a
 * {@link File} or {@link Path} exists failed.
 * 
 */
public class ShouldExistNoFollow
    extends BasicErrorMessageFactory
{
    @VisibleForTesting
    public static final String PATH_SHOULD_EXIST_NOFOLLOW
        = "Expecting path:%n  <%s>%nto exist (symbolic links not followed)";

    public static ErrorMessageFactory shouldExistNoFollow(final Path actual)
    {
        return new ShouldExistNoFollow(actual);
    }

    private ShouldExistNoFollow(final Path actual)
    {
        super(PATH_SHOULD_EXIST_NOFOLLOW, actual);
    }
}
