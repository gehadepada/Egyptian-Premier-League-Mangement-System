package Utils;

public class Consts {

    final static public String url = "jdbc:sqlite:DB.sqlite";

    final static public String[] positions = new String[]{
        PlayerType.Attacker.toString(),
        PlayerType.Defender.toString(),
        PlayerType.GoalKeeper.toString(),
        PlayerType.Midfielder.toString()};

    public static final String[] Rounds;

    static {
        Rounds = new String[34];
        for (int i = 0; i < 34; i++) {
            int no = i + 1;
            Rounds[i] = "" + no;
        }
    }

    final static public String POINTS = "points";
    final static public String SCORES = "scores";
    
    final static public String NO_TEAM = "No Team";

    final static public String OWN_GOAL = "Own Goal";

    final static public String DES_G = "The goalkeeper is the last line of defense in a soccer team, responsible for preventing the opposing team from scoring by protecting the goal. This player is unique in that they are the only one allowed to use their hands and arms, but only within the penalty area. The goalkeeper's duties include catching, deflecting, and saving shots on goal, as well as directing the defense. Goalkeepers must possess excellent reflexes, agility, and decision-making skills. Additionally, they play a vital role in starting offensive plays by distributing the ball accurately to their teammates.";
    final static public String DES_A ="Attackers, also known as forwards or strikers, are primarily responsible for scoring goals and creating offensive plays. They need to possess excellent finishing skills, speed, and the ability to make quick decisions. Attackers often operate in advanced positions, looking to exploit defensive weaknesses and convert chances into goals. Their role requires a combination of technical skill, tactical awareness, and physical attributes to outmaneuver defenders and goalkeeper.";
    final static public String DES_D ="Defenders are responsible for stopping the opposing team’s attackers from scoring and for clearing the ball from the defensive area. They play a crucial role in maintaining the team's defensive structure and protecting the goal. Defenders need to be strong, quick, and capable of reading the game to intercept passes and block shots. They often engage in physical battles with opposing players and are instrumental during set pieces, both defensively and offensively.";
    final static public String DES_M ="Midfielders are the link between the defense and the attack, playing a versatile role that involves both defending and creating scoring opportunities. They need to be highly skilled in passing, dribbling, and tactical awareness. Midfielders are often tasked with controlling the tempo of the game, distributing the ball to teammates, and supporting both defensive and offensive plays. Their position on the field allows them to influence almost every aspect of the game.";
}
