
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import org.juliorealiquez.db.Conexion;
import org.juliorealiquez.models.Veterinarios;
import org.juliorealiquez.system.Principal;

public class AgregarVeterinarioController implements Initializable{
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarEmpleado;
    @FXML private Button btnAgregarProveedor;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Veterinarios> TablaVeterinario;
    private ObservableList<Veterinarios> listarVeterinarios;
    @FXML
    private TableColumn<Veterinarios, Integer> codigoVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> NombreVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> ApellidoVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> Especialidad;
    @FXML
    private TableColumn<Veterinarios, String> TelefonoVeterinario;
    @FXML
    private TableColumn<Veterinarios, String> CorreoVeterinario;
    @FXML
    private TableColumn<Veterinarios, java.sql.Date> FechaIngreso;
    @FXML
    private TableColumn<Veterinarios, String> Estado;
    
    // Objetos de texto
    @FXML
    private TextField codigoVeterinarioField;
    @FXML
    private TextField nombreVeterinarioField;
    @FXML
    private TextField apellidoVeterinarioField;
    @FXML
    private TextField especialidadField;
    @FXML
    private TextField telefonoVeterinarioField;
    @FXML
    private TextField correoVeterinarioField;
    @FXML
    private DatePicker fechaIngresoPicker;
    @FXML
    private TextField estadoField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarEmpleado.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarProveedor.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreVeterinarioField, 15);
        limitarTextField(apellidoVeterinarioField, 20);
        limitarTextField(especialidadField, 30);
        limitarTextField(telefonoVeterinarioField, 8);
        limitarTextField(correoVeterinarioField, 30);
        limitarTextField(estadoField, 8);
        
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
        TablaVeterinario.setItems(getVeterinarios());
        
        codigoVeterinario.setCellValueFactory(new PropertyValueFactory<>("codigoVeterinario"));
        NombreVeterinario.setCellValueFactory(new PropertyValueFactory<>("nombreVeterinario"));
        ApellidoVeterinario.setCellValueFactory(new PropertyValueFactory<>("apellidoVeterinario"));
        Especialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        TelefonoVeterinario.setCellValueFactory(new PropertyValueFactory<>("telefonoVeterinario"));
        CorreoVeterinario.setCellValueFactory(new PropertyValueFactory<>("correoVeterinario"));
        FechaIngreso.setCellValueFactory(new PropertyValueFactory<>("fechaIngreso"));
        Estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        FechaIngreso.setCellFactory(column -> new TableCell<Veterinarios, java.sql.Date>() {
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
        listarVeterinarios = FXCollections.observableArrayList();
        
        TablaVeterinario.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoVeterinarioField.setText(String.valueOf(newSelection.getCodigoVeterinario()));
                nombreVeterinarioField.setText(newSelection.getNombreVeterinario());
                apellidoVeterinarioField.setText(newSelection.getApellidoVeterinario());
                especialidadField.setText(newSelection.getEspecialidad());
                telefonoVeterinarioField.setText(newSelection.getTelefonoVeterinario());
                correoVeterinarioField.setText(newSelection.getCorreoVeterinario());
                fechaIngresoPicker.setValue(newSelection.getFechaIngreso().toLocalDate());
                estadoField.setText(newSelection.getEstado());
            } else {
                codigoVeterinarioField.clear();
                nombreVeterinarioField.clear();
                apellidoVeterinarioField.clear();
                especialidadField.clear();
                telefonoVeterinarioField.clear();
                correoVeterinarioField.clear();
                fechaIngresoPicker.setValue(null);
                estadoField.clear();
            }
        });
    }
    
    public ObservableList<Veterinarios> buscarVeterinarioPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Veterinarios> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarVeterinario(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Veterinarios(
                        rs.getInt("codigoVeterinario"),
                        rs.getString("nombreVeterinario"),
                        rs.getString("apellidoVeterinario"),
                        rs.getString("especialidad"),
                        rs.getString("telefonoVeterinario"),
                        rs.getString("correoVeterinario"),
                        rs.getDate("fechaIngreso"),
                        rs.getString("estado")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar veterinario: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Veterinarios> getVeterinarios() {
        ObservableList<Veterinarios> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{CALL sp_ListarVeterinarios()}");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Veterinarios(
                        rs.getInt("codigoVeterinario"),
                        rs.getString("nombreVeterinario"),
                        rs.getString("apellidoVeterinario"),
                        rs.getString("especialidad"),
                        rs.getString("telefonoVeterinario"),
                        rs.getString("correoVeterinario"),
                        rs.getDate("fechaIngreso"),
                        rs.getString("estado")
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
        
        codigoVeterinarioField.clear();
        nombreVeterinarioField.clear();
        apellidoVeterinarioField.clear();
        especialidadField.clear();
        telefonoVeterinarioField.clear();
        correoVeterinarioField.clear();
        fechaIngresoPicker.setValue(null);
        estadoField.clear();
        TablaVeterinario.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoVeterinarioField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de veterinario para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Veterinarios> resultado = buscarVeterinarioPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningún veterinario con ese código.");
            } else {
                TablaVeterinario.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarVeterinario(ActionEvent event) {
        reproducirSonidoClick();
        
        String nombres = nombreVeterinarioField.getText().trim();
        String apellidos = apellidoVeterinarioField.getText().trim();
        String especialidad = especialidadField.getText().trim();
        String telefono = telefonoVeterinarioField.getText().trim();
        String correo = correoVeterinarioField.getText().trim();
        LocalDate fechaIngreso = fechaIngresoPicker.getValue();
        String estado = estadoField.getText().trim();
        
        if (nombres.isEmpty() || apellidos.isEmpty() || especialidad.isEmpty() || telefono.isEmpty() || correo.isEmpty() || fechaIngreso == null || estado.isEmpty()) {
            System.out.println("Error: Todos los campos deben estar llenos.");
            return;
        }
        
        Veterinarios veterinario = new Veterinarios();
        veterinario.setNombreVeterinario(nombres);
        veterinario.setApellidoVeterinario(apellidos);
        veterinario.setEspecialidad(especialidad);
        veterinario.setTelefonoVeterinario(telefono);
        veterinario.setCorreoVeterinario(correo);
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaIngresoPicker.getValue());
        veterinario.setFechaIngreso(fechaSQL);
        veterinario.setEstado(estado);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarVeterinario(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setString(1, veterinario.getNombreVeterinario());
            stmt.setString(2, veterinario.getApellidoVeterinario());
            stmt.setString(3, veterinario.getEspecialidad());
            stmt.setString(4, veterinario.getTelefonoVeterinario());
            stmt.setString(5, veterinario.getCorreoVeterinario());
            stmt.setDate(6, veterinario.getFechaIngreso());
            stmt.setString(7, veterinario.getEstado());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar veterinario: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarVeterinario(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoVeterinarioField.getText().trim());
            String nombres = nombreVeterinarioField.getText().trim();
            String apellidos = apellidoVeterinarioField.getText().trim();
            String especialidad = especialidadField.getText().trim();
            String telefono = telefonoVeterinarioField.getText().trim();
            String correo = correoVeterinarioField.getText().trim();
            LocalDate fechaIngreso = fechaIngresoPicker.getValue();
            String estado = estadoField.getText().trim();
            
            if (telefono.length() > 8) {
                System.out.println("Error: El número de teléfono no debe exceder los 8 caracteres.");
                return;
            }
            
            if (nombres.isEmpty() || apellidos.isEmpty() || especialidad.isEmpty() || telefono.isEmpty() || correo.isEmpty() || fechaIngreso == null || estado.isEmpty()) {
                System.out.println("Error: Todos los campos deben estar llenos.");
                return;
            }
            
            Veterinarios veterinario = new Veterinarios();
            veterinario.setCodigoVeterinario(codigo);
            veterinario.setNombreVeterinario(nombres);
            veterinario.setApellidoVeterinario(apellidos);
            veterinario.setEspecialidad(especialidad);
            veterinario.setTelefonoVeterinario(telefono);
            veterinario.setCorreoVeterinario(correo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaIngresoPicker.getValue());
            veterinario.setFechaIngreso(fechaSQL);
            veterinario.setEstado(estado);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarVeterinario(?, ?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, veterinario.getCodigoVeterinario());
            stmt.setString(2, veterinario.getNombreVeterinario());
            stmt.setString(3, veterinario.getApellidoVeterinario());
            stmt.setString(4, veterinario.getEspecialidad());
            stmt.setString(5, veterinario.getTelefonoVeterinario());
            stmt.setString(6, veterinario.getCorreoVeterinario());
            stmt.setDate(7, veterinario.getFechaIngreso());
            stmt.setString(8, veterinario.getEstado());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Veterinario actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código del veterinario debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar veterinario: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarVeterinario(ActionEvent event) {
        reproducirSonidoClick();
        Veterinarios veterinarioSeleccionado = TablaVeterinario.getSelectionModel().getSelectedItem();

        if (veterinarioSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarVeterinario(?) }");
                stmt.setInt(1, veterinarioSeleccionado.getCodigoVeterinario());
                stmt.execute();

                System.out.println("Veterinario eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Este veterinario está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar veterinario: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar el veterinario.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un veterinario de la tabla para eliminar.");
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
    private void botonAgregarEmpleado (ActionEvent event) {
        if (event.getSource() == btnAgregarEmpleado) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarEmpleadoView.fxml"));
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
    private void botonAgregarProveedor (ActionEvent event) {
        if (event.getSource() == btnAgregarProveedor) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarProveedorView.fxml"));
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
}