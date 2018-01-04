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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.test;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;


public class WithPlayerData {

  @Rule
  public ExpectedException thrown = none();

  protected static Player jordan;
  protected static Player magic;
  protected static Player kobe;
  protected static Player duncan;
  protected static List<Player> players;

  @BeforeClass
  public static void setUpOnce() {
    jordan = new Player(new Name("Michael", "Jordan"), "Chicago Bulls");
    jordan.setAssistsPerGame(8);
    jordan.setPointsPerGame(30);
    jordan.setReboundsPerGame(6);
    jordan.setHighestScore(69);
    magic = new Player(new Name("Magic", "Johnson"), "Los Angeles Lakers");
    magic.setAssistsPerGame(11);
    magic.setPointsPerGame(19);
    magic.setReboundsPerGame(7);
    magic.setHighestScore(56);
    kobe = new Player(new Name("Kobe", "Bryant"), "Los Angeles Lakers");
    kobe.setAssistsPerGame(5);
    kobe.setPointsPerGame(25);
    kobe.setReboundsPerGame(5);
    kobe.setHighestScore(81);
    duncan = new Player(new Name("Tim", "Duncan"), "San Antonio Spurs");
    duncan.setAssistsPerGame(3);
    duncan.setPointsPerGame(19);
    duncan.setReboundsPerGame(11);
    duncan.setHighestScore(53);
    players = newArrayList(jordan, magic, kobe, duncan);
  }

}