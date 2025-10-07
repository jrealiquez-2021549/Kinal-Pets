
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
import org.juliorealiquez.models.Facturas;
import org.juliorealiquez.system.Principal;

public class FacturaController implements Initializable{
    @FXML private Button btnInforme;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Facturas> TablaFactura;
    private ObservableList<Facturas> listarFacturas;
    @FXML
    private TableColumn<Facturas, Integer> codigoFactura;
    @FXML
    private TableColumn<Facturas, java.sql.Date> FechaEmision;
    @FXML
    private TableColumn<Facturas, Double> Total;
    @FXML
    private TableColumn<Facturas, String> MetodoPago;
    @FXML
    private TableColumn<Facturas, Integer> CodigoCliente;
    @FXML
    private TableColumn<Facturas, Integer> CodigoEmpleado;
    
    // Objetos de texto
    @FXML
    private TextField codigoFacturaField;
    @FXML
    private DatePicker fechaEmisionPicker;
    @FXML
    private TextField totalField;
    @FXML
    private TextField metodoPagoField;
    @FXML
    private TextField codigoClienteField;
    @FXML
    private TextField codigoEmpleadoField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnInforme.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(metodoPagoField, 15);
        
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
        TablaFactura.setItems(getFacturas());
        
        codigoFactura.setCellValueFactory(new PropertyValueFactory<>("codigoFactura"));
        FechaEmision.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        Total.setCellValueFactory(new PropertyValueFactory<>("total"));
        MetodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
        CodigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        CodigoEmpleado.setCellValueFactory(new PropertyValueFactory<>("codigoEmpleado"));
        
        FechaEmision.setCellFactory(column -> new TableCell<Facturas, java.sql.Date>() {
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
        listarFacturas = FXCollections.observableArrayList();
        
        TablaFactura.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoFacturaField.setText(String.valueOf(newSelection.getCodigoFactura()));
                fechaEmisionPicker.setValue(newSelection.getFechaEmision().toLocalDate());
                totalField.setText(String.valueOf(newSelection.getTotal()));
                metodoPagoField.setText(newSelection.getMetodoPago());
                codigoClienteField.setText(String.valueOf(newSelection.getCodigoCliente()));
                codigoEmpleadoField.setText(String.valueOf(newSelection.getCodigoEmpleado()));
            } else {
                codigoFacturaField.clear();
                fechaEmisionPicker.setValue(null);
                totalField.clear();
                metodoPagoField.clear();
                codigoClienteField.clear();
                codigoEmpleadoField.clear();
            }
        });
    }
    
    public ObservableList<Facturas> buscarFacturaPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Facturas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarFactura(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Facturas(
                        rs.getInt("codigoFactura"),
                        rs.getDate("fechaEmision"),
                        rs.getInt("total"),
                        rs.getString("metodoPago"),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoEmpleado")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar factura: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Facturas> getFacturas() {
        ObservableList<Facturas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarFacturas() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Facturas(
                        rs.getInt("codigoFactura"),
                        rs.getDate("fechaEmision"),
                        rs.getInt("total"),
                        rs.getString("metodoPago"),
                        rs.getInt("codigoCliente"),
                        rs.getInt("codigoEmpleado")
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
        
        codigoFacturaField.clear();
        fechaEmisionPicker.setValue(null);
        totalField.clear();
        metodoPagoField.clear();
        codigoClienteField.clear();
        codigoEmpleadoField.clear();
        TablaFactura.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoFacturaField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de factura para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Facturas> resultado = buscarFacturaPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna factura con ese código.");
            } else {
                TablaFactura.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarFactura(ActionEvent event) {
        reproducirSonidoClick();
        
        LocalDate fecha = fechaEmisionPicker.getValue();
        String totalL = totalField.getText().trim();
        String metodo = metodoPagoField.getText().trim();
        String codCliente = codigoClienteField.getText().trim();
        String codEmpleado = codigoEmpleadoField.getText().trim();
        
        if (fecha == null || totalL.isEmpty() || metodo.isEmpty() || codCliente.isEmpty() || codEmpleado.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        double total;
        int cliente;
        int empleado;
        try {
            total = Double.parseDouble(totalL);
            cliente = Integer.parseInt(codCliente);
            empleado = Integer.parseInt(codEmpleado);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los valores deben ser números enteros.");
            return;
        }
        
        // Verificación de llaves foráneas
        if (!existeCliente(cliente)) {
            mostrarVentanaError("El código de cliente ingresado no existe.");
            return;
        }

        if (!existeEmpleado(empleado)) {
            mostrarVentanaError("El código de empleado ingresado no existe.");
            return;
        }
        
        Facturas factura = new Facturas();
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaEmisionPicker.getValue());
        factura.setFechaEmision(fechaSQL);
        factura.setTotal(total);
        factura.setMetodoPago(metodo);
        factura.setCodigoCliente(cliente);
        factura.setCodigoEmpleado(empleado);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarFactura(?, ?, ?, ?, ?) }");
            stmt.setDate(1, factura.getFechaEmision());
            stmt.setDouble(2, factura.getTotal());
            stmt.setString(3, factura.getMetodoPago());
            stmt.setInt(4, factura.getCodigoCliente());
            stmt.setInt(5, factura.getCodigoEmpleado());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar factura: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarFactura(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoFacturaField.getText().trim());
            LocalDate fecha = fechaEmisionPicker.getValue();
            String totalL = totalField.getText().trim();
            String metodo = metodoPagoField.getText().trim();
            String codCliente = codigoClienteField.getText().trim();
            String codEmpleado = codigoEmpleadoField.getText().trim();
            
            if (fecha == null || totalL.isEmpty() || metodo.isEmpty() || codCliente.isEmpty() || codEmpleado.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }

            double total;
            int cliente;
            int empleado;
            try {
                total = Double.parseDouble(totalL);
                cliente = Integer.parseInt(codCliente);
                empleado = Integer.parseInt(codEmpleado);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los valores deben ser números enteros.");
                return;
            }
            
            // Verificación de llaves foráneas
            if (!existeCliente(cliente)) {
                mostrarVentanaError("El código de cliente ingresado no existe.");
                return;
            }

            if (!existeEmpleado(empleado)) {
                mostrarVentanaError("El código de empleado ingresado no existe.");
                return;
            }
            
            Facturas factura = new Facturas();
            factura.setCodigoFactura(codigo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaEmisionPicker.getValue());
            factura.setFechaEmision(fechaSQL);
            factura.setTotal(total);
            factura.setMetodoPago(metodo);
            factura.setCodigoCliente(cliente);
            factura.setCodigoEmpleado(empleado);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarFactura(?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, factura.getCodigoFactura());
            stmt.setDate(2, factura.getFechaEmision());
            stmt.setDouble(3, factura.getTotal());
            stmt.setString(4, factura.getMetodoPago());
            stmt.setInt(5, factura.getCodigoCliente());
            stmt.setInt(6, factura.getCodigoEmpleado());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Factura actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la factura debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar factura: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarFactura(ActionEvent event) {
        reproducirSonidoClick();
        Facturas facturaSeleccionado = TablaFactura.getSelectionModel().getSelectedItem();

        if (facturaSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarFactura(?) }");
                stmt.setInt(1, facturaSeleccionado.getCodigoFactura());
                stmt.execute();

                System.out.println("Factura eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta factura está siendo utilizada en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar factura: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la factura.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una factura de la tabla para eliminar.");
        }
    }
    
    @FXML
    private void botonInforme (ActionEvent event) {
        if (event.getSource() == btnInforme) {
            reproducirSonidoClick();
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/org/juliorealiquez/view/InformeView2.fxml"));
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
    
    private boolean existeCliente(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarCliente(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean existeEmpleado(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarEmpleado(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
