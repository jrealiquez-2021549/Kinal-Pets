
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import org.juliorealiquez.models.Recetas;
import org.juliorealiquez.system.Principal;

public class AgregarRecetaController implements Initializable{
    @FXML private Button btnAgregarVacuna;
    @FXML private Button btnAgregarMedicina;
    @FXML private Button btnAgregarVacunacion;
    @FXML private Button btnAgregarTratamiento;
    @FXML private Button btnVolver;
    @FXML private Button btnMusica;
    
    // Objetos de la tabla
    @FXML
    private TableView<Recetas> TablaReceta;
    private ObservableList<Recetas> listarRecetas;
    @FXML
    private TableColumn<Recetas, Integer> codigoReceta;
    @FXML
    private TableColumn<Recetas, String> Dosis;
    @FXML
    private TableColumn<Recetas, String> Frecuencia;
    @FXML
    private TableColumn<Recetas, Integer> DuracionDias;
    @FXML
    private TableColumn<Recetas, String> Indicaciones;
    @FXML
    private TableColumn<Recetas, Integer> CodigoConsulta;
    @FXML
    private TableColumn<Recetas, Integer> CodigoMedicamento;
    
    // Objetos de texto
    @FXML
    private TextField codigoRecetaField;
    @FXML
    private TextField dosisField;
    @FXML
    private TextField frecuenciaField;
    @FXML
    private TextField duracionDiasField;
    @FXML
    private TextField indicacionesField;
    @FXML
    private TextField codigoConsultaField;
    @FXML
    private TextField codigoMedicamentoField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAgregarVacuna.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarMedicina.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarVacunacion.setOnMouseEntered(e -> reproducirSonidoHover());
        btnAgregarTratamiento.setOnMouseEntered(e -> reproducirSonidoHover());
        btnVolver.setOnMouseEntered(e -> reproducirSonidoHover());
        btnMusica.setOnMouseEntered(e -> reproducirSonidoHover());
        
        limitarTextField(dosisField, 50);
        limitarTextField(frecuenciaField, 50);
        limitarTextField(indicacionesField, 200);
        
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
        TablaReceta.setItems(getRecetas());
        
        codigoReceta.setCellValueFactory(new PropertyValueFactory<>("codigoReceta"));
        Dosis.setCellValueFactory(new PropertyValueFactory<>("dosis"));
        Frecuencia.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        DuracionDias.setCellValueFactory(new PropertyValueFactory<>("duracionDias"));
        Indicaciones.setCellValueFactory(new PropertyValueFactory<>("indicaciones"));
        CodigoConsulta.setCellValueFactory(new PropertyValueFactory<>("codigoConsulta"));
        CodigoMedicamento.setCellValueFactory(new PropertyValueFactory<>("codigoMedicamento"));
    }
    
    public void MostrarLosDatos() {
        listarRecetas = FXCollections.observableArrayList();
        
        TablaReceta.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                codigoRecetaField.setText(String.valueOf(newSelection.getCodigoReceta()));
                dosisField.setText(newSelection.getDosis());
                frecuenciaField.setText(newSelection.getFrecuencia());
                duracionDiasField.setText(String.valueOf(newSelection.getDuracionDias()));
                indicacionesField.setText(newSelection.getIndicaciones());
                codigoConsultaField.setText(String.valueOf(newSelection.getCodigoConsulta()));
                codigoMedicamentoField.setText(String.valueOf(newSelection.getCodigoMedicamento()));
            } else {
                codigoRecetaField.clear();
                dosisField.clear();
                frecuenciaField.clear();
                duracionDiasField.clear();
                indicacionesField.clear();
                codigoConsultaField.clear();
                codigoMedicamentoField.clear();
            }
        });
    }
    
    public ObservableList<Recetas> buscarRecetaPorCodigo(int codigo) {
        reproducirSonidoClick();
        
        ObservableList<Recetas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarReceta(?) }");
            stmt.setInt(1, codigo); // Aquí mandas el código
            ResultSet rs = stmt.executeQuery();
 
            while (rs.next()) {
                lista.add(new Recetas(
                        rs.getInt("codigoReceta"),
                        rs.getString("dosis"),
                        rs.getString("frecuencia"),
                        rs.getInt("duracionDias"),
                        rs.getString("indicaciones"),
                        rs.getInt("codigoConsulta"),
                        rs.getInt("codigoMedicamento")
                    )
                );
            }
 
        } catch (SQLException e) {
            System.err.println("Error al buscar receta: " + e.getMessage());
        }
        return lista;
    }
    
    public ObservableList<Recetas> getRecetas() {
        ObservableList<Recetas> lista = FXCollections.observableArrayList();
 
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_ListarRecetas() }");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Recetas(
                        rs.getInt("codigoReceta"),
                        rs.getString("dosis"),
                        rs.getString("frecuencia"),
                        rs.getInt("duracionDias"),
                        rs.getString("indicaciones"),
                        rs.getInt("codigoConsulta"),
                        rs.getInt("codigoMedicamento")
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
        
        codigoRecetaField.clear();
        dosisField.clear();
        frecuenciaField.clear();
        duracionDiasField.clear();
        indicacionesField.clear();
        codigoConsultaField.clear();
        codigoMedicamentoField.clear();
        TablaReceta.getSelectionModel().clearSelection();
        cargarDatos();
    }
    
    @FXML
    void handleButtonPressBuscar(ActionEvent event) {
        reproducirSonidoClick();

        String codigoTexto = codigoRecetaField.getText().trim();

        if (codigoTexto.isEmpty()) {
            System.out.println("Por favor, ingrese un código de receta para buscar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(codigoTexto);
            ObservableList<Recetas> resultado = buscarRecetaPorCodigo(codigo);

            if (resultado.isEmpty()) {
                System.out.println("No se encontró ningúna receta con ese código.");
            } else {
                TablaReceta.setItems(resultado);
            }

        } catch (NumberFormatException e) {
            System.err.println("Error: El código debe ser un número entero.");
        }
    }
    
    @FXML
    void handleButtonPressAgregarReceta(ActionEvent event) {
        reproducirSonidoClick();
        
        String dosis = dosisField.getText().trim();
        String frecuencia = frecuenciaField.getText().trim();
        String duracionD = duracionDiasField.getText().trim();
        String indicacion = indicacionesField.getText().trim();
        String codConsulta = codigoConsultaField.getText().trim();
        String codMedicamento = codigoMedicamentoField.getText().trim();
        
        if (dosis.isEmpty() || frecuencia.isEmpty() || duracionD.isEmpty() || indicacion.isEmpty() || codConsulta.isEmpty() || codMedicamento.isEmpty()) {
            mostrarVentanaError("Todos los campos deben estar llenos.");
            return;
        }
        
        int duracion;
        int consulta;
        int medicamento;
        try {
            duracion = Integer.parseInt(duracionD);
            consulta = Integer.parseInt(codConsulta);
            medicamento = Integer.parseInt(codMedicamento);
        } catch (NumberFormatException e) {
            mostrarVentanaError("Los valores deben ser números enteros.");
            return;
        }
        
        // Verificación de llaves foráneas
        if (!existeConsulta(consulta)) {
            mostrarVentanaError("El código de consulta ingresado no existe.");
            return;
        }

        if (!existeMedicamento(medicamento)) {
            mostrarVentanaError("El código de medicamento ingresado no existe.");
            return;
        }
        
        Recetas receta = new Recetas();
        receta.setDosis(dosis);
        receta.setFrecuencia(frecuencia);
        receta.setDuracionDias(duracion);
        receta.setIndicaciones(indicacion);
        receta.setCodigoConsulta(consulta);
        receta.setCodigoMedicamento(medicamento);
        
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_AgregarReceta(?, ?, ?, ?, ?, ?) }");
            stmt.setString(1, receta.getDosis());
            stmt.setString(2, receta.getFrecuencia());
            stmt.setInt(3, receta.getDuracionDias());
            stmt.setString(4, receta.getIndicaciones());
            stmt.setInt(5, receta.getCodigoConsulta());
            stmt.setInt(6, receta.getCodigoMedicamento());
            stmt.execute();
            cargarDatos();
            
        } catch (SQLDataException e) {
            // Error relacionado con truncamiento de datos
            System.err.println("Error de truncamiento de datos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al registrar receta: " + e.getMessage());
        }
    }
    
    @FXML
    void handleButtonPressEditarReceta(ActionEvent event) {
        reproducirSonidoClick();
        
        try {
            int codigo = Integer.parseInt(codigoRecetaField.getText().trim());
            String dosis = dosisField.getText().trim();
            String frecuencia = frecuenciaField.getText().trim();
            String duracionD = duracionDiasField.getText().trim();
            String indicacion = indicacionesField.getText().trim();
            String codConsulta = codigoConsultaField.getText().trim();
            String codMedicamento = codigoMedicamentoField.getText().trim();
            
            if (dosis.isEmpty() || frecuencia.isEmpty() || duracionD.isEmpty() || indicacion.isEmpty() || codConsulta.isEmpty() || codMedicamento.isEmpty()) {
                mostrarVentanaError("Todos los campos deben estar llenos.");
                return;
            }
            
            int duracion;
            int consulta;
            int medicamento;
            try {
                duracion = Integer.parseInt(duracionD);
                consulta = Integer.parseInt(codConsulta);
                medicamento = Integer.parseInt(codMedicamento);
            } catch (NumberFormatException e) {
                mostrarVentanaError("Los valores deben ser números enteros.");
                return;
            }
            
            // Verificación de llaves foráneas
            if (!existeConsulta(consulta)) {
                mostrarVentanaError("El código de consulta ingresado no existe.");
                return;
            }

            if (!existeMedicamento(medicamento)) {
                mostrarVentanaError("El código de medicamento ingresado no existe.");
                return;
            }

            Recetas receta = new Recetas();
            receta.setCodigoReceta(codigo);
            receta.setDosis(dosis);
            receta.setFrecuencia(frecuencia);
            receta.setDuracionDias(duracion);
            receta.setIndicaciones(indicacion);
            receta.setCodigoConsulta(consulta);
            receta.setCodigoMedicamento(medicamento);
            
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_EditarReceta(?, ?, ?, ?, ?, ?, ?) }");
            stmt.setInt(1, receta.getCodigoReceta());
            stmt.setString(2, receta.getDosis());
            stmt.setString(3, receta.getFrecuencia());
            stmt.setInt(4, receta.getDuracionDias());
            stmt.setString(5, receta.getIndicaciones());
            stmt.setInt(6, receta.getCodigoConsulta());
            stmt.setInt(7, receta.getCodigoMedicamento());
            
            stmt.execute();
            cargarDatos();
            System.out.println("Receta actualizado exitosamente.");
            
        } catch (NumberFormatException e) {
            System.err.println("Error: El código de la receta debe ser un número.");
        } catch (Exception e) {
            System.err.println("Error al editar receta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void handleButtonPressEliminarReceta(ActionEvent event) {
        reproducirSonidoClick();
        Recetas recetaSeleccionado = TablaReceta.getSelectionModel().getSelectedItem();

        if (recetaSeleccionado != null) {
            try {
                Connection conn = Conexion.getInstancia().getConexion();
                CallableStatement stmt = conn.prepareCall("{ call sp_EliminarReceta(?) }");
                stmt.setInt(1, recetaSeleccionado.getCodigoReceta());
                stmt.execute();

                System.out.println("Receta eliminado exitosamente.");
                cargarDatos();

            } catch (SQLException e) {
                // Validamos si la excepción es por restricción de integridad
                if (e.getMessage().contains("foreign key") || e.getMessage().contains("clave foránea")) {
                    mostrarAlerta("No se puede eliminar", 
                        "Esta receta está siendo utilizada en otra entidad.\nAsegúrate de eliminar primero sus dependencias.");
                } else {
                    System.err.println("Error al eliminar receta: " + e.getMessage());
                    mostrarAlerta("Error inesperado", "Ocurrió un error al eliminar la receta.");
                }
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione una receta de la tabla para eliminar.");
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
    
    private boolean existeMedicamento(int codigo) {
        try {
            Connection conn = Conexion.getInstancia().getConexion();
            CallableStatement stmt = conn.prepareCall("{ call sp_BuscarMedicamento(?) }");
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
