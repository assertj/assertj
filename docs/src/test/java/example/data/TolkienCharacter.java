package example.data;

public class TolkienCharacter {

  public final String name;
  public final Integer age;
  public final Race race;

  public TolkienCharacter(String name, Integer age, Race race) {

    this.name = name;
    this.age = age;
    this.race = race;
  }

  public String getName() {

    return name;
  }

  public Integer getAge() {

    return age;
  }

  public Race getRace() {

    return race;
  }

  @Override
  public String toString() {
    return String.format("TolkienCharacter [name=%s, age=%s, race=%s]", name, age, race);
  }

}
