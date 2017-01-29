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
package org.assertj.core.api.filter;

import org.assertj.core.api.Condition;
import org.assertj.core.test.Player;


/**
 * 
 * A {@code Condition} checking if a {@link Player} is a potential MVP.
 * 
 * @author Joel Costigliola
 */
public class PotentialMvpCondition extends Condition<Player> {

  public PotentialMvpCondition() {
    super("is a potential MVP");
  }

  @Override
  public boolean matches(Player player) {
    return player.getPointsPerGame() > 20 && (player.getAssistsPerGame() >= 8 || player.getReboundsPerGame() >= 8);
  }

}
