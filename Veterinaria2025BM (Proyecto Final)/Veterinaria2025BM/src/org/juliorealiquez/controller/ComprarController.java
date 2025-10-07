
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
import org.juliorealiquez.models.Compras;
import org.juliorealiquez.system.Principal;

public class ComprarController implements Initializable {
    @FXML private Button btnFactura;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Compras> TablaCompra;
    private ObservableList<Compras> listarCompras;
    @FXML
    private TableColumn<Compras, Integer> codigoCompra;
    @FXML
    private TableColumn<Compras, java.sql.Date> FechaCompra;
    @FXML
    private TableColumn<Compras, Integer> Total;
    @FXML
    private TableColumn<Compras, String> Detalle;
    @FXML
    private TableColumn<Compras, Integer> CodigoProveedor;
    
    // Objetos de texto
    @FXML
    private TextField codigoCompraField;
    @FXML
    private DatePicker fechaCompraPicker;
    @FXML
    private TextField totalField;
    @FXML
    private TextField detalleField;
    @FXML
    private TextField codigoProveedorField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(totalField, 12);
        limitarTextField(detalleField, 200);
        
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
    
    public void cargarDatos() {
        TablaCompra.setItems(getCompras());
        
        codigoCompra.setCellValueFactory(new PropertyValueFactory<>("codigoCompra"));
        FechaCompra.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
        Total.setCellValueFactory(new PropertyValueFactory<>("total"));
        Detalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        CodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
        
        FechaCompra.setCellFactory(column -> new TableCell<Compras, java.sql.Date>() {
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
        listarCompras = FXCollections.observableArrayList();
        
        TablaCompra.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoCompraField.setText(String.valueOf(newSelection.getCodigoCompra()));
                fechaCompraPicker.setValue(newSelection.getFechaCompra().toLocalDate());
                totalField.setText(String.valueOf(newSelection.getTotal()));
                detalleField.setText(newSelection.getDetalle());
                codigoProveedorField.setText(String.valueOf(newSelection.getCodigoProveedor()));
            } else {
                codigoCompraField.clear();
                fechaCompraPicker.setValue(null);
                totalField.clear();
                detalleField.clear();
                codigoProveedorField.clear();
            }
        });
    }
    
    public ObservableList<Compras> buscarCompraPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Compras> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarCompra(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Compras(
                        rs.getInt("codigoCompra"),
                        rs.getDate("fechaCompra"),
                        rs.getInt("total"),
                        rs.getString("detalle"),
                        rs.getInt("codigoCompra")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar compra: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Compras> getCompras() {
        ObservableList<Compras> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarCompras() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Compras(
                        rs.getInt("codigoCompra"),
                        rs.getDate("fechaCompra"),
                        rs.getInt("total"),
                        rs.getString("detalle"),
                        rs.getInt("codigoCompra")
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
        
        codigoCompraField.clear();
        fechaCompraPicker.setValue(null);
        totalField.clear();
        detalleField.clear();
        codigoProveedorField.clear();
        TablaCompra.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoCompraField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de compra para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Compras> resultado = buscarCompraPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna compra con ese código.");
            } else {
                TablaCompra.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarCompra(ActionEvent event) {
        reproducirSonidoClick();
        
        LocalDate fecha = fechaCompraPicker.getValue();
        String totalL = totalField.getText().trim();
        String detalle = detalleField.getText().trim();
        String codProveedor = codigoProveedorField.getText().trim();
        
        if (fecha == null || totalL.isEmpty() || detalle.isEmpty() || codProveedor.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        int total;
        int proveedor;
        try {
            total = Integer.parseInt(totalL);
            proveedor = Integer.parseInt(codProveedor);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los valores deben ser números enteros.");
            return;
        }
        
        Compras compra = new Compras();
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaCompraPicker.getValue());
        compra.setFechaCompra(fechaSQL);
        compra.setTotal(total);
        compra.setDetalle(detalle);
        compra.setCodigoProveedor(proveedor);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarCompra(?, ?, ?, ?) }");
            stmt.setDate(1, compra.getFechaCompra());
            stmt.setInt(2, compra.getTotal());
            stmt.setString(3, compra.getDetalle());
            stmt.setInt(4, compra.getCodigoProveedor());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar compra: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarCompra(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoCompraField.getText().trim());
            LocalDate fecha = fechaCompraPicker.getValue();
            String totalL = totalField.getText().trim();
            String detalle = detalleField.getText().trim();
            String codProveedor = codigoProveedorField.getText().trim();

            if (fecha == null || totalL.isEmpty() || detalle.isEmpty() || codProveedor.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }

            int total;
            int proveedor;
            try {
                total = Integer.parseInt(totalL);
                proveedor = Integer.parseInt(codProveedor);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los valores deben ser números enteros.");
                return;
            }
            
            Compras compra = new Compras();
            compra.setCodigoCompra(codigo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaCompraPicker.getValue());
            compra.setFechaCompra(fechaSQL);
            compra.setTotal(total);
            compra.setDetalle(detalle);
            compra.setCodigoProveedor(proveedor);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarCompra(?, ?, ?, ?, ?) }");
            stmt.setInt(1, compra.getCodigoCompra());
            stmt.setDate(2, compra.getFechaCompra());
            stmt.setInt(3, compra.getTotal());
            stmt.setString(4, compra.getDetalle());
            stmt.setInt(5, compra.getCodigoProveedor());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Compra actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la compra debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar compra: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarCompra(ActionEvent event) {
        reproducirSonidoClick();
        Compras compraSeleccionado = TablaCompra.getSelectionModel().getSelectedItem();

        if (compraSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarCompra(?) }");
                stmt.setInt(1, compraSeleccionado.getCodigoCompra());
                stmt.execute();

                System.out.println("Compra eliminada exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta compra está siendo utilizada en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar compra: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la compra.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una compra de la tabla para eliminar.");
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
    
    private boolean existeProveedor(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarProveedor(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
