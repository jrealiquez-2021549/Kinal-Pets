
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.juliorealiquez.db.Conexion;
import org.juliorealiquez.models.Vacunaciones;
import org.juliorealiquez.system.Principal;

public class AgregarVacunacionController implements Initializable{
    @FXML private Button btnAgregarVacuna;
    @FXML private Button btnAgregarMedicina;
    @FXML private Button btnAgregarTratamiento;
    @FXML private Button btnAgregarReceta;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Vacunaciones> TablaVacunacion;
    private ObservableList<Vacunaciones> listarVacunaciones;
    @FXML
    private TableColumn<Vacunaciones, Integer> codigoVacunacion;
    @FXML
    private TableColumn<Vacunaciones, java.sql.Date> FechaAplicacion;
    @FXML
    private TableColumn<Vacunaciones, String> Observaciones;
    @FXML
    private TableColumn<Vacunaciones, Integer> CodigoMascota;
    @FXML
    private TableColumn<Vacunaciones, Integer> CodigoVacuna;
    @FXML
    private TableColumn<Vacunaciones, Integer> CodigoVeterinario;
    
    // Objetos de texto
    @FXML
    private TextField codigoVacunacionField;
    @FXML
    private DatePicker fechaAplicacionPicker;
    @FXML
    private TextField observacionesField;
    @FXML
    private TextField codigoMascotaField;
    @FXML
    private TextField codigoVacunaField;
    @FXML
    private TextField codigoVeterinarioField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAgregarVacuna.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarMedicina.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarTratamiento.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarReceta.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(observacionesField, 200);
        
        cargarDatos();
        MostrarLosDatos();
    }
    
    private void limitarTextField(TextField field, int maxLength) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            return newText.length() <= maxLength ? change : null;
        };
        field.setTextFormatter(new TextFormatter<>(filter));
    }
    
    private boolean reproduciendo = true;
    
    private void mostrarAlerta(String titulo, String mensaje) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    private void mostrarVentanaError(String mensajeError) {
        Stage alertaStage = new Stage();
        alertaStage.initModality(Modality.APPLICATION_MODAL);
        alertaStage.setTitle("Error");
        alertaStage.getIcons().add(new Image(getClass().getResourceAsStream("/org/juliorealiquez/image/logo_error.png")));

        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(20));

        ImageView icono = new ImageView(new Image(getClass().getResourceAsStream("/org/juliorealiquez/image/logo_error.png")));
        icono.setFitWidth(50);
        icono.setFitHeight(50);

        Label mensaje = new Label(mensajeError);
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
        alertaStage.showAndWait();
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
    
    public void cargarDatos() {
        TablaVacunacion.setItems(getVacunaciones());
        
        codigoVacunacion.setCellValueFactory(new PropertyValueFactory<>("codigoVacunacion"));
        FechaAplicacion.setCellValueFactory(new PropertyValueFactory<>("fechaAplicacion"));
        Observaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        CodigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        CodigoVacuna.setCellValueFactory(new PropertyValueFactory<>("codigoVacuna"));
        CodigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        
        FechaAplicacion.setCellFactory(column -> new TableCell<Vacunaciones, java.sql.Date>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDate().format(formatter));
                }
            }
        });
    }
    
    public void MostrarLosDatos() {
        listarVacunaciones = FXCollections.observableArrayList();
        
        TablaVacunacion.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoVacunacionField.setText(String.valueOf(newSelection.getCodigoVacunacion()));
                fechaAplicacionPicker.setValue(newSelection.getFechaAplicacion().toLocalDate());
                observacionesField.setText(newSelection.getObservaciones());
                codigoMascotaField.setText(String.valueOf(newSelection.getCodigoMascota()));
                codigoVacunaField.setText(String.valueOf(newSelection.getCodigoVacuna()));
                codigoVeterinarioField.setText(String.valueOf(newSelection.getCodigoVeterinario()));
            } else {
                codigoVacunacionField.clear();
                fechaAplicacionPicker.setValue(null);
                observacionesField.clear();
                codigoMascotaField.clear();
                codigoVacunaField.clear();
                codigoVeterinarioField.clear();
            }
        });
    }
    
    public ObservableList<Vacunaciones> buscarVacunacionPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Vacunaciones> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarVacunacion(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Vacunaciones(
                        rs.getInt("codigoVacunacion"),
                        rs.getDate("fechaAplicacion"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVacuna"),
                        rs.getInt("codigoVeterinario")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar vacunacion: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Vacunaciones> getVacunaciones() {
        ObservableList<Vacunaciones> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarVacunaciones() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Vacunaciones(
                        rs.getInt("codigoVacunacion"),
                        rs.getDate("fechaAplicacion"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVacuna"),
                        rs.getInt("codigoVeterinario")
                    )
                );
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    @FXML
    void handleButtonPressLimpiar(ActionEvent event) {
        reproducirSonidoClick();
        
        codigoVacunacionField.clear();
        fechaAplicacionPicker.setValue(null);
        observacionesField.clear();
        codigoMascotaField.clear();
        codigoVacunaField.clear();
        codigoVeterinarioField.clear();
        TablaVacunacion.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoVacunacionField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de vacunacion para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Vacunaciones> resultado = buscarVacunacionPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna vacunacion con ese código.");
            } else {
                TablaVacunacion.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarVacunacion(ActionEvent event) {
        reproducirSonidoClick();
        
        LocalDate fecha = fechaAplicacionPicker.getValue();
        String observaciones = observacionesField.getText().trim();
        String codMascota = codigoMascotaField.getText().trim();
        String codVacuna = codigoVacunaField.getText().trim();
        String codVeterinario = codigoVeterinarioField.getText().trim();
        
        if (fecha == null || observaciones.isEmpty() || codMascota.isEmpty() || codVacuna.isEmpty() || codVeterinario.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        int mascota;
        int vacuna;
        int veterinario;
        try {
            mascota = Integer.parseInt(codMascota);
            vacuna = Integer.parseInt(codVacuna);
            veterinario = Integer.parseInt(codVeterinario);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los códigos deben ser números enteros.");
            return;
        }
        
        // Verificación de llaves foráneas
        if (!existeMascota(mascota)) {
            mostrarVentanaError("El código de mascota ingresado no existe.");
            return;
        }
        
        if (!existeVacuna(vacuna)) {
            mostrarVentanaError("El código de vacuna ingresado no existe.");
            return;
        }

        if (!existeVeterinario(veterinario)) {
            mostrarVentanaError("El código de veterinario ingresado no existe.");
            return;
        }
        
        Vacunaciones vacunacion = new Vacunaciones();
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaAplicacionPicker.getValue());
        vacunacion.setFechaAplicacion(fechaSQL);
        vacunacion.setObservaciones(observaciones);
        vacunacion.setCodigoMascota(mascota);
        vacunacion.setCodigoVacuna(vacuna);
        vacunacion.setCodigoVeterinario(veterinario);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarVacunacion(?, ?, ?, ?, ?) }");
            stmt.setDate(1, vacunacion.getFechaAplicacion());
            stmt.setString(2, vacunacion.getObservaciones());
            stmt.setInt(3, vacunacion.getCodigoMascota());
            stmt.setInt(4, vacunacion.getCodigoVacuna());
            stmt.setInt(5, vacunacion.getCodigoVeterinario());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar vacunacion: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarVacunacion(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoVacunacionField.getText().trim());
            LocalDate fecha = fechaAplicacionPicker.getValue();
            String observaciones = observacionesField.getText().trim();
            String codMascota = codigoMascotaField.getText().trim();
            String codVacuna = codigoVacunaField.getText().trim();
            String codVeterinario = codigoVeterinarioField.getText().trim();
            
            if (fecha == null || observaciones.isEmpty() || codMascota.isEmpty() || codVacuna.isEmpty() || codVeterinario.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }

            int mascota;
            int vacuna;
            int veterinario;
            try {
                mascota = Integer.parseInt(codMascota);
                vacuna = Integer.parseInt(codVacuna);
                veterinario = Integer.parseInt(codVeterinario);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los códigos deben ser números enteros.");
                return;
            }
            
            // Verificación de llaves foráneas
            if (!existeMascota(mascota)) {
                mostrarVentanaError("El código de mascota ingresado no existe.");
                return;
            }
            
            if (!existeVacuna(vacuna)) {
                mostrarVentanaError("El código de vacuna ingresado no existe.");
                return;
            }

            if (!existeVeterinario(veterinario)) {
                mostrarVentanaError("El código de veterinario ingresado no existe.");
                return;
            }
            
            Vacunaciones vacunacion = new Vacunaciones();
            vacunacion.setCodigoVacunacion(codigo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaAplicacionPicker.getValue());
            vacunacion.setFechaAplicacion(fechaSQL);
            vacunacion.setObservaciones(observaciones);
            vacunacion.setCodigoMascota(mascota);
            vacunacion.setCodigoVacuna(vacuna);
            vacunacion.setCodigoVeterinario(veterinario);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarVacunacion(?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, vacunacion.getCodigoVacunacion());
            stmt.setDate(2, vacunacion.getFechaAplicacion());
            stmt.setString(3, vacunacion.getObservaciones());
            stmt.setInt(4, vacunacion.getCodigoMascota());
            stmt.setInt(5, vacunacion.getCodigoVacuna());
            stmt.setInt(6, vacunacion.getCodigoVeterinario());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Vacunacion actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la vacunacion debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar vacunacion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarVacunacion(ActionEvent event) {
        reproducirSonidoClick();
        Vacunaciones vacunacionSeleccionado = TablaVacunacion.getSelectionModel().getSelectedItem();

        if (vacunacionSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarVacunacion(?) }");
                stmt.setInt(1, vacunacionSeleccionado.getCodigoVacunacion());
                stmt.execute();

                System.out.println("Vacunacion eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta vacunacion está siendo utilizada en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar vacunacion: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la vacunacion.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una vacunacion de la tabla para eliminar.");
        }
    }
    
    @FXML
    private void botonAgregarVacuna (ActionEvent event) {
        if (event.getSource() == btnAgregarVacuna) {
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
    private void botonAgregarMedicina (ActionEvent event) {
        if (event.getSource() == btnAgregarMedicina) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarMedicinaView.fxml"));
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
    private void botonAgregarTratamiento (ActionEvent event) {
        if (event.getSource() == btnAgregarTratamiento) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarTratamientoView.fxml"));
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
    private void botonAgregarReceta (ActionEvent event) {
        if (event.getSource() == btnAgregarReceta) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarRecetaView.fxml"));
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
    
    private boolean existeMascota(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarMascota(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean existeVacuna(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarVacuna(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean existeVeterinario(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarVeterinario(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
