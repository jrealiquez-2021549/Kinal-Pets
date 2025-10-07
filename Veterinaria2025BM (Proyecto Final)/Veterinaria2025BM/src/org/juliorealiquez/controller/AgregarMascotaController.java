
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
import org.juliorealiquez.models.Mascotas;
import org.juliorealiquez.system.Principal;

public class AgregarMascotaController implements Initializable{
    @FXML private Button btnVolver;
    @FXML private Button btnAgregarCliente;
    @FXML private Button btnAgregarConsulta;
    @FXML private Button btnAgregarCita;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Mascotas> TablaMascota;
    private ObservableList<Mascotas> listarMascotas;
    @FXML
    private TableColumn<Mascotas, Integer> codigoMascota;
    @FXML
    private TableColumn<Mascotas, String> NombreMascota;
    @FXML
    private TableColumn<Mascotas, String> Especie;
    @FXML
    private TableColumn<Mascotas, String> Raza;
    @FXML
    private TableColumn<Mascotas, String> Sexo;
    @FXML
    private TableColumn<Mascotas, java.sql.Date> FechaNacimiento;
    @FXML
    private TableColumn<Mascotas, String> Color;
    @FXML
    private TableColumn<Mascotas, String> PesoActualKg;
    @FXML
    private TableColumn<Mascotas, Integer> CodigoCliente;
    
    // Objetos de texto
    @FXML
    private TextField codigoMascotaField;
    @FXML
    private TextField nombreMascotaField;
    @FXML
    private TextField especieField;
    @FXML
    private TextField razaField;
    @FXML
    private TextField sexoField;
    @FXML
    private DatePicker fechaNacimientoPicker;
    @FXML
    private TextField colorField;
    @FXML
    private TextField pesoActualKgField;
    @FXML
    private TextField codigoClienteField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarCliente.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarConsulta.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarCita.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreMascotaField, 20);
        limitarTextField(especieField, 25);
        limitarTextField(razaField, 30);
        limitarTextField(sexoField, 6);
        limitarTextField(colorField, 20);
        limitarTextField(pesoActualKgField, 7);
        
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
        TablaMascota.setItems(getMascotas());
        
        codigoMascota.setCellValueFactory(new PropertyValueFactory<>("codigoMascota"));
        NombreMascota.setCellValueFactory(new PropertyValueFactory<>("nombreMascota"));
        Especie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        Raza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        Sexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        FechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        Color.setCellValueFactory(new PropertyValueFactory<>("color"));
        PesoActualKg.setCellValueFactory(new PropertyValueFactory<>("pesoActualKg"));
        CodigoCliente.setCellValueFactory(new PropertyValueFactory<>("codigoCliente"));
        
        FechaNacimiento.setCellFactory(column -> new TableCell<Mascotas, java.sql.Date>() {
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
        listarMascotas = FXCollections.observableArrayList();
        
        TablaMascota.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoMascotaField.setText(String.valueOf(newSelection.getCodigoMascota()));
                nombreMascotaField.setText(newSelection.getNombreMascota());
                especieField.setText(newSelection.getEspecie());
                razaField.setText(newSelection.getRaza());
                sexoField.setText(newSelection.getSexo());
                fechaNacimientoPicker.setValue(newSelection.getFechaNacimiento().toLocalDate());
                colorField.setText(newSelection.getColor());
                pesoActualKgField.setText(String.valueOf(newSelection.getPesoActualKg()));
                codigoClienteField.setText(String.valueOf(newSelection.getCodigoCliente()));
            } else {
                codigoMascotaField.clear();
                nombreMascotaField.clear();
                especieField.clear();
                razaField.clear();
                sexoField.clear();
                fechaNacimientoPicker.setValue(null);
                colorField.clear();
                pesoActualKgField.clear();
                codigoClienteField.clear();
            }
        });
    }
    
    public ObservableList<Mascotas> buscarClientePorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Mascotas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarMascota(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Mascotas(
                        rs.getInt("codigoMascota"),
                        rs.getString("nombreMascota"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        rs.getString("sexo"),
                        rs.getDate("fechaNacimiento"),
                        rs.getString("color"),
                        rs.getDouble("pesoActualKg"),
                        rs.getInt("codigoCliente")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar mascota: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Mascotas> getMascotas() {
        ObservableList<Mascotas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarMascotas() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Mascotas(
                        rs.getInt("codigoMascota"),
                        rs.getString("nombreMascota"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        rs.getString("sexo"),
                        rs.getDate("fechaNacimiento"),
                        rs.getString("color"),
                        rs.getDouble("pesoActualKg"),
                        rs.getInt("codigoCliente")
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
        
        codigoMascotaField.clear();
        nombreMascotaField.clear();
        especieField.clear();
        razaField.clear();
        sexoField.clear();
        fechaNacimientoPicker.setValue(null);
        colorField.clear();
        pesoActualKgField.clear();
        codigoClienteField.clear();
        TablaMascota.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoMascotaField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de mascota para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Mascotas> resultado = buscarClientePorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna mascota con ese código.");
            } else {
                TablaMascota.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarMascota(ActionEvent event) {
        reproducirSonidoClick();
        
        String nombres = nombreMascotaField.getText().trim();
        String especies = especieField.getText().trim();
        String razas = razaField.getText().trim();
        String sexo = sexoField.getText().trim();
        LocalDate fechaNacimiento = fechaNacimientoPicker.getValue();
        String colores = colorField.getText().trim();
        String pesoA = pesoActualKgField.getText().trim();
        String codClientes = codigoClienteField.getText().trim();
        
        if (nombres.isEmpty() || especies.isEmpty() || razas.isEmpty() || sexo.isEmpty() || fechaNacimiento == null || colores.isEmpty() || pesoA.isEmpty() || codClientes.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        double peso;
        int clientes;
        try {
            peso = Double.parseDouble(pesoA);
            clientes = Integer.parseInt(codClientes);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los códigos deben ser números enteros.");
            return;
        }
        
        // Verificación de llaves foráneas
        if (!existeCliente(clientes)) {
            mostrarVentanaError("El código de cliente ingresado no existe.");
            return;
        }
        
        Mascotas mascota = new Mascotas();
        mascota.setNombreMascota(nombres);
        mascota.setEspecie(especies);
        mascota.setRaza(razas);
        mascota.setSexo(sexo);
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaNacimientoPicker.getValue());
        mascota.setFechaNacimiento(fechaSQL);
        mascota.setColor(colores);
        mascota.setPesoActualKg(peso);
        mascota.setCodigoCliente(clientes);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarMascota(?, ?, ?, ?, ?, ?, ?, ?) }");
            stmt.setString(1, mascota.getNombreMascota());
            stmt.setString(2, mascota.getEspecie());
            stmt.setString(3, mascota.getRaza());
            stmt.setString(4, mascota.getSexo());
            stmt.setDate(5, mascota.getFechaNacimiento());
            stmt.setString(6, mascota.getColor());
            stmt.setDouble(7, mascota.getPesoActualKg());
            stmt.setInt(8, mascota.getCodigoCliente());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar mascota: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarMascota(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoMascotaField.getText().trim());
            String nombres = nombreMascotaField.getText().trim();
            String especies = especieField.getText().trim();
            String razas = razaField.getText().trim();
            String sexo = sexoField.getText().trim();
            LocalDate fechaNacimiento = fechaNacimientoPicker.getValue();
            String colores = colorField.getText().trim();
            String pesoA = pesoActualKgField.getText().trim();
            String codClientes = codigoClienteField.getText().trim();
            
            if (nombres.isEmpty() || especies.isEmpty() || razas.isEmpty() || sexo.isEmpty() || fechaNacimiento == null || colores.isEmpty() || pesoA.isEmpty() || codClientes.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }
            
            double peso;
            int clientes;
            try {
                peso = Double.parseDouble(pesoA);
                clientes = Integer.parseInt(codClientes);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los códigos deben ser números enteros.");
                return;
            }
            
            // Verificación de llaves foráneas
            if (!existeCliente(clientes)) {
                mostrarVentanaError("El código de cliente ingresado no existe.");
                return;
            }
            
            Mascotas mascota = new Mascotas();
            mascota.setCodigoMascota(codigo);
            mascota.setNombreMascota(nombres);
            mascota.setEspecie(especies);
            mascota.setRaza(razas);
            mascota.setSexo(sexo);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaNacimientoPicker.getValue());
            mascota.setFechaNacimiento(fechaSQL);
            mascota.setColor(colores);
            mascota.setPesoActualKg(peso);
            mascota.setCodigoCliente(clientes);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarMascota(?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, mascota.getCodigoMascota());
            stmt.setString(2, mascota.getNombreMascota());
            stmt.setString(3, mascota.getEspecie());
            stmt.setString(4, mascota.getRaza());
            stmt.setString(5, mascota.getSexo());
            stmt.setDate(6, mascota.getFechaNacimiento());
            stmt.setString(7, mascota.getColor());
            stmt.setDouble(8, mascota.getPesoActualKg());
            stmt.setInt(9, mascota.getCodigoCliente());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Mascota actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la mascota debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar mascota: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarMascota(ActionEvent event) {
        reproducirSonidoClick();
        Mascotas mascotaSeleccionado = TablaMascota.getSelectionModel().getSelectedItem();

        if (mascotaSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarMascota(?) }");
                stmt.setInt(1, mascotaSeleccionado.getCodigoMascota());
                stmt.execute();

                System.out.println("Mascota eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta mascota está siendo utilizado en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar mascota: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la mascota.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una mascota de la tabla para eliminar.");
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
}
