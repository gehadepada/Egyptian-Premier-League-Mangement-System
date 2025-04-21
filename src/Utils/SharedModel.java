/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Models.MatchModel;
import Models.PlayerModel;
import Models.TeamModel;

/**
 *
 * @author abdo
 */
public class SharedModel {
    
    private static TeamModel selected_team ;
    private static PlayerModel selected_player;
    private static MatchModel selected_match;

    public static TeamModel getSelected_team() {
        return selected_team;
    }

    public static void setSelected_team(TeamModel selected_team) {
        SharedModel.selected_team = selected_team;
    }

    public static PlayerModel getSelected_player() {
        return selected_player;
    }

    public static void setSelected_player(PlayerModel selected_player) {
        SharedModel.selected_player = selected_player;
    }

    public static MatchModel getSelected_match() {
        return selected_match;
    }

    public static void setSelected_match(MatchModel selected_match) {
        SharedModel.selected_match = selected_match;
    }
    
    
    
    
}
