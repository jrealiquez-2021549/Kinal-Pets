
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
import org.juliorealiquez.models.Consultas;
import org.juliorealiquez.system.Principal;

public class AgregarConsultaController implements Initializable{
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarCliente;
    @FXML private Button btnAgregarMascota;
    @FXML private Button btnAgregarCita;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Consultas> TablaConsulta;
    private ObservableList<Consultas> listarConsultas;
    @FXML
    private TableColumn<Consultas, Integer> codigoConsulta;
    @FXML
    private TableColumn<Consultas, java.sql.Date> FechaConsulta;
    @FXML
    private TableColumn<Consultas, String> Motivo;
    @FXML
    private TableColumn<Consultas, String> Diagnostico;
    @FXML
    private TableColumn<Consultas, String> Observaciones;
    @FXML
    private TableColumn<Consultas, Integer> CodigoMascota;
    @FXML
    private TableColumn<Consultas, Integer> CodigoVeterinario;
    
    // Objetos de texto
    @FXML
    private TextField codigoConsultaField;
    @FXML
    private DatePicker fechaConsultaPicker;
    @FXML
    private TextField motivoField;
    @FXML
    private TextField diagnosticoField;
    @FXML
    private TextField observacionesField;
    @FXML
    private TextField codigoMascotaField;
    @FXML
    private TextField codigoVeterinarioField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAgregarCliente.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarMascota.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarCita.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(motivoField, 200);
        limitarTextField(diagnosticoField, 200);
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
        TablaConsulta.setItems(getConsultas());
        
        codigoConsulta.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        FechaConsulta.setCellValueFactory(new PropertyValueFactory<>("fechaConsulta"));
        Motivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        Diagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        Observaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        CodigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        CodigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        
            FechaConsulta.setCellFactory(column -> new TableCell<Consultas, java.sql.Date>() {
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
        listarConsultas = FXCollections.observableArrayList();
        
        TablaConsulta.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoConsultaField.setText(String.valueOf(newSelection.getCodigoConsulta()));
                fechaConsultaPicker.setValue(newSelection.getFechaConsulta().toLocalDate());
                motivoField.setText(newSelection.getMotivo());
                diagnosticoField.setText(newSelection.getDiagnostico());
                observacionesField.setText(newSelection.getObservaciones());
                codigoMascotaField.setText(String.valueOf(newSelection.getCodigoMascota()));
                codigoVeterinarioField.setText(String.valueOf(newSelection.getCodigoVeterinario()));
            } else {
                codigoConsultaField.clear();
                fechaConsultaPicker.setValue(null);
                motivoField.clear();
                diagnosticoField.clear();
                observacionesField.clear();
                codigoMascotaField.clear();
                codigoVeterinarioField.clear();
            }
        });
    }
    
    public ObservableList<Consultas> buscarConsultaPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Consultas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarConsulta(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Consultas(
                        rs.getInt("codigoConsulta"),
                        rs.getDate("fechaConsulta"),
                        rs.getString("motivo"),
                        rs.getString("diagnostico"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
                        rs.getInt("codigoVeterinario")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar consulta: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Consultas> getConsultas() {
        ObservableList<Consultas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarConsultas() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Consultas(
                        rs.getInt("codigoConsulta"),
                        rs.getDate("fechaConsulta"),
                        rs.getString("motivo"),
                        rs.getString("diagnostico"),
                        rs.getString("observaciones"),
                        rs.getInt("codigoMascota"),
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
        
        codigoConsultaField.clear();
        fechaConsultaPicker.setValue(null);
        motivoField.clear();
        diagnosticoField.clear();
        observacionesField.clear();
        codigoMascotaField.clear();
        codigoVeterinarioField.clear();
        TablaConsulta.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoConsultaField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de consulta para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Consultas> resultado = buscarConsultaPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna consulta con ese código.");
            } else {
                TablaConsulta.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarConsulta(ActionEvent event) {
        reproducirSonidoClick();
        
        LocalDate fecha = fechaConsultaPicker.getValue();
        String motivo = motivoField.getText().trim();
        String diagnostico = diagnosticoField.getText().trim();
        String observaciones = observacionesField.getText().trim();
        String codMascota = codigoMascotaField.getText().trim();
        String codVeterinario = codigoVeterinarioField.getText().trim();
        
        if (fecha == null || motivo.isEmpty() || diagnostico.isEmpty() || observaciones.isEmpty() || codMascota.isEmpty() || codVeterinario.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        int mascota;
        int veterinario;
        try {
            mascota = Integer.parseInt(codMascota);
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

        if (!existeVeterinario(veterinario)) {
            mostrarVentanaError("El código de veterinario ingresado no existe.");
            return;
        }
        
        Consultas consulta = new Consultas();
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaConsultaPicker.getValue());
        consulta.setFechaConsulta(fechaSQL);
        consulta.setMotivo(motivo);
        consulta.setDiagnostico(diagnostico);
        consulta.setObservaciones(observaciones);
        consulta.setCodigoMascota(mascota);
        consulta.setCodigoVeterinario(veterinario);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarConsulta(?, ?, ?, ?, ?, ?) }");
            stmt.setDate(1, consulta.getFechaConsulta());
            stmt.setString(2, consulta.getMotivo());
            stmt.setString(3, consulta.getDiagnostico());
            stmt.setString(4, consulta.getObservaciones());
            stmt.setInt(5, consulta.getCodigoMascota());
            stmt.setInt(6, consulta.getCodigoVeterinario());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar consulta: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarConsulta(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoConsultaField.getText().trim());
            LocalDate fecha = fechaConsultaPicker.getValue();
            String motivo = motivoField.getText().trim();
            String diagnostico = diagnosticoField.getText().trim();
            String observaciones = observacionesField.getText().trim();
            String codMascota = codigoMascotaField.getText().trim();
            String codVeterinario = codigoVeterinarioField.getText().trim();
            
            if (fecha == null || motivo.isEmpty() || diagnostico.isEmpty() || observaciones.isEmpty() || codMascota.isEmpty() || codVeterinario.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }
            
            int mascota;
            int veterinario;
            try {
                mascota = Integer.parseInt(codMascota);
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

            if (!existeVeterinario(veterinario)) {
                mostrarVentanaError("El código de veterinario ingresado no existe.");
                return;
            }
            
            Consultas consulta = new Consultas();
            consulta.setCodigoConsulta(codigo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaConsultaPicker.getValue());
            consulta.setFechaConsulta(fechaSQL);
            consulta.setMotivo(motivo);
            consulta.setDiagnostico(diagnostico);
            consulta.setObservaciones(observaciones);
            consulta.setCodigoMascota(mascota);
            consulta.setCodigoVeterinario(veterinario);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarConsulta(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, consulta.getCodigoConsulta());
            stmt.setDate(2, consulta.getFechaConsulta());
            stmt.setString(3, consulta.getMotivo());
            stmt.setString(4, consulta.getDiagnostico());
            stmt.setString(5, consulta.getObservaciones());
            stmt.setInt(6, consulta.getCodigoMascota());
            stmt.setInt(7, consulta.getCodigoVeterinario());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Consulta actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la consulta debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar consulta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarConsulta(ActionEvent event) {
        reproducirSonidoClick();
        Consultas consultaSeleccionado = TablaConsulta.getSelectionModel().getSelectedItem();

        if (consultaSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarConsulta(?) }");
                stmt.setInt(1, consultaSeleccionado.getCodigoConsulta());
                stmt.execute();

                System.out.println("Consulta eliminada exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta consulta está siendo utilizada en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar consulta: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la consulta.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una consulta de la tabla para eliminar.");
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
    private void botonAgregarCliente (ActionEvent event) {
        if (event.getSource() == btnAgregarCliente) {
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
    private void botonAgregarMascota (ActionEvent event) {
        if (event.getSource() == btnAgregarMascota) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarMascotaView.fxml"));
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
    private void botonAgregarCita (ActionEvent event) {
        if (event.getSource() == btnAgregarCita) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarCitaView.fxml"));
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