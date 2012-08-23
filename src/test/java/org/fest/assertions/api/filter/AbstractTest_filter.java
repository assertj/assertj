package org.fest.assertions.api.filter;

import static org.fest.util.Lists.newArrayList;

import java.util.List;

import org.junit.BeforeClass;

import org.fest.test.Name;
import org.fest.test.Player;

public class AbstractTest_filter {

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
    james = new Player(new Name("Lebron", "James"), "Miami Heat");
    james.setAssistsPerGame(6);
    james.setPointsPerGame(27);
    james.setReboundsPerGame(8);
    durant = new Player(new Name("Kevin", "Durant"), "OKC");
    durant.setAssistsPerGame(4);
    durant.setPointsPerGame(30);
    durant.setReboundsPerGame(5);
    noah = new Player(new Name("Joachim", "Noah"), "Chicago Bulls");
    noah.setAssistsPerGame(4);
    noah.setPointsPerGame(10);
    noah.setReboundsPerGame(11);
    players = newArrayList(rose, james, durant, noah);
  }

}