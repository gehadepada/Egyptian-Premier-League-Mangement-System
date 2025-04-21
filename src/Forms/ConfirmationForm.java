/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author abdo
 */
public class ConfirmationForm {
    
    public static boolean showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(content);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("Cancel");
        
        alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeNo);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.isPresent()){
            
            if (result.get() == buttonTypeYes){
                return true;
            }
            else{
                return false;
            }
            
        }
        else{
            return false;
        }
        
        
    }
    
}
