
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.juliorealiquez.db.ValidadorUsuario;
import org.juliorealiquez.system.Principal;

public class InicioSesionController implements Initializable {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    
    @FXML private Button btnInicioSesion;
    @FXML private Button btnRegistrarce;
    @FXML private Button btnApagar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnInicioSesion.setOnMouseEntered(e -> reproducirSonidoHover());
        btnRegistrarce.setOnMouseEntered(e -> reproducirSonidoHover());
        btnApagar.setOnMouseEntered(e -> reproducirSonidoHover());
    }
    
    private void transicionInicioNivel(Parent nuevaVista) {
        Scene escenaActual = Principal.getPrimaryStage().getScene();
        Parent vistaActual = escenaActual.getRoot();

        // Fondo negro sobre la escena actual
        Rectangle blackout = new Rectangle(escenaActual.getWidth(), escenaActual.getHeight(), Color.BLACK);
        blackout.setOpacity(0);

        StackPane contenedor = new StackPane(vistaActual, blackout);
        Scene nuevaEscena = new Scene(contenedor, escenaActual.getWidth(), escenaActual.getHeight());
        Principal.getPrimaryStage().setScene(nuevaEscena);

        // Oscurecimiento tipo fade-out
        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), blackout);
        fadeOut.setFromValue(0);
        fadeOut.setToValue(1);

        fadeOut.setOnFinished(e -> {
            // Configura la nueva vista para entrada
            nuevaVista.setOpacity(0);
            nuevaVista.setScaleX(1.2);
            nuevaVista.setScaleY(1.2);

            // Nuevo contenedor con la nueva vista (después del blackout)
            StackPane nuevaCapa = new StackPane(nuevaVista);
            Scene escenaFinal = new Scene(nuevaCapa, escenaActual.getWidth(), escenaActual.getHeight());
            Principal.getPrimaryStage().setScene(escenaFinal);

            // Efectos de entrada del nivel
            FadeTransition fadeIn = new FadeTransition(Duration.millis(600), nuevaVista);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), nuevaVista);
            scaleIn.setFromX(1.2);
            scaleIn.setFromY(1.2);
            scaleIn.setToX(1);
            scaleIn.setToY(1);

            ParallelTransition animacionEntrada = new ParallelTransition(fadeIn, scaleIn);
            animacionEntrada.play();
        });

        fadeOut.play();
    }
    
    private void transicionTipoTuberia(Parent nuevaVista) {
        Scene escenaActual = Principal.getPrimaryStage().getScene();
        Parent vistaActual = escenaActual.getRoot();

        StackPane contenedor = new StackPane();
        contenedor.getChildren().addAll(nuevaVista, vistaActual);

        nuevaVista.setTranslateY(-escenaActual.getHeight());

        Scene nuevaEscena = new Scene(contenedor, escenaActual.getWidth(), escenaActual.getHeight());
        Principal.getPrimaryStage().setScene(nuevaEscena);

        // Animación para bajar la vista actual (como bajando por tubería)
        TranslateTransition salida = new TranslateTransition(Duration.millis(800), vistaActual);
        salida.setToY(escenaActual.getHeight());

        // Animación para subir la nueva vista (como saliendo de tubería)
        TranslateTransition entrada = new TranslateTransition(Duration.millis(800), nuevaVista);
        entrada.setToY(0);

        ParallelTransition transicion = new ParallelTransition(salida, entrada);
        transicion.setOnFinished(e -> {
            
        });
        transicion.play();
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
    
    private void reproducirSonidoIniciar() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/iniciar (mp3cut.net).mp3").toExternalForm();
            AudioClip sonido = new AudioClip(rutaSonido);
            sonido.setVolume(SonidoController.getVolumenEfectos());
            sonido.play();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido del botón.");
            e.printStackTrace();
        }
    }
    
    private void reproducirSonidoError() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/error.mp3").toExternalForm();
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
    
    private void reproducirSonidoAlerta() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/alerta.mp3").toExternalForm();
            AudioClip sonido = new AudioClip(rutaSonido);
            sonido.play();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido de alerta.");
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
    private void botonIniciarSesion(ActionEvent event) {
        if (event.getSource() == btnInicioSesion) {
            String usuario = txtUsuario.getText();
            String contrasenia = txtContrasenia.getText();

            // Validar credenciales desde la base de datos
            String nivelAcceso = ValidadorUsuario.validarCredenciales(usuario, contrasenia);

            if (nivelAcceso != null) {
                reproducirSonidoClick();
                reproducirSonidoIniciar();

                try {
                    MusicaFondo.reproducirFondo();
                    String vista = "";

                    switch (nivelAcceso) {
                        case "Empleado":
                            vista = "/org/juliorealiquez/view/MenuPrincipalView.fxml";
                            break;
                        case "Administrador":
                            vista = "/org/juliorealiquez/view/MenuPrincipalView.fxml";
                            break;
                        case "Propietario":
                            vista = "/org/juliorealiquez/view/MenuPrincipalView.fxml";
                            break;
                        default:
                            vista = "/org/juliorealiquez/view/MenuPrincipalView.fxml";
                            break;
                    }

                    Parent nuevaVista = FXMLLoader.load(getClass().getResource(vista));
                    transicionInicioNivel(nuevaVista);

                } catch (IOException e) {
                    System.out.println("Error al cargar la vista del menú según nivel.");
                    e.printStackTrace();
                }

            } else {
                reproducirSonidoError();

                Stage alertaStage = new Stage();
                alertaStage.initModality(Modality.APPLICATION_MODAL);
                alertaStage.setTitle("Error de autenticación");
                alertaStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/juliorealiquez/image/logo_error.png")));

                VBox contenedor = new VBox(20);
                contenedor.setAlignment(Pos.CENTER);
                contenedor.setPadding(new Insets(20));

                ImageView icono = new ImageView(new Image(getClass().getResourceAsStream("/org/juliorealiquez/image/logo_error.png")));
                icono.setFitWidth(50);
                icono.setFitHeight(50);

                Label mensaje = new Label("Usuario o contraseña incorrectos.");
                mensaje.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                mensaje.setWrapText(true);
                mensaje.setAlignment(Pos.CENTER);

                HBox contenido = new HBox(10, icono, mensaje);
                contenido.setAlignment(Pos.CENTER);

                Button botonAceptar = new Button("Aceptar");
                botonAceptar.setOnAction(e -> alertaStage.close());

                contenedor.getChildren().addAll(contenido, botonAceptar);
                Scene escena = new Scene(contenedor);
                alertaStage.setScene(escena);
                alertaStage.setResizable(false);

                reproducirSonidoAlerta();
                alertaStage.showAndWait();
            }
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    @FXML
    private void botonRegistrarce (ActionEvent event) {
        if (event.getSource() == btnRegistrarce) {
            try {
                reproducirSonidoClick();
                
                Parent nuevaVista = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/RegistrarceView.fxml"));
                transicionTipoTuberia(nuevaVista);
                
            } catch (IOException e) {
                System.out.println("Cambio de vista erroneo");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void botonApagar(ActionEvent event) {
        if (event.getSource() == btnApagar) {
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
