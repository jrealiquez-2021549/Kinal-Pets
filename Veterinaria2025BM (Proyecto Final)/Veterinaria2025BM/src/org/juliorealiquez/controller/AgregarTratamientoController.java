
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
import org.juliorealiquez.models.Tratamientos;
import org.juliorealiquez.system.Principal;

public class AgregarTratamientoController implements Initializable{
    @FXML private Button btnAgregarVacuna;
    @FXML private Button btnAgregarMedicina;
    @FXML private Button btnAgregarVacunacion;
    @FXML private Button btnAgregarReceta;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Tratamientos> TablaTratamiento;
    private ObservableList<Tratamientos> listarTratamientos;
    @FXML
    private TableColumn<Tratamientos, Integer> codigoTratamiento;
    @FXML
    private TableColumn<Tratamientos, String> Descripcion;
    @FXML
    private TableColumn<Tratamientos, java.sql.Date> FechaInicio;
    @FXML
    private TableColumn<Tratamientos, java.sql.Date> FechaFin;
    @FXML
    private TableColumn<Tratamientos, String> MedicamentosIndicados;
    @FXML
    private TableColumn<Tratamientos, Integer> CodigoConsulta;
    
    // Objetos de texto
    @FXML
    private TextField codigoTratamientoField;
    @FXML
    private TextField descripcionField;
    @FXML
    private DatePicker fechaInicioPicker;
    @FXML
    private DatePicker fechaFinPicker;
    @FXML
    private TextField medicamentosIndicadosField;
    @FXML
    private TextField codigoConsultaField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAgregarVacuna.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarMedicina.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarVacunacion.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarReceta.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(descripcionField, 200);
        limitarTextField(medicamentosIndicadosField, 50);
        
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
        TablaTratamiento.setItems(getTratamientos());
        
        codigoTratamiento.setCellValueFactory(new PropertyValueFactory<>("codigoTratamiento"));
        Descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        FechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        FechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        MedicamentosIndicados.setCellValueFactory(new PropertyValueFactory<>("medicamentosIndicados"));
        CodigoConsulta.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        
        FechaInicio.setCellFactory(column -> new TableCell<Tratamientos, java.sql.Date>() {
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
        
        FechaFin.setCellFactory(column -> new TableCell<Tratamientos, java.sql.Date>() {
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
        listarTratamientos = FXCollections.observableArrayList();
        
        TablaTratamiento.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoTratamientoField.setText(String.valueOf(newSelection.getCodigoTratamiento()));
                descripcionField.setText(newSelection.getDescripcion());
                fechaInicioPicker.setValue(newSelection.getFechaInicio().toLocalDate());
                fechaFinPicker.setValue(newSelection.getFechaFin().toLocalDate());
                medicamentosIndicadosField.setText(newSelection.getMedicamentosIndicados());
                codigoConsultaField.setText(String.valueOf(newSelection.getCodigoConsulta()));
            } else {
                codigoTratamientoField.clear();
                descripcionField.clear();
                fechaInicioPicker.setValue(null);
                fechaFinPicker.setValue(null);
                medicamentosIndicadosField.clear();
                codigoConsultaField.clear();
            }
        });
    }
    
    public ObservableList<Tratamientos> buscarTratamientoPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Tratamientos> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarTratamiento(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Tratamientos(
                        rs.getInt("codigoTratamiento"),
                        rs.getString("descripcion"),
                        rs.getDate("fechaInicio"),
                        rs.getDate("fechaFin"),
                        rs.getString("medicamentosIndicados"),
                        rs.getInt("codigoConsulta")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar tratamiento: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Tratamientos> getTratamientos() {
        ObservableList<Tratamientos> lista = FXCollections.observableArrayList();
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarTratamientos() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Tratamientos(
                        rs.getInt("codigoTratamiento"),
                        rs.getString("descripcion"),
                        rs.getDate("fechaInicio"),
                        rs.getDate("fechaFin"),
                        rs.getString("medicamentosIndicados"),
                        rs.getInt("codigoConsulta")
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
        
        codigoTratamientoField.clear();
        descripcionField.clear();
        fechaInicioPicker.setValue(null);
        fechaFinPicker.setValue(null);
        medicamentosIndicadosField.clear();
        codigoConsultaField.clear();
        TablaTratamiento.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoTratamientoField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de tratamiento para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Tratamientos> resultado = buscarTratamientoPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningún tratamiento con ese código.");
            } else {
                TablaTratamiento.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarTratamiento(ActionEvent event) {
        reproducirSonidoClick();
        
        String descripcion = descripcionField.getText().trim();
        LocalDate fechaI = fechaInicioPicker.getValue();
        LocalDate fechaF = fechaFinPicker.getValue();
        String medicamento = medicamentosIndicadosField.getText().trim();
        String codConsulta = codigoConsultaField.getText().trim();
        
        if (descripcion.isEmpty() || fechaI == null || fechaF == null || medicamento.isEmpty() || codConsulta.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        int consulta;
        try {
            consulta = Integer.parseInt(codConsulta);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los códigos deben ser números enteros.");
            return;
        }
        
        // Verificación de llaves foráneas
        if (!existeConsulta(consulta)) {
            mostrarVentanaError("El código de consulta ingresado no existe.");
            return;
        }
        
        Tratamientos tratamiento = new Tratamientos();
        tratamiento.setDescripcion(descripcion);
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaInicioPicker.getValue());
        tratamiento.setFechaInicio(fechaSQL);
        java.sql.Date fechaSQL2 = java.sql.Date.valueOf(fechaFinPicker.getValue());
        tratamiento.setFechaFin(fechaSQL2);
        tratamiento.setMedicamentosIndicados(medicamento);
        tratamiento.setCodigoConsulta(consulta);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarTratamiento(?, ?, ?, ?, ?) }");
            stmt.setString(1, tratamiento.getDescripcion());
            stmt.setDate(2, tratamiento.getFechaInicio());
            stmt.setDate(3, tratamiento.getFechaFin());
            stmt.setString(4, tratamiento.getMedicamentosIndicados());
            stmt.setInt(5, tratamiento.getCodigoConsulta());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar tratamiento: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarTratamiento(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoTratamientoField.getText().trim());
            String descripcion = descripcionField.getText().trim();
            LocalDate fechaI = fechaInicioPicker.getValue();
            LocalDate fechaF = fechaFinPicker.getValue();
            String medicamento = medicamentosIndicadosField.getText().trim();
            String codConsulta = codigoConsultaField.getText().trim();
            
            if (descripcion.isEmpty() || fechaI == null || fechaF == null || medicamento.isEmpty() || codConsulta.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }
            
            int consulta;
            try {
                consulta = Integer.parseInt(codConsulta);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los códigos deben ser números enteros.");
                return;
            }
            
            // Verificación de llaves foráneas
            if (!existeConsulta(consulta)) {
                mostrarVentanaError("El código de consulta ingresado no existe.");
                return;
            }
            
            Tratamientos tratamiento = new Tratamientos();
            tratamiento.setCodigoTratamiento(codigo);
            tratamiento.setDescripcion(descripcion);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaInicioPicker.getValue());
            tratamiento.setFechaInicio(fechaSQL);
            java.sql.Date fechaSQL2 = java.sql.Date.valueOf(fechaFinPicker.getValue());
            tratamiento.setFechaFin(fechaSQL2);
            tratamiento.setMedicamentosIndicados(medicamento);
            tratamiento.setCodigoConsulta(consulta);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarTratamiento(?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, tratamiento.getCodigoTratamiento());
            stmt.setString(2, tratamiento.getDescripcion());
            stmt.setDate(3, tratamiento.getFechaInicio());
            stmt.setDate(4, tratamiento.getFechaFin());
            stmt.setString(5, tratamiento.getMedicamentosIndicados());
            stmt.setInt(6, tratamiento.getCodigoConsulta());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Tratamiento actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código del tratamieno debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar tratamiento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarTratamiento(ActionEvent event) {
        reproducirSonidoClick();
        Tratamientos tratamientoSeleccionado = TablaTratamiento.getSelectionModel().getSelectedItem();

        if (tratamientoSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarTratamiento(?) }");
                stmt.setInt(1, tratamientoSeleccionado.getCodigoTratamiento());
                stmt.execute();

                System.out.println("Tratamiento eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Este tratamiento está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar tratamiento: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar el tratamiento.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un tratamiento de la tabla para eliminar.");
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
    private void botonAgregarVacunacion (ActionEvent event) {
        if (event.getSource() == btnAgregarVacunacion) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarVacunacionView.fxml"));
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
    
    private boolean existeConsulta(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarConsulta(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
