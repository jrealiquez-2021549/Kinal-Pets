
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.juliorealiquez.system.Principal;

public class MenuPrincipalController implements Initializable {
    @FXML private Button btnInventario2;
    @FXML private Button btnAgregar;
    @FXML private Button btnAgregar2;
    @FXML private Button btnInforme2;
    @FXML private Button btnContratar2;
    @FXML private Button btnComprar2;
    @FXML private Button btnApagar;
    @FXML private Button btnOpciones;
    @FXML private Button btnMusica;
    
    private boolean reproduciendo = true;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (MusicaFondo.getMediaPlayer() == null) {
            MusicaFondo.reproducirFondo();
        }
        
        btnInventario2.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregar2.setOnMouseEntered(e -> reproducirSonidoHover());
        btnInforme2.setOnMouseEntered(e -> reproducirSonidoHover());
        btnContratar2.setOnMouseEntered(e -> reproducirSonidoHover());
        btnComprar2.setOnMouseEntered(e -> reproducirSonidoHover());
        btnApagar.setOnMouseEntered(e -> reproducirSonidoHover());
        btnOpciones.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
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
    
    private void reproducirSonidoSalir() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/salir.mp3").toExternalForm();
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
    private void botonInventario2 (ActionEvent event) {
        if (event.getSource() == btnInventario2) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarVacunaView.fxml"));
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
    private void botonAgregar2 (ActionEvent event) {
        if (event.getSource() == btnAgregar2) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarClienteView.fxml"));
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
    private void botonContratar2 (ActionEvent event) {
        if (event.getSource() == btnContratar2) {
            try {
                reproducirSonidoClick();
                
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarVeterinarioView.fxml"));
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
    private void botonInforme2 (ActionEvent event) {
        if (event.getSource() == btnInforme2) {
            try {
                reproducirSonidoClick();
                
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/InformeView2.fxml"));
                root.setOpacity(0);
                Scene nuevaEscena = new Scene(root);
                
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(800), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                
                Principal.getPrimaryStage().setScene(nuevaEscena);
            } catch (IOException e) {
                System.out.println("Cambio de vista erroneo");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void botonComprar2 (ActionEvent event) {
        if (event.getSource() == btnComprar2) {
            try {
                reproducirSonidoClick();
                
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarCompraView.fxml"));
                root.setOpacity(0);
                Scene nuevaEscena = new Scene(root);
                
                javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(javafx.util.Duration.millis(800), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                
                Principal.getPrimaryStage().setScene(nuevaEscena);
            } catch (IOException e) {
                System.out.println("Cambio de vista erroneo");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void botonOpciones(ActionEvent event) {
        if (event.getSource() == btnOpciones) {
            reproducirSonidoClick();
            try {
                // Cargar la vista FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/juliorealiquez/view/OpcionesView.fxml"));
                Parent root = loader.load();

                // Establecer la escena
                Scene scene = new Scene(root);
                Stage stage = new Stage();

                // Ícono opcional
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/juliorealiquez/image/Kinal pets_logo_opciones.png")));

                // Establecer la ventana como MODAL
                stage.initModality(Modality.APPLICATION_MODAL);

                // Establecer la ventana principal como propietaria (opcional pero recomendable)
                stage.initOwner(btnOpciones.getScene().getWindow());

                stage.setScene(scene);
                stage.setTitle("Opciones");
                stage.setResizable(false);
                stage.centerOnScreen();

                // Agregar la animación Fade-In
                root.setOpacity(0);  // Inicialmente invisible
                FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.millis(800), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();

                // Mostrar la ventana y bloquear hasta que se cierre
                stage.showAndWait();
            } catch (IOException e) {
                System.out.println("No se pudo abrir la ventana de Opciones");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void botonApagar(ActionEvent event) {
        if (event.getSource() == btnApagar) {
            reproducirSonidoClick();
            reproducirSonidoSalir();

            // Hacer un fade out antes de cerrar
            Stage stage = (Stage) btnApagar.getScene().getWindow();
            Scene escena = stage.getScene();
        
            javafx.animation.FadeTransition fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.millis(1000), escena.getRoot());
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeOut.setOnFinished(e -> {
                stage.close(); // Cierra la ventana
                System.exit(0); // Cierra completamente la app (como un videojuego)
            });

            fadeOut.play(); // Ejecuta la animación
        }
    }
    
}