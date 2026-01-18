/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.scripts;

import static org.junit.platform.commons.support.ResourceSupport.findAllResourcesInPackage;

import java.nio.file.Path;
import java.util.List;

import org.junit.platform.commons.io.Resource;
import org.junit.platform.commons.io.ResourceFilter;

class ClasspathResources {

  static Path resourcePath(String resourceName) {
    ResourceFilter filter = ResourceFilter.of(resource -> resource.getName().equals(resourceName));
    List<Resource> resources = findAllResourcesInPackage("", filter);
    if (resources.size() != 1) throw new IllegalStateException("Unique resource not found: " + resources);
    return Path.of(resources.getFirst().getUri());
  }

}
