
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
import org.juliorealiquez.models.Vacunas;
import org.juliorealiquez.system.Principal;

public class AgregarVacunaController implements Initializable{
    @FXML private Button btnAgregarMedicina;
    @FXML private Button btnAgregarVacunacion;
    @FXML private Button btnAgregarTratamiento;
    @FXML private Button btnAgregarReceta;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Vacunas> TablaVacuna;
    private ObservableList<Vacunas> listarVacunas;
    @FXML
    private TableColumn<Vacunas, Integer> codigoVacuna;
    @FXML
    private TableColumn<Vacunas, String> NombreVacuna;
    @FXML
    private TableColumn<Vacunas, String> Descripcion;
    @FXML
    private TableColumn<Vacunas, String> Dosis;
    @FXML
    private TableColumn<Vacunas, Integer> FrecuenciaMeses;
    
    // Objetos de texto
    @FXML
    private TextField codigoVacunaField;
    @FXML
    private TextField nombreVacunaField;
    @FXML
    private TextField descripcionField;
    @FXML
    private TextField dosisField;
    @FXML
    private TextField frecuenciaMesesField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAgregarMedicina.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarVacunacion.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarTratamiento.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarReceta.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(nombreVacunaField, 50);
        limitarTextField(descripcionField, 150);
        limitarTextField(dosisField, 50);
        
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
        TablaVacuna.setItems(getVacunas());
        
        codigoVacuna.setCellValueFactory(new PropertyValueFactory<>("codigoVacuna"));
        NombreVacuna.setCellValueFactory(new PropertyValueFactory<>("nombreVacuna"));
        Descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        Dosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        FrecuenciaMeses.setCellValueFactory(new PropertyValueFactory<>("frecuenciaMeses"));
    }
    
    public void MostrarLosDatos() {
        listarVacunas = FXCollections.observableArrayList();
        
        TablaVacuna.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoVacunaField.setText(String.valueOf(newSelection.getCodigoVacuna()));
                nombreVacunaField.setText(newSelection.getNombreVacuna());
                descripcionField.setText(newSelection.getDescripcion());
                dosisField.setText(newSelection.getDosis());
                frecuenciaMesesField.setText(String.valueOf(newSelection.getFrecuenciaMeses()));
            } else {
                codigoVacunaField.clear();
                nombreVacunaField.clear();
                descripcionField.clear();
                dosisField.clear();
                frecuenciaMesesField.clear();
            }
        });
    }
    
    public ObservableList<Vacunas> buscarVacunaPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Vacunas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarVacuna(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Vacunas(
                        rs.getInt("codigoVacuna"),
                        rs.getString("nombreVacuna"),
                        rs.getString("descripcion"),
                        rs.getString("dosis"),
                        rs.getInt("frecuenciaMeses")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar vacuna: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Vacunas> getVacunas() {
        ObservableList<Vacunas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarVacunas() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Vacunas(
                        rs.getInt("codigoVacuna"),
                        rs.getString("nombreVacuna"),
                        rs.getString("descripcion"),
                        rs.getString("dosis"),
                        rs.getInt("frecuenciaMeses")
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
        
        codigoVacunaField.clear();
        nombreVacunaField.clear();
        descripcionField.clear();
        dosisField.clear();
        frecuenciaMesesField.clear();
        TablaVacuna.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoVacunaField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de vacuna para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Vacunas> resultado = buscarVacunaPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna vacuna con ese código.");
            } else {
                TablaVacuna.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarVacuna(ActionEvent event) {
        reproducirSonidoClick();

        String nombres = nombreVacunaField.getText().trim();
        String descripcion = descripcionField.getText().trim();
        String dosis = dosisField.getText().trim();
        String frecuenciaM = frecuenciaMesesField.getText().trim();

        if (nombres.isEmpty() || descripcion.isEmpty() || dosis.isEmpty() || frecuenciaM.isEmpty()) {
            System.out.println("Error: Todos los campos deben estar llenos.");
            return;
        }

        int frecuencia;
        try {
            frecuencia = Integer.parseInt(frecuenciaM);
        } catch (NumberFormatException e) {
            System.out.println("Error: La frecuencia debe ser un número entero válido.");
            return;
        }

        Vacunas vacuna = new Vacunas();
        vacuna.setNombreVacuna(nombres);
        vacuna.setDescripcion(descripcion);
        vacuna.setDosis(dosis);
        vacuna.setFrecuenciaMeses(frecuencia);

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarVacuna(?, ?, ?, ?) }");
            stmt.setString(1, vacuna.getNombreVacuna());
            stmt.setString(2, vacuna.getDescripcion());
            stmt.setString(3, vacuna.getDosis());
            stmt.setInt(4, vacuna.getFrecuenciaMeses());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar vacuna: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarVacuna(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoVacunaField.getText().trim());
            String nombres = nombreVacunaField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            String dosis = dosisField.getText().trim();
            String frecuenciaStr = frecuenciaMesesField.getText().trim();
            
            if (nombres.isEmpty() || descripcion.isEmpty() || dosis.isEmpty() || frecuenciaStr.isEmpty()) {
                System.out.println("Error: Todos los campos deben estar llenos.");
                return;
            }
            
            int frecuencia;
            try {
                frecuencia = Integer.parseInt(frecuenciaStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: La frecuencia debe ser un número entero válido.");
                return;
            }
            
            Vacunas vacuna = new Vacunas();
            vacuna.setCodigoVacuna(codigo);
            vacuna.setNombreVacuna(nombres);
            vacuna.setDescripcion(descripcion);
            vacuna.setDosis(dosis);
            vacuna.setFrecuenciaMeses(frecuencia);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarVacuna(?, ?, ?, ?, ?) }");
            stmt.setInt(1, vacuna.getCodigoVacuna());
            stmt.setString(2, vacuna.getNombreVacuna());
            stmt.setString(3, vacuna.getDescripcion());
            stmt.setString(4, vacuna.getDosis());
            stmt.setInt(5, vacuna.getFrecuenciaMeses());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Vacuna actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la vacuna debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar vacuna: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarVacuna(ActionEvent event) {
        reproducirSonidoClick();
        Vacunas vacunaSeleccionado = TablaVacuna.getSelectionModel().getSelectedItem();

        if (vacunaSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarVacuna(?) }");
                stmt.setInt(1, vacunaSeleccionado.getCodigoVacuna());
                stmt.execute();

                System.out.println("Vacuna eliminada exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta vacuna está siendo utilizada en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar vacuna: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la vacuna.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una vacuna de la tabla para eliminar.");
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
}