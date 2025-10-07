
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import org.juliorealiquez.db.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import org.juliorealiquez.system.Principal;

public class RegistrarController implements Initializable {
    @FXML private Button btnVolver;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private PasswordField txtConfirmarContrasenia;
    @FXML private PasswordField txtLlaveAcceso;
    
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
    
    private void limitarTextField(TextField field, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.length() <= maxLength ? change : null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(txtUsuario, 30);
        limitarTextField(txtContrasenia, 50);
        limitarTextField(txtConfirmarContrasenia, 50);
    }
    
    private Button botonActivo = null;
    
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
    
    private void reproducirSonidoAlerta() {
        try {
            String rutaSonido = getClass().getResource("/org/juliorealiquez/resource/alerta.mp3").toExternalForm();
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
    
    private void transicionTipoTuberia(Parent nuevaVista) {
        Scene escenaActual = Principal.getPrimaryStage().getScene();
        Parent vistaActual = escenaActual.getRoot();

        StackPane contenedor = new StackPane();
        contenedor.getChildren().addAll(nuevaVista, vistaActual);

        // Posiciona la nueva vista *debajo* para que suba (ahora al revés: arriba para que baje)
        nuevaVista.setTranslateY(escenaActual.getHeight());

        Scene nuevaEscena = new Scene(contenedor, escenaActual.getWidth(), escenaActual.getHeight());
        Principal.getPrimaryStage().setScene(nuevaEscena);

        // Vista actual sube hacia arriba (sale hacia arriba)
        TranslateTransition salida = new TranslateTransition(Duration.millis(800), vistaActual);
        salida.setToY(-escenaActual.getHeight());

        // Nueva vista baja hacia el centro (entra desde abajo)
        TranslateTransition entrada = new TranslateTransition(Duration.millis(800), nuevaVista);
        entrada.setToY(0);

        ParallelTransition transicion = new ParallelTransition(salida, entrada);

        transicion.setOnFinished(e -> {
            StackPane nuevoContenedor = new StackPane(nuevaVista);
            Scene escenaFinal = new Scene(nuevoContenedor, escenaActual.getWidth(), escenaActual.getHeight());
            Principal.getPrimaryStage().setScene(escenaFinal);
        });
        
        transicion.play();
    }

    private String nivelAccesoSeleccionado = null;
    private final String LLAVE_MAESTRA = "2021549"; // Cambia por tu llave real
    
    @FXML
    private void manejarBoton(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();

        // 1. Determinar el nivel de acceso según el icono del botón
        if (botonPresionado.getGraphic() != null) {
            ImageView icono = (ImageView) botonPresionado.getGraphic();
            String ruta = icono.getImage().getUrl();

            if (ruta.contains("Empleado")) {
                nivelAccesoSeleccionado = "Empleado";
            } else if (ruta.contains("Administrador")) {
                nivelAccesoSeleccionado = "Administrador";
            } else if (ruta.contains("Propietario")) {
                nivelAccesoSeleccionado = "Propietario";
            }
        }

        // 2. Actualizar el estilo del botón activo
        if (botonActivo != null) {
            botonActivo.getStyleClass().remove("activo");
        }
        botonPresionado.getStyleClass().add("activo");
        botonActivo = botonPresionado;
    }

    @FXML
    private void botonRegistrar(ActionEvent event) {
        if (!validarCampos()) return;
        if (!validarLlave()) return;
        if (nivelAccesoSeleccionado == null) {
            mostrarAlerta("Por favor selecciona un nivel de acceso.");
            return;
        }

        // Insertar usuario en la base de datos
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Usuarios (nombreUsuario, contraseñaUsuario, nivelAcceso) VALUES (?, ?, ?)");
            stmt.setString(1, txtUsuario.getText());
            stmt.setString(2, txtContrasenia.getText());
            stmt.setString(3, nivelAccesoSeleccionado);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                mostrarAlerta("Usuario agregado exitosamente.");
                limpiarCampos();
            } else {
                reproducirSonidoError();
                mostrarAlerta("Error al registrar el usuario.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al conectar con la base de datos.");
        }
    }
    
    @FXML
    private void botonVolver (ActionEvent event) {
        if (event.getSource() == btnVolver) {
            try {
                MusicaFondo.reproducirInicio();
                reproducirSonidoClick();
                
                Parent nuevaVista = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/InicioSesionView.fxml"));
                transicionTipoTuberia(nuevaVista);
                
            } catch (IOException e) {
                System.out.println("Cambio de vista erroneo");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void validarLlave(ActionEvent event) {
        if (txtLlaveAcceso.getText().equals(LLAVE_MAESTRA)) {
            mostrarAlerta("Llave de acceso válida.");
        } else {
            reproducirSonidoError();
            mostrarAlerta("Llave incorrecta. No puedes registrar el usuario.");
        }
    }

    private boolean validarLlave() {
        return txtLlaveAcceso.getText().equals(LLAVE_MAESTRA);
    }

    private boolean validarCampos() {
        String usuario = txtUsuario.getText();
        String contrasenia = txtContrasenia.getText();
        String confirmar = txtConfirmarContrasenia.getText();

        if (usuario.isEmpty() || contrasenia.isEmpty() || confirmar.isEmpty()) {
            reproducirSonidoError();
            mostrarAlerta("Completa todos los campos.");
            return false;
        }

        if (!contrasenia.equals(confirmar)) {
            reproducirSonidoError();
            mostrarAlerta("Las contraseñas no coinciden.");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String mensaje) {
        Stage alertaStage = new Stage();
        alertaStage.initModality(Modality.APPLICATION_MODAL);
        alertaStage.setTitle("Información");
        alertaStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/juliorealiquez/image/logo_error.png")));

        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(20));

        Label mensajeLbl = new Label(mensaje);
        mensajeLbl.setStyle("-fx-font-size: 14px;");

        Button aceptar = new Button("Aceptar");
        aceptar.setOnAction(e -> alertaStage.close());

        contenedor.getChildren().addAll(mensajeLbl, aceptar);

        Scene escena = new Scene(contenedor);
        alertaStage.setScene(escena);
        alertaStage.setResizable(false);
        alertaStage.showAndWait();
    }

    private void limpiarCampos() {
        txtUsuario.clear();
        txtContrasenia.clear();
        txtConfirmarContrasenia.clear();
        txtLlaveAcceso.clear();
        nivelAccesoSeleccionado = null;
    }
}