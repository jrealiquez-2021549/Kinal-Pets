
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import org.juliorealiquez.system.Principal;

public class InformeController implements Initializable {
    @FXML private Button btnFactura;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnFactura.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
    }
    
    private boolean reproduciendo = true;
    
    private void reproducirSonidoClick() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/click.mp3").toExternalForm();
            AudioClip sonido = new AudioClip(rutaSonido);
            sonido.setVolume(SonidoController.getVolumenEfectos());
            sonido.play();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido del botón.");
            e.printStackTrace();
        }
    }
    
    private void reproducirSonidoVolver() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/volver.mp3").toExternalForm();
            AudioClip sonido = new AudioClip(rutaSonido);
            sonido.setVolume(SonidoController.getVolumenEfectos());
            sonido.play();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido del botón.");
            e.printStackTrace();
        }
    }
    
    private void reproducirSonidoHover() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/hover.mp3").toExternalForm();
            AudioClip sonido = new AudioClip(rutaSonido);
            sonido.setVolume(SonidoController.getVolumenEfectos());
            sonido.play();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido hover del botón.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void botonFactura (ActionEvent event) {
        if (event.getSource() == btnFactura) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/FacturaView.fxml"));
                root.setOpacity(0);
                Scene nuevaEscena = new Scene(root);
                Principal.getPrimaryStage().setScene(nuevaEscena);
                
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(800), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                
            } catch (IOException e) {
                System.out.println("Cambio de vista erroneo");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void botonMusica(ActionEvent event) {
        reproducirSonidoClick();
        
        MediaPlayer mediaPlayer = MusicaFondo.getMediaPlayer();

        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();

        if (status == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            reproduciendo = false;
        } else if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY || status == MediaPlayer.Status.STOPPED) {
            mediaPlayer.play();
            reproduciendo = true;
        }
        
        }
    }
    
    @FXML
    private void botonVolver (ActionEvent event) {
        if (event.getSource() == btnVolver) {
            try {
                reproducirSonidoVolver();
                
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/MenuPrincipalView.fxml"));
                root.setOpacity(0);
                Scene nuevaEscena = new Scene(root);
                Principal.getPrimaryStage().setScene(nuevaEscena);
                
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(800), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                
            } catch (IOException e) {
                System.out.println("Cambio de vista erroneo");
                e.printStackTrace();
            }
        }
    }
}
