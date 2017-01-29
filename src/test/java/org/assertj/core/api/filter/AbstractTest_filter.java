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

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.test.Player;
import org.junit.BeforeClass;
import org.junit.Rule;


public class AbstractTest_filter {

  @Rule
  public ExpectedException thrown = none();

  protected static Player rose;
  protected static Player james;
  protected static Player durant;
  protected static Player noah;
  protected static List<Player> players;

  @BeforeClass
  public static void setUpOnce() {
    rose = new Player(new Name("Derrick", "Rose"), "Chicago Bulls");
    rose.setAssistsPerGame(8);
    rose.setPointsPerGame(25);
    rose.setReboundsPerGame(5);
    rose.setHighestScore(50);
    james = new Player(new Name("Lebron", "James"), "Miami Heat");
    james.setAssistsPerGame(6);
    james.setPointsPerGame(27);
    james.setReboundsPerGame(8);
    james.setHighestScore(50);
    durant = new Player(new Name("Kevin", "Durant"), "OKC");
    durant.setAssistsPerGame(4);
    durant.setPointsPerGame(30);
    durant.setReboundsPerGame(5);
    durant.setHighestScore(60);
    noah = new Player(new Name("Joachim", "Noah"), "Chicago Bulls");
    noah.setAssistsPerGame(4);
    noah.setPointsPerGame(10);
    noah.setReboundsPerGame(11);
    noah.setHighestScore(20);
    players = newArrayList(rose, james, durant, noah);
  }

}