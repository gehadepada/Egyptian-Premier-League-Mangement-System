/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author abdo
 */
public class AgesModel {
    
    String team_name;
    String team_image;
    double ages_avg;

    public AgesModel(String team_name, String team_image, double ages_avg) {
        this.team_name = team_name;
        this.team_image = team_image;
        this.ages_avg = ages_avg;
    }
    

    public String getTeam_image() {
        return team_image;
    }

    public void setTeam_image(String team_image) {
        this.team_image = team_image;
    }

    

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public double getAges_avg() {
        return ages_avg;
    }

    public void setAges_avg(double ages_avg) {
        this.ages_avg = ages_avg;
    }
    
    
    
}
