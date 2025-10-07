
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
import org.juliorealiquez.models.Medicamentos;
import org.juliorealiquez.system.Principal;

public class AgregarMedicinaController implements Initializable{
    @FXML private Button btnAgregarVacuna;
    @FXML private Button btnAgregarVacunacion;
    @FXML private Button btnAgregarTratamiento;
    @FXML private Button btnAgregarReceta;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Medicamentos> TablaMedicamento;
    private ObservableList<Medicamentos> listarMedicamentos;
    @FXML
    private TableColumn<Medicamentos, Integer> codigoMedicamento;
    @FXML
    private TableColumn<Medicamentos, String> NombreMedicamento;
    @FXML
    private TableColumn<Medicamentos, String> Descripcion;
    @FXML
    private TableColumn<Medicamentos, Integer> Stock;
    @FXML
    private TableColumn<Medicamentos, Double> PrecioUnitario;
    @FXML
    private TableColumn<Medicamentos, java.sql.Date> FechaVencimiento;
    @FXML
    private TableColumn<Medicamentos, Integer> CodigoProveedor;
    
    // Objetos de texto
    @FXML
    private TextField codigoMedicamentoField;
    @FXML
    private TextField nombreMedicamentoField;
    @FXML
    private TextField descripcionField;
    @FXML
    private TextField stockField;
    @FXML
    private TextField precioUnitarioField;
    @FXML
    private DatePicker fechaVencimientoPicker;
    @FXML
    private TextField codigoProveedorField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAgregarVacuna.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarVacunacion.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarTratamiento.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarReceta.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreMedicamentoField, 30);
        limitarTextField(descripcionField, 250);
        limitarTextField(precioUnitarioField, 12);
        
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
        TablaMedicamento.setItems(getMedicamentos());
        
        codigoMedicamento.setCellValueFactory(new PropertyValueFactory<>("codigoMedicamento"));
        NombreMedicamento.setCellValueFactory(new PropertyValueFactory<>("nombreMedicamento"));
        Descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        Stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        FechaVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        CodigoProveedor.setCellValueFactory(new PropertyValueFactory<>("codigoProveedor"));
        
        FechaVencimiento.setCellFactory(column -> new TableCell<Medicamentos, java.sql.Date>() {
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
        listarMedicamentos = FXCollections.observableArrayList();
        
        TablaMedicamento.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoMedicamentoField.setText(String.valueOf(newSelection.getCodigoMedicamento()));
                nombreMedicamentoField.setText(newSelection.getNombreMedicamento());
                descripcionField.setText(newSelection.getDescripcion());
                stockField.setText(String.valueOf(newSelection.getStock()));
                precioUnitarioField.setText(String.valueOf(newSelection.getPrecioUnitario()));
                fechaVencimientoPicker.setValue(newSelection.getFechaVencimiento().toLocalDate());
                codigoProveedorField.setText(String.valueOf(newSelection.getCodigoProveedor()));
            } else {
                codigoMedicamentoField.clear();
                nombreMedicamentoField.clear();
                descripcionField.clear();
                stockField.clear();
                precioUnitarioField.clear();
                fechaVencimientoPicker.setValue(null);
                codigoProveedorField.clear();
            }
        });
    }
    
    public ObservableList<Medicamentos> buscarMedicamentoPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Medicamentos> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarMedicamento(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Medicamentos(
                        rs.getInt("codigoMedicamento"),
                        rs.getString("nombreMedicamento"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precioUnitario"),
                        rs.getDate("fechaVencimiento"),
                        rs.getInt("codigoProveedor")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar medicamento: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Medicamentos> getMedicamentos() {
        ObservableList<Medicamentos> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarMedicamentos() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Medicamentos(
                        rs.getInt("codigoMedicamento"),
                        rs.getString("nombreMedicamento"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precioUnitario"),
                        rs.getDate("fechaVencimiento"),
                        rs.getInt("codigoProveedor")
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
        
        codigoMedicamentoField.clear();
        nombreMedicamentoField.clear();
        descripcionField.clear();
        stockField.clear();
        precioUnitarioField.clear();
        fechaVencimientoPicker.setValue(null);
        codigoProveedorField.clear();
        TablaMedicamento.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoMedicamentoField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de medicamento para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Medicamentos> resultado = buscarMedicamentoPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningún medicamento con ese código.");
            } else {
                TablaMedicamento.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarMedicamento(ActionEvent event) {
        reproducirSonidoClick();
        
        String nombres = nombreMedicamentoField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String stockK = stockField.getText().trim();
        String unitarioO = precioUnitarioField.getText().trim();
        LocalDate fecha = fechaVencimientoPicker.getValue();
        String codProveedor = codigoProveedorField.getText().trim();
        
        if (nombres.isEmpty() || descripcion.isEmpty() || stockK.isEmpty() || unitarioO.isEmpty() || fecha == null || codProveedor.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        int stock;
        double unitario;
        int proveedor;
        try {
            stock = Integer.parseInt(stockK);
            unitario = Double.parseDouble(unitarioO);
            proveedor = Integer.parseInt(codProveedor);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los valores deben ser números enteros.");
            return;
        }
        
        // Verificación de llaves foráneas
        if (!existeProveedor(proveedor)) {
            mostrarVentanaError("El código de proveedor ingresado no existe.");
            return;
        }
        
        Medicamentos medicamento = new Medicamentos();
        medicamento.setNombreMedicamento(nombres);
        medicamento.setDescripcion(descripcion);
        medicamento.setStock(stock);
        medicamento.setPrecioUnitario(unitario);
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaVencimientoPicker.getValue());
        medicamento.setFechaVencimiento(fechaSQL);
        medicamento.setCodigoProveedor(proveedor);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarMedicamento(?, ?, ?, ?, ?, ?) }");
            stmt.setString(1, medicamento.getNombreMedicamento());
            stmt.setString(2, medicamento.getDescripcion());
            stmt.setInt(3, medicamento.getStock());
            stmt.setDouble(4, medicamento.getPrecioUnitario());
            stmt.setDate(5, medicamento.getFechaVencimiento());
            stmt.setInt(6, medicamento.getCodigoProveedor());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar medicamento: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarMedicamento(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoMedicamentoField.getText().trim());
            String nombres = nombreMedicamentoField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            String stockK = stockField.getText().trim();
            String unitarioO = precioUnitarioField.getText().trim();
            LocalDate fecha = fechaVencimientoPicker.getValue();
            String codProveedor = codigoProveedorField.getText().trim();
            
            if (nombres.isEmpty() || descripcion.isEmpty() || stockK.isEmpty() || unitarioO.isEmpty() || fecha == null || codProveedor.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }
            
            int stock;
            double unitario;
            int proveedor;
            try {
                stock = Integer.parseInt(stockK);
                unitario = Double.parseDouble(unitarioO);
                proveedor = Integer.parseInt(codProveedor);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los valores deben ser números enteros.");
                return;
            }
            
            // Verificación de llaves foráneas
            if (!existeProveedor(proveedor)) {
                mostrarVentanaError("El código de proveedor ingresado no existe.");
                return;
            }
            
            Medicamentos medicamento = new Medicamentos();
            medicamento.setCodigoMedicamento(codigo);
            medicamento.setNombreMedicamento(nombres);
            medicamento.setDescripcion(descripcion);
            medicamento.setStock(stock);
            medicamento.setPrecioUnitario(unitario);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaVencimientoPicker.getValue());
            medicamento.setFechaVencimiento(fechaSQL);
            medicamento.setCodigoProveedor(proveedor);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarMedicamento(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, medicamento.getCodigoMedicamento());
            stmt.setString(2, medicamento.getNombreMedicamento());
            stmt.setString(3, medicamento.getDescripcion());
            stmt.setInt(4, medicamento.getStock());
            stmt.setDouble(5, medicamento.getPrecioUnitario());
            stmt.setDate(6, medicamento.getFechaVencimiento());
            stmt.setInt(7, medicamento.getCodigoProveedor());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Medicamento actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código del medicamento debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar medicamento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarMedicamento(ActionEvent event) {
        reproducirSonidoClick();
        Medicamentos medicamentoSeleccionado = TablaMedicamento.getSelectionModel().getSelectedItem();

        if (medicamentoSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarMedicamento(?) }");
                stmt.setInt(1, medicamentoSeleccionado.getCodigoMedicamento());
                stmt.execute();

                System.out.println("Medicamento eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Este medicamento está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar medicamento: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar el medicamento.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un medicamento de la tabla para eliminar.");
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