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
package org.assertj.core.test;

import static java.lang.String.format;

/**
 * @author Joel Costigliola
 */
public class Player {

  private Name name;
  public Name nickname;
  private int pointsPerGame;
  private int assistsPerGame;
  private int reboundsPerGame;
  private String team;
  // used to test private field access
  @SuppressWarnings("unused")
  private int highestScore;

  public Player() {}

  public Player(Name name, String team) {
    setName(name);
    setTeam(team);
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public int getPointsPerGame() {
    return pointsPerGame;
  }

  public void setPointsPerGame(int pointsPerGame) {
    this.pointsPerGame = pointsPerGame;
  }

  public int getAssistsPerGame() {
    return assistsPerGame;
  }

  public void setAssistsPerGame(int assistsPerGame) {
    this.assistsPerGame = assistsPerGame;
  }

  public int getReboundsPerGame() {
    return reboundsPerGame;
  }

  public void setReboundsPerGame(int reboundsPerGame) {
    this.reboundsPerGame = reboundsPerGame;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public void setHighestScore(int highestScore) {
    this.highestScore = highestScore;
  }

  @Override
  public String toString() {
    return format("%s[%s %s, team=%s]", getClass().getSimpleName(), name.getFirst(), name.getLast(), team);
  }
}
