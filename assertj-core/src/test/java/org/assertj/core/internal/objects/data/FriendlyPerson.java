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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.objects.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendlyPerson extends Person {
  public List<FriendlyPerson> friends = new ArrayList<>();
  public Set<FriendlyPerson> otherFriends = new HashSet<>();

  public FriendlyPerson() {
    super();
  }

  public FriendlyPerson(String name) {
    super(name);
  }

  public static FriendlyPerson friend(String name) {
    return new FriendlyPerson(name);
  }
}