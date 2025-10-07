
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.controller;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import org.juliorealiquez.db.Conexion;
import org.juliorealiquez.models.Empleados;
import org.juliorealiquez.system.Principal;

public class AgregarEmpleadoController implements Initializable{
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarVeterinario;
    @FXML private Button btnAgregarProveedor;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Empleados> TablaEmpleado;
    private ObservableList<Empleados> listarEmpleados;
    @FXML
    private TableColumn<Empleados, Integer> codigoEmpleado;
    @FXML
    private TableColumn<Empleados, String> NombreEmpleado;
    @FXML
    private TableColumn<Empleados, String> ApellidoEmpleado;
    @FXML
    private TableColumn<Empleados, String> Cargo;
    @FXML
    private TableColumn<Empleados, String> TelefonoEmpleado;
    @FXML
    private TableColumn<Empleados, String> CorreoEmpleado;
    
    // Objetos de texto
    @FXML
    private TextField codigoEmpleadoField;
    @FXML
    private TextField nombreEmpleadoField;
    @FXML
    private TextField apellidoEmpleadoField;
    @FXML
    private TextField cargoField;
    @FXML
    private TextField telefonoEmpleadoField;
    @FXML
    private TextField correoEmpleadoField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarVeterinario.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarProveedor.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreEmpleadoField, 15);
        limitarTextField(apellidoEmpleadoField, 20);
        limitarTextField(cargoField, 50);
        limitarTextField(telefonoEmpleadoField, 8);
        limitarTextField(correoEmpleadoField, 30);
        
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
        TablaEmpleado.setItems(getEmpleados());
        
        codigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        NombreEmpleado.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
        ApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<>("apellidoEmpleado"));
        Cargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        TelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<>("telefonoEmpleado"));
        CorreoEmpleado.setCellValueFactory(new PropertyValueFactory<>("correoEmpleado"));
    }
    
    public void MostrarLosDatos() {
        listarEmpleados = FXCollections.observableArrayList();
        
        TablaEmpleado.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoEmpleadoField.setText(String.valueOf(newSelection.getCodigoEmpleado()));
                nombreEmpleadoField.setText(newSelection.getNombreEmpleado());
                apellidoEmpleadoField.setText(newSelection.getApellidoEmpleado());
                cargoField.setText(newSelection.getCargo());
                telefonoEmpleadoField.setText(newSelection.getTelefonoEmpleado());
                correoEmpleadoField.setText(newSelection.getCorreoEmpleado());
            } else {
                codigoEmpleadoField.clear();
                nombreEmpleadoField.clear();
                apellidoEmpleadoField.clear();
                cargoField.clear();
                telefonoEmpleadoField.clear();
                correoEmpleadoField.clear();
            }
        });
    }
    
    public ObservableList<Empleados> buscarEmpleadoPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Empleados> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarEmpleado(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Empleados(
                        rs.getInt("codigoEmpleado"),
                        rs.getString("nombreEmpleado"),
                        rs.getString("apellidoEmpleado"),
                        rs.getString("cargo"),
                        rs.getString("telefonoEmpleado"),
                        rs.getString("correoEmpleado")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Empleados> getEmpleados() {
        ObservableList<Empleados> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarEmpleados() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Empleados(
                        rs.getInt("codigoEmpleado"),
                        rs.getString("nombreEmpleado"),
                        rs.getString("apellidoEmpleado"),
                        rs.getString("cargo"),
                        rs.getString("telefonoEmpleado"),
                        rs.getString("correoEmpleado")
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
        
        codigoEmpleadoField.clear();
        nombreEmpleadoField.clear();
        apellidoEmpleadoField.clear();
        cargoField.clear();
        telefonoEmpleadoField.clear();
        correoEmpleadoField.clear();
        TablaEmpleado.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoEmpleadoField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de empleado para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Empleados> resultado = buscarEmpleadoPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningún empleado con ese código.");
            } else {
                TablaEmpleado.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarEmpleado(ActionEvent event) {
        reproducirSonidoClick();
        
        String nombres = nombreEmpleadoField.getText().trim();
        String apellidos = apellidoEmpleadoField.getText().trim();
        String cargo = cargoField.getText().trim();
        String telefono = telefonoEmpleadoField.getText().trim();
        String correo = correoEmpleadoField.getText().trim();
        
        if (nombres.isEmpty() || apellidos.isEmpty() || cargo.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            System.out.println("Error: Todos los campos deben estar llenos.");
            return;
        }
        
        Empleados empleado = new Empleados();
        empleado.setNombreEmpleado(nombres);
        empleado.setApellidoEmpleado(apellidos);
        empleado.setCargo(cargo);
        empleado.setTelefonoEmpleado(telefono);
        empleado.setCorreoEmpleado(correo);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarEmpleado(?, ?, ?, ?, ?) }");
            stmt.setString(1, empleado.getNombreEmpleado());
            stmt.setString(2, empleado.getApellidoEmpleado());
            stmt.setString(3, empleado.getCargo());
            stmt.setString(4, empleado.getTelefonoEmpleado());
            stmt.setString(5, empleado.getCorreoEmpleado());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar empleado: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarEmpleado(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoEmpleadoField.getText().trim());
            String nombres = nombreEmpleadoField.getText().trim();
            String apellidos = apellidoEmpleadoField.getText().trim();
            String cargo = cargoField.getText().trim();
            String telefono = telefonoEmpleadoField.getText().trim();
            String correo = correoEmpleadoField.getText().trim();
            
            if (telefono.length() > 8) {
                System.out.println("Error: El número de teléfono no debe exceder los 8 caracteres.");
                return;
            }
            
            if (nombres.isEmpty() || apellidos.isEmpty() || cargo.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                System.out.println("Error: Todos los campos deben estar llenos.");
                return;
            }
            
            Empleados empleado = new Empleados();
            empleado.setCodigoEmpleado(codigo);
            empleado.setNombreEmpleado(nombres);
            empleado.setApellidoEmpleado(apellidos);
            empleado.setCargo(cargo);
            empleado.setTelefonoEmpleado(telefono);
            empleado.setCorreoEmpleado(correo);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarEmpleado(?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, empleado.getCodigoEmpleado());
            stmt.setString(2, empleado.getNombreEmpleado());
            stmt.setString(3, empleado.getApellidoEmpleado());
            stmt.setString(4, empleado.getCargo());
            stmt.setString(5, empleado.getTelefonoEmpleado());
            stmt.setString(6, empleado.getCorreoEmpleado());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Empleado actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código del empleado debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar empleado: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarEmpleado(ActionEvent event) {
        reproducirSonidoClick();
        Empleados empleadoSeleccionado = TablaEmpleado.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarEmpleado(?) }");
                stmt.setInt(1, empleadoSeleccionado.getCodigoEmpleado());
                stmt.execute();

                System.out.println("Empleado eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Este empleado está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar empleado: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar el empleado.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un empleado de la tabla para eliminar.");
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
    private void botonAgregarVeterinario (ActionEvent event) {
        if (event.getSource() == btnAgregarVeterinario) {
            reproducirSonidoClick();
            
            try {
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