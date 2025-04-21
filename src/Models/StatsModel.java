package Models;

public class StatsModel {
    
    private String name;
    private int value;
    private int team;

    public StatsModel(String name, int value, int team) {
        this.name = name;
        this.value = value;
        this.team = team;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
    
    
    
    
    
}
