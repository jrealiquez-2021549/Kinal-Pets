
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.system;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.juliorealiquez.controller.MusicaFondo;


public class Principal extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            // Configurar el archivo FXML
            Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/InicioSesionView.fxml"));
            stage.getIcons().add(new Image("/org/juliorealiquez/image/kinal pets_logo2.png"));
            
            // Configfurar la escena
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Kinal Pets - 2021549");
            stage.setResizable(false);
            root.setOpacity(0);
            primaryStage.show();
            
            MusicaFondo.reproducirInicio();
            
            javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(1000), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            
        } catch (IOException e) {
            System.out.println("Error al cargar la vista");
            e.printStackTrace();
        }
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
