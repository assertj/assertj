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
package org.assertj.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


/**
 * Utility methods for loading test resources.
 * @author Simon Schrottner
 */
public final class ResourceUtil {

    private ResourceUtil() {}

    public static Path getResource(String path) {
        URL resource = ResourceUtil.class.getClassLoader().getResource(path);
        if(Objects.nonNull(resource)) {
            return Paths.get(resource.getPath());
        } else {
            return new File(path).toPath();
        }
    }
    public static FileReader getResourceAsReader(String path) throws FileNotFoundException {
        return new FileReader(getResource(path).toAbsolutePath().toString());
    }
}
