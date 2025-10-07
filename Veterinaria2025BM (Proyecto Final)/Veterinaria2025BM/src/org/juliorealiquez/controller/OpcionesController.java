
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class OpcionesController implements Initializable{

    @FXML private Button btnVolver;
    @FXML private Slider sliderVolumen;
    @FXML private Slider sliderEfectos;
    @FXML private Button btnRestablecer;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnRestablecer.setOnMouseEntered(e -> reproducirSonidoHover());

        MediaPlayer mediaPlayer = MusicaFondo.getMediaPlayer();
        if (mediaPlayer != null) {
            sliderVolumen.setValue(mediaPlayer.getVolume() * 100); // Asegúrate de que el slider de música esté sincronizado con el volumen real
            sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
                mediaPlayer.setVolume(newVal.doubleValue() / 100.0);
            });
        }

        // Volumen de efectos
        sliderEfectos.setValue(SonidoController.getVolumenEfectos() * 100); // Asegúrate de que el slider de efectos esté sincronizado con el volumen real
        sliderEfectos.valueProperty().addListener((obs, oldVal, newVal) -> {
            double nuevoVolumen = newVal.doubleValue() / 100.0;
            SonidoController.setVolumenEfectos(nuevoVolumen);
        });
    }
    
    private boolean reproduciendo = true;
    
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
    
    @FXML
    private void botonRestablecer(ActionEvent event) {
        reproducirSonidoClick();
        // Valores por defecto
        double volumenPorDefecto = 0.5;

        // Restablecer slider de música
        sliderVolumen.setValue(volumenPorDefecto * 100);
        MediaPlayer mediaPlayer = MusicaFondo.getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumenPorDefecto);
        }

        // Restablecer slider de efectos
        sliderEfectos.setValue(volumenPorDefecto * 100);
        SonidoController.setVolumenEfectos(volumenPorDefecto);
    }
    
    @FXML
    private void botonVolver(ActionEvent event) {
        if (event.getSource() == btnVolver) {
            try {
                reproducirSonidoClick();

                // Obtener el Stage actual desde el botón
                Stage stageActual = (Stage) btnVolver.getScene().getWindow();
                stageActual.close(); // Cierra esta ventana

            } catch (Exception e) {
                System.out.println("Error al cerrar la ventana de Opciones");
                e.printStackTrace();
            }
        }
    }
}