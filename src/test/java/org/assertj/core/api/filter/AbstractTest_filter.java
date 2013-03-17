package org.assertj.core.api.filter;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.test.Name;
import org.assertj.core.test.Player;
import org.junit.BeforeClass;


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