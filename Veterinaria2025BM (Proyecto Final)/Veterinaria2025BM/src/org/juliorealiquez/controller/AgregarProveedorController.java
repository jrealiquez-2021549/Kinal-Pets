
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
import org.juliorealiquez.models.Proveedores;
import org.juliorealiquez.system.Principal;

public class AgregarProveedorController implements Initializable {
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarVeterinario;
    @FXML private Button btnAgregarEmpleado;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Proveedores> TablaProveedor;
    private ObservableList<Proveedores> listarProveedores;
    @FXML
    private TableColumn<Proveedores, Integer> codigoProveedor;
    @FXML
    private TableColumn<Proveedores, String> NombreProveedor;
    @FXML
    private TableColumn<Proveedores, String> DireccionProveedor;
    @FXML
    private TableColumn<Proveedores, String> TelefonoProveedor;
    @FXML
    private TableColumn<Proveedores, String> CorreoProveedor;
    
    // Objetos de texto
    @FXML
    private TextField codigoProveedorField;
    @FXML
    private TextField nombreProveedorField;
    @FXML
    private TextField direccionProveedorField;
    @FXML
    private TextField telefonoProveedorField;
    @FXML
    private TextField correoProveedorField;
    
    private boolean reproduciendo = true;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarVeterinario.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarEmpleado.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreProveedorField, 15);
        limitarTextField(direccionProveedorField, 100);
        limitarTextField(telefonoProveedorField, 8);
        limitarTextField(correoProveedorField, 30);
        
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
        TablaProveedor.setItems(getProveedores());
        
        codigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
        NombreProveedor.setCellValueFactory(new PropertyValueFactory<>("nombreProveedor"));
        DireccionProveedor.setCellValueFactory(new PropertyValueFactory<>("direccionProveedor"));
        TelefonoProveedor.setCellValueFactory(new PropertyValueFactory<>("telefonoProveedor"));
        CorreoProveedor.setCellValueFactory(new PropertyValueFactory<>("correoProveedor"));
    }
    
    public void MostrarLosDatos() {
        listarProveedores = FXCollections.observableArrayList();
        
        TablaProveedor.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoProveedorField.setText(String.valueOf(newSelection.getCodigoProveedor()));
                nombreProveedorField.setText(newSelection.getNombreProveedor());
                direccionProveedorField.setText(newSelection.getDireccionProveedor());
                telefonoProveedorField.setText(newSelection.getTelefonoProveedor());
                correoProveedorField.setText(newSelection.getCorreoProveedor());
            } else {
                codigoProveedorField.clear();
                nombreProveedorField.clear();
                direccionProveedorField.clear();
                telefonoProveedorField.clear();
                correoProveedorField.clear();
            }
        });
    }
    
    public ObservableList<Proveedores> buscarProveedorPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Proveedores> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarProveedor(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Proveedores(
                        rs.getInt("codigoProveedor"),
                        rs.getString("nombreProveedor"),
                        rs.getString("direccionProveedor"),
                        rs.getString("telefonoProveedor"),
                        rs.getString("correoProveedor")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar proveedor: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Proveedores> getProveedores() {
        ObservableList<Proveedores> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{CALL sp_ListarProveedores()}");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Proveedores(
                        rs.getInt("codigoProveedor"),
                        rs.getString("nombreProveedor"),
                        rs.getString("direccionProveedor"),
                        rs.getString("telefonoProveedor"),
                        rs.getString("correoProveedor")
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
        
        codigoProveedorField.clear();
        nombreProveedorField.clear();
        direccionProveedorField.clear();
        telefonoProveedorField.clear();
        correoProveedorField.clear();
        TablaProveedor.getSelectionModel().clearSelection();
        cargarDatos();
    }
 
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoProveedorField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de proveedor para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Proveedores> resultado = buscarProveedorPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningún proveedor con ese código.");
            } else {
                TablaProveedor.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }

    
    @FXML
    void handleButtonPressAgregarProveedor(ActionEvent event) {
        reproducirSonidoClick();
        
        String nombres = nombreProveedorField.getText().trim();
        String direccion = direccionProveedorField.getText().trim();
        String telefono = telefonoProveedorField.getText().trim();
        String correo = correoProveedorField.getText().trim();
        
        if (nombres.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
            System.out.println("Error: Todos los campos deben estar llenos.");
            return;
        }
        
        Proveedores proveedor = new Proveedores();
        proveedor.setNombreProveedor(nombres);
        proveedor.setDireccionProveedor(direccion);
        proveedor.setTelefonoProveedor(telefono);
        proveedor.setCorreoProveedor(correo);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarProveedor(?, ?, ?, ?) }");
            stmt.setString(1, proveedor.getNombreProveedor());
            stmt.setString(2, proveedor.getDireccionProveedor());
            stmt.setString(3, proveedor.getTelefonoProveedor());
            stmt.setString(4, proveedor.getCorreoProveedor());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar proveedor: " + e.getMessage());
        }
    }

    @FXML
    void handleButtonPressEditarProveedor(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoProveedorField.getText().trim());
            String nombres = nombreProveedorField.getText().trim();
            String direccion = direccionProveedorField.getText().trim();
            String telefono = telefonoProveedorField.getText().trim();
            String correo = correoProveedorField.getText().trim();
            
            if (telefono.length() > 10) {
                System.out.println("Error: El número de teléfono no debe exceder los 10 caracteres.");
                return;
            }
            
            if (nombres.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || correo.isEmpty()) {
                System.out.println("Error: Todos los campos deben estar llenos.");
                return;
            }
            
            Proveedores proveedor = new Proveedores();
            proveedor.setCodigoProveedor(codigo); // <-- Aquí corregido
            proveedor.setNombreProveedor(nombres);
            proveedor.setDireccionProveedor(direccion);
            proveedor.setTelefonoProveedor(telefono);
            proveedor.setCorreoProveedor(correo);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarProveedor(?, ?, ?, ?, ?) }");
            stmt.setInt(1, proveedor.getCodigoProveedor());
            stmt.setString(2, proveedor.getNombreProveedor());
            stmt.setString(3, proveedor.getDireccionProveedor()); 
            stmt.setString(4, proveedor.getTelefonoProveedor()); 
            stmt.setString(5, proveedor.getCorreoProveedor());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Proveedor actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código del proveedor debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar proveedor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleButtonPressEliminarProveedor(ActionEvent event) {
        reproducirSonidoClick();
        Proveedores proveedorSeleccionado = TablaProveedor.getSelectionModel().getSelectedItem();

        if (proveedorSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarProveedor(?) }");
                stmt.setInt(1, proveedorSeleccionado.getCodigoProveedor());
                stmt.execute();

                System.out.println("Proveedor eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Este proveedor está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar proveedor: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar el proveedor.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un proveedor de la tabla para eliminar.");
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