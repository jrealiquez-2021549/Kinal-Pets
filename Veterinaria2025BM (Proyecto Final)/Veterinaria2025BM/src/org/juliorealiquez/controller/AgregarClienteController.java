
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
import org.juliorealiquez.models.Clientes;
import org.juliorealiquez.system.Principal;

public class AgregarClienteController implements Initializable{
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarMascota;
    @FXML private Button btnAgregarConsulta;
    @FXML private Button btnAgregarCita;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Clientes> TablaCliente;
    private ObservableList<Clientes> listarClientes;
    @FXML
    private TableColumn<Clientes, Integer> codigoCliente;
    @FXML
    private TableColumn<Clientes, String> NombreCliente;
    @FXML
    private TableColumn<Clientes, String> ApellidoCliente;
    @FXML
    private TableColumn<Clientes, String> TelefonoCliente;
    @FXML
    private TableColumn<Clientes, String> DireccionCliente;
    @FXML
    private TableColumn<Clientes, String> CorreoCliente;
    @FXML
    private TableColumn<Clientes, java.sql.Date> FechaRegistro;
    
    // Objetos de texto
    @FXML
    private TextField codigoClienteField;
    @FXML
    private TextField nombreClienteField;
    @FXML
    private TextField apellidoClienteField;
    @FXML
    private TextField telefonoClienteField;
    @FXML
    private TextField direccionClienteField;
    @FXML
    private TextField correoClienteField;
    @FXML
    private DatePicker fechaRegistroPicker;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarMascota.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarConsulta.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarCita.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreClienteField, 15);
        limitarTextField(apellidoClienteField, 20);
        limitarTextField(telefonoClienteField, 8);
        limitarTextField(direccionClienteField, 100);
        limitarTextField(correoClienteField, 30);
        
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
        TablaCliente.setItems(getClientes());
        
        codigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        NombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        ApellidoCliente.setCellValueFactory(new PropertyValueFactory<>("apellidoCliente"));
        TelefonoCliente.setCellValueFactory(new PropertyValueFactory<>("telefonoCliente"));
        DireccionCliente.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        CorreoCliente.setCellValueFactory(new PropertyValueFactory<>("correoCliente"));
        FechaRegistro.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        
        FechaRegistro.setCellFactory(column -> new TableCell<Clientes, java.sql.Date>() {
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
        listarClientes = FXCollections.observableArrayList();
        
        TablaCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoClienteField.setText(String.valueOf(newSelection.getCodigoCliente()));
                nombreClienteField.setText(newSelection.getNombreCliente());
                apellidoClienteField.setText(newSelection.getApellidoCliente());
                telefonoClienteField.setText(newSelection.getTelefonoCliente());
                direccionClienteField.setText(newSelection.getDireccionCliente());
                correoClienteField.setText(newSelection.getCorreoCliente());
                fechaRegistroPicker.setValue(newSelection.getFechaRegistro().toLocalDate());
            } else {
                codigoClienteField.clear();
                nombreClienteField.clear();
                apellidoClienteField.clear();
                telefonoClienteField.clear();
                direccionClienteField.clear();
                correoClienteField.clear();
                fechaRegistroPicker.setValue(null);
            }
        });
    }
    
    public ObservableList<Clientes> buscarClientePorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Clientes> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarCliente(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("codigoCliente"),
                        rs.getString("nombreCliente"),
                        rs.getString("apellidoCliente"),
                        rs.getString("telefonoCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("correoCliente"),
                        rs.getDate("fechaRegistro")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Clientes> getClientes() {
        ObservableList<Clientes> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarClientes() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("codigoCliente"),
                        rs.getString("nombreCliente"),
                        rs.getString("apellidoCliente"),
                        rs.getString("telefonoCliente"),
                        rs.getString("direccionCliente"),
                        rs.getString("correoCliente"),
                        rs.getDate("fechaRegistro")
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
        
        codigoClienteField.clear();
        nombreClienteField.clear();
        apellidoClienteField.clear();
        telefonoClienteField.clear();
        direccionClienteField.clear();
        correoClienteField.clear();
        fechaRegistroPicker.setValue(null);
        TablaCliente.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoClienteField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de cliente para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Clientes> resultado = buscarClientePorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningún cliente con ese código.");
            } else {
                TablaCliente.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarCliente(ActionEvent event) {
        reproducirSonidoClick();
        
        String nombres = nombreClienteField.getText().trim();
        String apellidos = apellidoClienteField.getText().trim();
        String telefono = telefonoClienteField.getText().trim();
        String direccion = direccionClienteField.getText().trim();
        String correo = correoClienteField.getText().trim();
        LocalDate fechaRegistro = fechaRegistroPicker.getValue();
        
        if (nombres.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || correo.isEmpty() || fechaRegistro == null) {
            System.out.println("Error: Todos los campos deben estar llenos.");
            return;
        }
        
        Clientes cliente = new Clientes();
        cliente.setNombreCliente(nombres);
        cliente.setApellidoCliente(apellidos);
        cliente.setTelefonoCliente(telefono);
        cliente.setDireccionCliente(direccion);
        cliente.setCorreoCliente(correo);
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaRegistroPicker.getValue());
        cliente.setFechaRegistro(fechaSQL);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarCliente(?, ?, ?, ?, ?, ?) }");
            stmt.setString(1, cliente.getNombreCliente());
            stmt.setString(2, cliente.getApellidoCliente());
            stmt.setString(3, cliente.getTelefonoCliente());
            stmt.setString(4, cliente.getDireccionCliente());
            stmt.setString(5, cliente.getCorreoCliente());
            stmt.setDate(6, cliente.getFechaRegistro());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarCliente(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoClienteField.getText().trim());
            String nombres = nombreClienteField.getText().trim();
            String apellidos = apellidoClienteField.getText().trim();
            String telefono = telefonoClienteField.getText().trim();
            String direccion = direccionClienteField.getText().trim();
            String correo = correoClienteField.getText().trim();
            LocalDate fechaRegistro = fechaRegistroPicker.getValue();
            
            if (telefono.length() > 8) {
                System.out.println("Error: El número de teléfono no debe exceder los 8 caracteres.");
                return;
            }
            
            if (nombres.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || correo.isEmpty() || fechaRegistro == null) {
                System.out.println("Error: Todos los campos deben estar llenos.");
                return;
            }
            
            Clientes cliente = new Clientes();
            cliente.setCodigoCliente(codigo);
            cliente.setNombreCliente(nombres);
            cliente.setApellidoCliente(apellidos);
            cliente.setTelefonoCliente(telefono);
            cliente.setDireccionCliente(direccion);
            cliente.setCorreoCliente(correo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaRegistroPicker.getValue());
            cliente.setFechaRegistro(fechaSQL);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarCliente(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, cliente.getCodigoCliente());
            stmt.setString(2, cliente.getNombreCliente());
            stmt.setString(3, cliente.getApellidoCliente());
            stmt.setString(4, cliente.getTelefonoCliente());
            stmt.setString(5, cliente.getDireccionCliente());
            stmt.setString(6, cliente.getCorreoCliente());
            stmt.setDate(7, cliente.getFechaRegistro());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Cliente actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código del cliente debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarCliente(ActionEvent event) {
        reproducirSonidoClick();
        Clientes clienteSeleccionado = TablaCliente.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarCliente(?) }");
                stmt.setInt(1, clienteSeleccionado.getCodigoCliente());
                stmt.execute();

                System.out.println("Cliente eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Este cliente está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar cliente: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar el cliente.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un cliente de la tabla para eliminar.");
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
    private void botonAgregarConsulta (ActionEvent event) {
        if (event.getSource() == btnAgregarConsulta) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/AgregarConsultaView.fxml"));
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
}
