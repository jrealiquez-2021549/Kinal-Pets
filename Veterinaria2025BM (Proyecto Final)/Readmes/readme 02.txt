Nombre: Veterinaria2025BM

Cómo Funciona:
La clase principal de la aplicación es Principal.java, ubicada en src/org/juliorealiquez/system/Principal.java. Representa el punto de entrada del programa. Cuando se ejecuta la aplicación, la Java Virtual Machine (JVM) carga esta clase y ejecuta el método main(String[] args). Dentro de este método, se invoca Application.launch(args), lo cual inicia la aplicación JavaFX, preparando el entorno gráfico, estableciendo el ciclo de vida de la aplicación y abriendo la ventana principal.

La clase Principal extiende javafx.application.Application e implementa el método start(Stage primaryStage), donde se configura y carga inicialmente la interfaz gráfica. En este método se realiza la carga del archivo InicioSesionView.fxml mediante FXMLLoader.load(), que contiene la estructura de la ventana de inicio de sesión. Posteriormente, se crea una escena con ese contenido y se asigna al primaryStage. También se establece el título de la ventana y se muestra con primaryStage.show(). Este flujo garantiza que al iniciar la aplicación, el usuario vea primero la ventana de login para autenticarse.

Vista y Controlador de Login

La vista se define en el archivo InicioSesionView.fxml, el cual estructura la interfaz gráfica con campos de texto para el nombre de usuario y la contraseña (TextField y PasswordField), un botón para ingresar (Button), y etiquetas o mensajes para mostrar errores. El uso de este archivo XML permite separar la presentación de la lógica de la aplicación.

El controlador asociado es InicioSesionController.java, el cual vincula la vista con la lógica de negocio. Dentro de su método initialize(), se configuran aspectos iniciales como limpiar campos, establecer el foco en los elementos apropiados y asociar eventos al botón de login.

Un método clave en el controlador es handleLoginButtonAction(ActionEvent event), que se ejecuta al hacer clic en el botón "Ingresar". Este método recoge los valores ingresados en los campos de usuario y contraseña, y verifica que no estén vacíos. Si algún campo está vacío, se muestra un diálogo de alerta con el mensaje "Usuario y contraseña son requeridos", utilizando la clase Alert de JavaFX.

En caso de que los campos estén completos, el controlador llama a ValidadorUsuario.validarUsuario(String usuario, String contraseña) para comprobar las credenciales en la base de datos. Si la validación falla, se muestra el mensaje "Credenciales incorrectas" y se limpia el campo de la contraseña para permitir un nuevo intento. Si la validación es exitosa, se muestra un mensaje de éxito ("Login exitoso!"), se carga la ventana principal (MenuPrincipalView.fxml) mediante un nuevo FXMLLoader y se cierra la ventana de login para liberar recursos y evitar duplicados.

Validación de Usuario

El método ValidadorUsuario.validarUsuario se conecta a la base de datos utilizando la clase Conexion.java. Realiza una consulta SQL preparada para buscar un registro en la tabla usuarios que coincida con el nombre de usuario y la contraseña proporcionados. Si encuentra una coincidencia, devuelve true; en caso contrario, false. Además, maneja adecuadamente las excepciones SQL y asegura el cierre de recursos para evitar fugas.

Menú Principal: MenuPrincipalView.fxml y MenuPrincipalController.java

La vista del menú principal está definida en el archivo MenuPrincipalView.fxml. Esta interfaz gráfica aparece luego de un inicio de sesión exitoso y contiene botones u opciones para acceder a los diferentes módulos del sistema veterinario. Entre los módulos disponibles se incluyen:

- Gestión de clientes
- Gestión de mascotas
- Citas y consultas
- Empleados y veterinarios
- Medicamentos, tratamientos y recetas
- Facturación
- Compras y proveedores
- Informes y estadísticas

Además, la vista incluye un botón de "Cerrar sesión", el cual permite finalizar la sesión actual del usuario.

Controlador: MenuPrincipalController.java

El controlador MenuPrincipalController se encarga de manejar la lógica detrás de la ventana principal y la navegación entre los diferentes módulos. Entre sus atributos principales se encuentran las referencias a cada botón definido en la vista, utilizando anotaciones @FXML, por ejemplo:

- btnClientes
- btnMascotas
- btnCitas
- btnFacturas
- btnCerrarSesion

Uno de los métodos fundamentales del controlador es initialize(), el cual se ejecuta automáticamente al cargar el controlador. Este método puede usarse para establecer configuraciones iniciales, como la asignación de permisos según el tipo de usuario o ajustes visuales de la interfaz.

Para cada botón en el menú, se define un método específico que maneja el evento de clic del usuario, tales como:

- handleClientes(ActionEvent event)
- handleMascotas(ActionEvent event)
- handleCitas(ActionEvent event)
- handleFacturas(ActionEvent event)

Estos métodos permiten abrir la ventana correspondiente al módulo seleccionado por el usuario.

Cada uno de los métodos que maneja eventos de botones utiliza FXMLLoader para cargar la vista correspondiente, por ejemplo, ClientesView.fxml. Posteriormente, se crea una nueva escena y una nueva ventana (Stage) para mostrar el módulo deseado. Esta ventana se muestra al usuario de forma independiente, y opcionalmente, se puede ocultar o deshabilitar la ventana del menú principal para evitar confusión o accesos múltiples al mismo tiempo.

Manejo del Cierre de Sesión
El botón de "Cerrar sesión" ejecuta un método que realiza las siguientes acciones:

@FXML
private void handleCerrarSesion(ActionEvent event) {
    // Cierra la ventana actual (menú principal)
    Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
    stage.close();

    // Vuelve a mostrar la ventana de inicio de sesión
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/juliorealiquez/view/InicioSesionView.fxml"));
        Parent root = loader.load();

        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(root));
        loginStage.setTitle("Inicio de Sesión");
        loginStage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Aquí se podría mostrar un diálogo de error si falla la carga
    }
}

Este método obtiene la ventana actual (el menú principal) y la cierra, finalizando la sesión activa. Luego, carga nuevamente la vista de inicio de sesión (InicioSesionView.fxml) y la muestra como una nueva ventana, permitiendo que otro usuario pueda iniciar sesión sin necesidad de reiniciar la aplicación completa.

Validaciones y Configuraciones Adicionales

Si el sistema cuenta con un esquema de roles o permisos (como administrador, veterinario, etc.), dentro del método initialize() se pueden habilitar o deshabilitar botones en función del usuario autenticado. También se pueden mostrar notificaciones o mensajes personalizados, como un saludo con el nombre del usuario que ha iniciado sesión.

Vista: AgregarClienteView.fxml

La vista AgregarClienteView.fxml define la interfaz gráfica para el módulo de gestión de clientes, específicamente para agregar nuevos registros. Esta ventana incluye campos destinados al ingreso de datos personales del cliente, tales como nombre completo, dirección, número de teléfono y correo electrónico.

Además, cuenta con botones para realizar acciones específicas, como Guardar cliente y Cancelar o limpiar formulario, lo que permite al usuario completar o reiniciar el proceso de registro de manera eficiente.

Controlador: AgregarClienteController.java

El controlador asociado, AgregarClienteController.java, es responsable de gestionar la lógica del formulario de cliente. Su propósito es validar las entradas del usuario, interactuar con la base de datos y garantizar una experiencia fluida y segura durante el proceso de registro.

Entre sus atributos principales se incluyen referencias @FXML a los campos de texto (TextField), botones (Button) y etiquetas para mostrar mensajes o alertas. También se utiliza una variable de conexión a la base de datos, comúnmente gestionada a través de la clase Conexion.java.

Métodos Clave del Controlador

1. initialize()
Este método se ejecuta al cargar el controlador. Puede utilizarse para limpiar los campos, preparar validaciones o realizar configuraciones iniciales antes de que el usuario comience a interactuar con la interfaz.

2. handleGuardarCliente(ActionEvent event)
Es el método principal que se ejecuta cuando el usuario hace clic en el botón Guardar. El flujo que sigue este método incluye varias etapas importantes:

- Validación de datos: Se verifica que todos los campos obligatorios estén llenos. Luego, se emplean expresiones regulares para comprobar que el formato del número de teléfono y del correo electrónico sea correcto. En caso de errores, se muestra un mensaje de alerta al usuario y el proceso se detiene.

- Preparación y ejecución de la consulta SQL: Se construye un comando INSERT para agregar los datos del nuevo cliente a la base de datos. Se utiliza un PreparedStatement para prevenir inyecciones SQL y realizar la inserción segura. Si la operación tiene éxito, se muestra un mensaje de confirmación; si ocurre un error (por ejemplo, duplicación o fallo de conexión), se informa al usuario mediante una alerta.

- Limpieza y actualización del formulario: Después de una inserción exitosa, se limpian los campos para permitir un nuevo registro. Opcionalmente, se puede actualizar una lista o tabla de clientes visibles en la aplicación.

3. limpiarFormulario()
Este método se encarga de restablecer todos los campos del formulario, dejándolos en blanco para facilitar un nuevo ingreso de datos.

Ejemplo del Método handleGuardarCliente (Simplificado)

@FXML
private void handleGuardarCliente(ActionEvent event) {
    String nombre = txtNombre.getText().trim();
    String direccion = txtDireccion.getText().trim();
    String telefono = txtTelefono.getText().trim();
    String correo = txtCorreo.getText().trim();

    if(nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
        mostrarAlerta("Error", "Todos los campos son obligatorios");
        return;
    }

    if(!telefono.matches("\\d{8,10}")) {
        mostrarAlerta("Error", "Teléfono inválido");
        return;
    }

    if(!correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
        mostrarAlerta("Error", "Correo electrónico inválido");
        return;
    }

    String sql = "INSERT INTO clientes (nombre, direccion, telefono, correo) VALUES (?, ?, ?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, direccion);
        ps.setString(3, telefono);
        ps.setString(4, correo);

        int filas = ps.executeUpdate();

        if(filas > 0) {
            mostrarAlerta("Éxito", "Cliente registrado correctamente");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el cliente");
        }

    } catch (SQLException e) {
        mostrarAlerta("Error", "Error en la base de datos: " + e.getMessage());
    }
}

Validaciones y Manejo de Errores

El controlador aplica validaciones estrictas antes de intentar guardar los datos. Utiliza expresiones regulares para garantizar que el número de teléfono y el correo electrónico cumplan con los formatos esperados. También gestiona posibles excepciones SQL para evitar cierres inesperados de la aplicación y ofrece mensajes claros al usuario en caso de problemas técnicos.

Vista: AgregarMascotaView.fxml
La vista AgregarMascotaView.fxml define la interfaz gráfica destinada al registro y administración de mascotas dentro del sistema. Esta pantalla incluye campos para ingresar los siguientes datos:

- Nombre de la mascota
- Especie (por ejemplo, perro, gato, etc.)
- Raza
- Edad
- Cliente propietario (generalmente a través de un ComboBox o buscador)

Además, incorpora botones para Guardar, Limpiar formulario y Cancelar, lo que permite gestionar de forma intuitiva el proceso de registro de nuevas mascotas.

Controlador: AgregarMascotaController.java

El controlador AgregarMascotaController administra toda la lógica del formulario de mascotas, incluyendo la validación de los datos, la vinculación con el cliente propietario y la comunicación con la base de datos para realizar el registro.

Atributos principales

El controlador posee referencias @FXML a los campos de texto y selección, una referencia al ComboBox utilizado para seleccionar el cliente propietario, y una variable para manejar la conexión a la base de datos mediante la clase Conexion.java.

Métodos Clave del Controlador

1. initialize()
Este método se ejecuta al cargar la vista y su controlador. Su función principal es cargar desde la base de datos la lista de clientes registrados, la cual se utiliza para poblar el ComboBox de propietarios. Además, permite inicializar validaciones y configuraciones necesarias para la interfaz.

2. handleGuardarMascota(ActionEvent event)
Este método se dispara al hacer clic en el botón Guardar. El flujo de ejecución es el siguiente:

- Obtiene los datos ingresados en los campos del formulario.
- Valida que todos los campos estén completos y tengan formatos válidos (por ejemplo, que la edad sea un número positivo).
- Verifica que el cliente propietario seleccionado sea válido.
- Prepara una sentencia SQL INSERT para registrar la mascota, relacionándola con el cliente por medio de su ID.
- Ejecuta la consulta con PreparedStatement para asegurar una inserción segura y evitar inyecciones SQL.
- Si el registro es exitoso, muestra una alerta de confirmación y limpia el formulario.
- Si ocurre un error, informa al usuario con un mensaje detallado.

limpiarFormulario()
Este método limpia todos los campos del formulario, incluyendo la selección del ComboBox, dejando la interfaz lista para ingresar una nueva mascota.

Ejemplo del Método handleGuardarMascota (Simplificado)

@FXML
private void handleGuardarMascota(ActionEvent event) {
    String nombre = txtNombre.getText().trim();
    String especie = txtEspecie.getText().trim();
    String raza = txtRaza.getText().trim();
    String edadStr = txtEdad.getText().trim();
    Cliente clienteSeleccionado = comboClientes.getValue();

    if(nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edadStr.isEmpty() || clienteSeleccionado == null) {
        mostrarAlerta("Error", "Todos los campos son obligatorios");
        return;
    }

    int edad;
    try {
        edad = Integer.parseInt(edadStr);
        if(edad < 0) throw new NumberFormatException();
    } catch(NumberFormatException e) {
        mostrarAlerta("Error", "Edad inválida");
        return;
    }

    String sql = "INSERT INTO mascotas (nombre, especie, raza, edad, cliente_id) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, especie);
        ps.setString(3, raza);
        ps.setInt(4, edad);
        ps.setInt(5, clienteSeleccionado.getId());

        int filas = ps.executeUpdate();

        if(filas > 0) {
            mostrarAlerta("Éxito", "Mascota registrada correctamente");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo registrar la mascota");
        }

    } catch (SQLException e) {
        mostrarAlerta("Error", "Error en la base de datos: " + e.getMessage());
    }
}

Validaciones y Manejo de Errores

El controlador asegura que todos los campos contengan datos válidos antes de intentar guardar la mascota. Se verifica que la edad sea un número entero positivo, y que un cliente válido esté seleccionado para evitar registros sin propietario. Cualquier error de conexión o ejecución SQL se captura adecuadamente y se notifica al usuario mediante alertas.

Vista: AgregarCitaView.fxml

La vista AgregarCitaView.fxml proporciona la interfaz gráfica para programar nuevas citas veterinarias dentro del sistema. Está diseñada para facilitar el agendamiento preciso y organizado de citas entre clientes, mascotas y veterinarios.

Los campos principales de esta interfaz incluyen:

- Selección de cliente (mediante ComboBox o buscador)
- Selección de mascota asociada (filtrada automáticamente según el cliente seleccionado)
- Selección de veterinario disponible
- Fecha y hora de la cita (con controles de calendario y selector de hora)

También se incluyen botones para Guardar, Cancelar y Limpiar formulario, ofreciendo al usuario una experiencia intuitiva y ordenada.

Controlador: AgregarCitaController.java

El controlador AgregarCitaController es responsable de manejar la lógica para la programación y validación de citas. Administra las relaciones entre clientes, mascotas y veterinarios, así como la validación de disponibilidad horaria para evitar conflictos.

Atributos principales

El controlador incluye referencias @FXML a campos de selección (ComboBox) y controles de fecha y hora. Internamente, maneja listas de clientes, mascotas y veterinarios activos. Además, gestiona la conexión a la base de datos mediante la clase Conexion.java.

Métodos Clave del Controlador

1. initialize()
Este método se ejecuta al cargar la vista. Se encarga de poblar el ComboBox de clientes a partir de la base de datos. Cuando el usuario selecciona un cliente, el controlador actualiza el combo de mascotas con aquellas asociadas a ese cliente. También carga la lista de veterinarios disponibles en ese momento.

Adicionalmente, configura los selectores de fecha y hora para que no se puedan elegir fechas pasadas, y se puedan aplicar validaciones adicionales según reglas del sistema (por ejemplo, horarios hábiles).

2. handleGuardarCita(ActionEvent event)
Este método se ejecuta cuando el usuario presiona el botón Guardar. Su flujo es el siguiente:

- Obtiene los datos seleccionados del formulario (cliente, mascota, veterinario, fecha y hora).
- Verifica que no haya campos vacíos.
- Valida que la fecha y hora elegidas sean válidas (no en el pasado).
- Llama al método validarDisponibilidadVeterinario() para comprobar si el veterinario está libre en ese horario.
- Si el horario está disponible, registra la cita en la base de datos.
- Si hay un conflicto (otra cita agendada en el mismo horario), muestra un mensaje de error.
- Si la cita se registra correctamente, notifica al usuario y limpia el formulario.

3. validarDisponibilidadVeterinario(int veterinarioId, LocalDateTime fechaHora)
Este método consulta la base de datos para verificar si el veterinario tiene alguna otra cita agendada en la misma fecha y hora. Si no hay conflictos, devuelve true; en caso contrario, retorna false.

Código Simplificado de Validación de Disponibilidad

private boolean validarDisponibilidadVeterinario(int veterinarioId, LocalDateTime fechaHora) throws SQLException {
    String sql = "SELECT COUNT(*) FROM citas WHERE veterinario_id = ? AND fecha_hora = ?";
    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, veterinarioId);
        ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            int count = rs.getInt(1);
            return count == 0;
        }
    }
    return false;
}

Este fragmento ilustra cómo el sistema garantiza que no se registren citas superpuestas para un mismo veterinario.

Vista: AgregarConsultaView.fxml

La vista AgregarConsultaView.fxml proporciona la interfaz gráfica necesaria para registrar los detalles clínicos de una consulta veterinaria. Esta interfaz permite vincular la consulta con una cita previamente agendada y recopilar información médica relevante.

Los campos principales incluyen:

- Selección de la cita (relacionada con la mascota y el veterinario asignado)
- Descripción de los síntomas observados
- Diagnóstico realizado por el profesional
- Observaciones adicionales
- Prescripción o vínculo a tratamientos o recetas (opcional)

La vista también ofrece botones para Guardar, Limpiar y Cancelar, permitiendo una gestión completa del formulario.

Controlador: AgregarConsultaController.java

El controlador AgregarConsultaController se encarga de manejar la lógica para crear y registrar una consulta clínica. Esto incluye la validación de los campos, la vinculación con una cita existente y la inserción de los datos en la base de datos del sistema.

Atributos principales

Entre los atributos más relevantes del controlador se encuentran las referencias @FXML a los campos de texto para síntomas, diagnóstico y observaciones, así como una referencia al ComboBox que contiene las citas disponibles. También se gestiona una conexión a la base de datos mediante la clase Conexion.java.

Métodos Clave del Controlador

1. initialize()
Este método se ejecuta al cargar la vista y su controlador. Su propósito principal es llenar el ComboBox con las citas próximas o pendientes disponibles para registrar una consulta. También se inicializan validaciones básicas para asegurar que los campos obligatorios no queden vacíos.

2. handleGuardarConsulta(ActionEvent event)
Este método se activa al presionar el botón Guardar. El flujo de trabajo es el siguiente:

- Obtiene los datos ingresados en el formulario, así como la cita seleccionada.
- Verifica que se haya seleccionado una cita y que los campos de síntomas y diagnóstico no estén vacíos.
- Prepara una sentencia SQL para insertar los datos en la tabla consultas, vinculándolos a la cita seleccionada mediante su ID.
- Ejecuta la consulta con un PreparedStatement para garantizar la seguridad frente a inyecciones SQL.
- Si la inserción es exitosa, muestra un mensaje de confirmación y limpia el formulario.
- En caso de error, muestra una alerta con detalles y no registra la consulta.

Ejemplo del Método handleGuardarConsulta

@FXML
private void handleGuardarConsulta(ActionEvent event) {
    Cita citaSeleccionada = comboCitas.getValue();
    String sintomas = txtSintomas.getText().trim();
    String diagnostico = txtDiagnostico.getText().trim();
    String observaciones = txtObservaciones.getText().trim();

    if(citaSeleccionada == null || sintomas.isEmpty() || diagnostico.isEmpty()) {
        mostrarAlerta("Error", "Debe seleccionar una cita y completar síntomas y diagnóstico");
        return;
    }

    String sql = "INSERT INTO consultas (cita_id, sintomas, diagnostico, observaciones) VALUES (?, ?, ?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, citaSeleccionada.getId());
        ps.setString(2, sintomas);
        ps.setString(3, diagnostico);
        ps.setString(4, observaciones);

        int filas = ps.executeUpdate();

        if(filas > 0) {
            mostrarAlerta("Éxito", "Consulta registrada correctamente");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo registrar la consulta");
        }

    } catch (SQLException e) {
        mostrarAlerta("Error", "Error en la base de datos: " + e.getMessage());
    }
}

Este código ejemplifica cómo el sistema toma la información proporcionada por el usuario y la guarda correctamente en la base de datos, asegurando una asociación válida con una cita ya existente.

Gestión de Medicamentos

La vista AgregarMedicinaView.fxml proporciona un formulario para registrar y administrar los medicamentos disponibles en la veterinaria. Este formulario incluye campos esenciales como el nombre del medicamento, su descripción, la cantidad en stock y la fecha de caducidad. Además, cuenta con botones para guardar la información o cancelar la operación.

El controlador correspondiente, AgregarMedicinaController.java, maneja la lógica de interacción con la interfaz. Posee referencias @FXML a los campos de entrada y se conecta a la base de datos para registrar los medicamentos. Entre sus métodos clave se encuentra initialize(), encargado de preparar la vista al momento de cargarla, lo cual puede incluir la limpieza de campos o el precargado de listas si se necesita.

El método handleGuardarMedicina(ActionEvent event) es el encargado de gestionar el evento de guardar. Este método valida que todos los campos estén completos, que la cantidad ingresada sea un número positivo y que la fecha de caducidad sea válida. Si los datos son correctos, se ejecuta una sentencia SQL preparada para insertar el medicamento en la base de datos. Luego, se muestra un mensaje de éxito o de error, según el resultado, y se limpia el formulario para permitir un nuevo ingreso.

Gestión de Tratamientos

La vista AgregarTratamientoView.fxml permite registrar tratamientos médicos que serán aplicados a las mascotas. El formulario contiene campos como el nombre del tratamiento y una descripción, así como botones para guardar, limpiar o cancelar.

El controlador asociado, AgregarTratamientoController.java, funciona de forma similar al de medicamentos. Valida que los campos obligatorios estén completos y luego inserta el tratamiento en la base de datos mediante una sentencia SQL preparada. También muestra mensajes de confirmación o error y limpia la interfaz luego de una operación exitosa.

Gestión de Recetas

La vista AgregarRecetaView.fxml permite registrar recetas médicas que vinculan consultas con medicamentos y tratamientos. El formulario contiene campos como la selección de una consulta (relacionada con la mascota y su cita), el medicamento prescrito, la dosis, la frecuencia y observaciones adicionales. También cuenta con botones para guardar o cancelar.

El controlador AgregarRecetaController.java gestiona la lógica para registrar una receta. Al iniciar, carga las consultas disponibles para que el usuario pueda seleccionar una. También permite elegir un medicamento de la base de datos. Al presionar guardar, el sistema valida que todos los campos obligatorios estén completos, y si todo es correcto, registra la receta en la base de datos, vinculándola a la consulta y al medicamento correspondiente. Luego, muestra un mensaje de retroalimentación al usuario y limpia el formulario.

Ejemplo de Método para Registrar un Medicamento:

@FXML
private void handleGuardarMedicina(ActionEvent event) {
    String nombre = txtNombre.getText().trim();
    String descripcion = txtDescripcion.getText().trim();
    String cantidadStr = txtCantidad.getText().trim();
    LocalDate fechaCaducidad = datePickerCaducidad.getValue();

    if(nombre.isEmpty() || descripcion.isEmpty() || cantidadStr.isEmpty() || fechaCaducidad == null) {
        mostrarAlerta("Error", "Debe completar todos los campos");
        return;
    }

    int cantidad;
    try {
        cantidad = Integer.parseInt(cantidadStr);
        if(cantidad < 0) throw new NumberFormatException();
    } catch(NumberFormatException e) {
        mostrarAlerta("Error", "Cantidad inválida");
        return;
    }

    String sql = "INSERT INTO medicamentos (nombre, descripcion, cantidad, fecha_caducidad) VALUES (?, ?, ?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setInt(3, cantidad);
        ps.setDate(4, Date.valueOf(fechaCaducidad));

        int filas = ps.executeUpdate();

        if(filas > 0) {
            mostrarAlerta("Éxito", "Medicamento registrado correctamente");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el medicamento");
        }
    } catch(SQLException e) {
        mostrarAlerta("Error", "Error en base de datos: " + e.getMessage());
    }
}

Este fragmento de código muestra cómo se realiza la validación y el registro de un medicamento, asegurando integridad en los datos antes de realizar cualquier inserción.

Gestión de Vacunas

La vista AgregarVacunaView.fxml proporciona una interfaz gráfica para registrar y administrar las vacunas disponibles en la veterinaria. El formulario incluye campos como el nombre de la vacuna, una descripción, la dosis recomendada y la fecha de caducidad. Además, incorpora botones para guardar los datos, limpiar el formulario o cancelar la operación.

El controlador correspondiente, AgregarVacunaController.java, gestiona la lógica de este módulo. En el método initialize(), se cargan configuraciones iniciales y se limpian los campos del formulario. El método clave handleGuardarVacuna(ActionEvent event) se encarga de validar que todos los campos estén completos y en el formato correcto, incluyendo la conversión y validación de la fecha de caducidad. Si la validación es exitosa, se ejecuta una sentencia SQL preparada que inserta los datos en la tabla vacunas de la base de datos. Luego, se muestra un mensaje informativo sobre el resultado de la operación y se limpia el formulario para futuros registros.

Gestión de Vacunaciones

La vista AgregarVacunacionView.fxml permite registrar la aplicación de vacunas a las mascotas. En esta interfaz, el usuario puede seleccionar la mascota correspondiente, elegir la vacuna aplicada y definir la fecha de aplicación mediante un selector de calendario. También incluye botones para guardar o cancelar la acción.

El controlador AgregarVacunacionController.java gestiona la lógica de este proceso. Al iniciar, carga las listas de mascotas y vacunas disponibles para que el usuario pueda seleccionarlas desde combos desplegables. El método handleGuardarVacunacion(ActionEvent event) valida que se haya seleccionado una mascota, una vacuna y una fecha válida. Si todos los datos están correctos, se registra la vacunación en la base de datos mediante una sentencia SQL preparada, vinculando la mascota, la vacuna y la fecha de aplicación. El sistema muestra al usuario un mensaje de confirmación en caso de éxito o un mensaje de error si ocurre algún problema, y posteriormente limpia el formulario para nuevos registros.

Ejemplo de Código para Guardar una Vacunación:

@FXML
private void handleGuardarVacunacion(ActionEvent event) {
    Mascota mascota = comboMascotas.getValue();
    Vacuna vacuna = comboVacunas.getValue();
    LocalDate fechaAplicacion = datePickerFecha.getValue();

    if(mascota == null || vacuna == null || fechaAplicacion == null) {
        mostrarAlerta("Error", "Debe seleccionar mascota, vacuna y fecha");
        return;
    }

    String sql = "INSERT INTO vacunaciones (mascota_id, vacuna_id, fecha_aplicacion) VALUES (?, ?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, mascota.getId());
        ps.setInt(2, vacuna.getId());
        ps.setDate(3, Date.valueOf(fechaAplicacion));

        int filas = ps.executeUpdate();

        if(filas > 0) {
            mostrarAlerta("Éxito", "Vacunación registrada correctamente");
            limpiarFormulario();
        } else {
            mostrarAlerta("Error", "No se pudo registrar la vacunación");
        }
    } catch(SQLException e) {
        mostrarAlerta("Error", "Error en base de datos: " + e.getMessage());
    }
}

Este fragmento de código ejemplifica el procedimiento de validación y almacenamiento de una vacunación, asegurando integridad en los datos y proporcionando retroalimentación al usuario.

Gestión de Proveedores

La interfaz AgregarProveedorView.fxml permite registrar y administrar proveedores que suministran medicamentos, insumos y productos a la veterinaria. Este formulario contiene campos para ingresar el nombre del proveedor, dirección, teléfono y correo electrónico. También incluye botones para guardar, limpiar el formulario o cancelar la operación.

El controlador AgregarProveedorController.java se encarga de manejar la lógica del registro de proveedores. En su método initialize(), se limpian los campos y se pueden preparar listas auxiliares si es necesario. El método handleGuardarProveedor(ActionEvent event) realiza la validación de todos los campos obligatorios, asegurándose de que el teléfono y el correo electrónico estén en el formato correcto. Si los datos son válidos, se construye una sentencia SQL preparada con PreparedStatement que inserta los datos en la tabla proveedores. Luego, se muestra un mensaje de confirmación o error al usuario, y el formulario se limpia para permitir nuevos registros.

Gestión de Compras

La vista AgregarCompraView.fxml proporciona la interfaz para registrar compras realizadas a proveedores. Esta ventana permite al usuario seleccionar un proveedor mediante un combo box, definir la fecha de compra, y gestionar una lista de productos o medicamentos comprados, especificando cantidad y precio por unidad. Se incluyen botones para agregar productos a la lista de compra, eliminar productos seleccionados, y guardar la compra.

El controlador AgregarCompraController.java gestiona este proceso. En su método initialize(), se cargan desde la base de datos los proveedores y medicamentos disponibles para ser seleccionados. También se inicializa la tabla que mostrará los productos añadidos a la compra.

El método handleAgregarProductoCompra(ActionEvent event) valida que el producto, la cantidad y el precio sean válidos antes de agregarlo a la lista. Luego, actualiza la tabla visual y recalcula el total.

El método principal handleGuardarCompra(ActionEvent event) valida que se haya seleccionado un proveedor y una fecha, y que la lista de productos no esté vacía. Si todo es correcto, se inserta primero el encabezado de la compra en la tabla compras, utilizando una sentencia SQL con retorno de claves generadas (RETURN_GENERATED_KEYS) para capturar el ID de la compra recién creada. A continuación, por cada producto en la lista, se inserta un registro en la tabla detalle_compras, asociándolo a la compra mediante el ID, y se especifican el medicamento, la cantidad y el precio.

Opcionalmente, también se puede actualizar el stock de los medicamentos comprados. Tras completar el proceso, se informa al usuario mediante un mensaje de éxito y se limpia el formulario para permitir nuevas compras.

Ejemplo Simplificado del Método para Guardar Compra:

@FXML
private void handleGuardarCompra(ActionEvent event) {
    Proveedor proveedor = comboProveedores.getValue();
    LocalDate fechaCompra = datePickerFecha.getValue();

    if(proveedor == null || fechaCompra == null || listaProductos.isEmpty()) {
        mostrarAlerta("Error", "Debe seleccionar proveedor, fecha y agregar productos");
        return;
    }

    String sqlCompra = "INSERT INTO compras (proveedor_id, fecha) VALUES (?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement psCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {

        psCompra.setInt(1, proveedor.getId());
        psCompra.setDate(2, Date.valueOf(fechaCompra));
        psCompra.executeUpdate();

        ResultSet rs = psCompra.getGeneratedKeys();
        int compraId = -1;
        if(rs.next()) {
            compraId = rs.getInt(1);
        }

        for(ProductoCompra prod : listaProductos) {
            String sqlDetalle = "INSERT INTO detalle_compras (compra_id, medicamento_id, cantidad, precio) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                psDetalle.setInt(1, compraId);
                psDetalle.setInt(2, prod.getMedicamento().getId());
                psDetalle.setInt(3, prod.getCantidad());
                psDetalle.setDouble(4, prod.getPrecio());
                psDetalle.executeUpdate();
            }
            // Aquí puede ir código para actualizar stock en inventario
        }

        mostrarAlerta("Éxito", "Compra registrada correctamente");
        limpiarFormulario();

    } catch (SQLException e) {
        mostrarAlerta("Error", "Error en base de datos: " + e.getMessage());
    }
}

Gestión de Facturación

La vista FacturaView.fxml proporciona una interfaz completa para generar y administrar facturas relacionadas con servicios y productos ofrecidos a los clientes. La ventana permite seleccionar un cliente y, opcionalmente, asociar una cita o servicio previamente registrado. Incluye una lista dinámica de productos y servicios agregados a la factura, donde cada ítem contiene su cantidad, precio unitario y subtotal correspondiente. También se muestra automáticamente el total a pagar, y se ofrecen botones para agregar productos, eliminarlos, generar la factura y limpiar el formulario.

El controlador FacturaController.java gestiona todo el flujo de creación de facturas. Utiliza listas internas para almacenar temporalmente los productos o servicios que forman parte de la factura. Además, mantiene referencias a los componentes de la interfaz gráfica y a objetos modelo como Factura y DetalleFactura, junto con las conexiones necesarias a la base de datos.

Principales métodos:

1. initialize()
Este método inicializa la vista cargando la lista de clientes disponibles desde la base de datos, configurando la tabla de productos, y estableciendo eventos para los botones.

2. handleAgregarProducto()
Permite agregar un producto o servicio a la lista de la factura. Valida la cantidad ingresada y actualiza tanto la tabla de resumen como el total a pagar.

3. handleEliminarProducto()
Elimina el producto seleccionado de la tabla de productos y ajusta el total automáticamente.

4. handleGenerarFactura()
Este método se encarga de validar que haya productos seleccionados y un cliente asignado. Luego, construye un objeto Factura con los datos recopilados (cliente, fecha actual, total calculado) y lo inserta en la base de datos. Una vez registrada la factura, se insertan todos los detalles (productos) asociados mediante un ciclo que recorre la lista. Al finalizar, se muestra un mensaje de éxito o error y se limpia el formulario para una nueva operación.


Ejemplo simplificado del método handleGenerarFactura():

public void handleGenerarFactura() {
    if(productosFactura.isEmpty()) {
        mostrarAlerta("Error", "Debe agregar productos");
        return;
    }
    if(comboClientes.getValue() == null) {
        mostrarAlerta("Error", "Debe seleccionar un cliente");
        return;
    }
    try {
        Factura factura = new Factura();
        factura.setCliente(comboClientes.getValue());
        factura.setFecha(new Date());
        factura.setTotal(calcularTotal());

        int idFactura = conexion.insertarFactura(factura);

        for(Producto prod : productosFactura) {
            DetalleFactura detalle = new DetalleFactura(idFactura, prod, prod.getCantidad(), prod.getPrecio());
            conexion.insertarDetalleFactura(detalle);
        }

        mostrarAlerta("Éxito", "Factura generada correctamente");
        limpiarFormulario();

    } catch(SQLException e) {
        mostrarAlerta("Error", "Error al generar factura: " + e.getMessage());
    }
}

Gestión de Informes

La vista InformeView2.fxml facilita la generación de reportes administrativos y clínicos que permiten realizar un seguimiento detallado de la operación en la veterinaria. Esta ventana permite al usuario seleccionar el tipo de informe que desea visualizar (ventas, consultas, vacunaciones, etc.), aplicar filtros por fecha u otros parámetros relevantes, y visualizar los resultados en forma de tablas o gráficos.

El controlador InformeController.java maneja la lógica de los informes. Permite consultar datos complejos de la base mediante sentencias SQL agregadas o procedimientos, llenar las estructuras visuales con los resultados y exportar los datos en distintos formatos (como PDF o Excel).

Funcionalidades clave:

- Carga y presenta datos relevantes según los filtros seleccionados.
- Utiliza consultas SQL para obtener estadísticas como:
- Total de ventas por periodo.
- Número de consultas atendidas.
- Historial de vacunaciones por mascota o especie.
- Llena tablas, gráficos o áreas de texto con los datos recuperados.
- Proporciona botones para exportar los resultados o imprimirlos.
- Maneja errores de forma controlada, notificando al usuario en caso de fallos en la consulta o conexión.

Gestión de Registro de Usuarios

La vista RegistrarceView.fxml proporciona un formulario de registro para nuevos usuarios en el sistema. Este formulario permite a los usuarios introducir su nombre completo, nombre de usuario, contraseña, confirmación de contraseña y correo electrónico. También incluye botones para enviar el registro o limpiar el formulario.

El controlador RegistrarController.java se encarga de gestionar la lógica de validación y registro de nuevos usuarios. A continuación se detallan los pasos clave en este proceso.

Funciones y Flujo:

- Validaciones iniciales:
	- Verifica que todos los campos estén completos.
	- Asegura que la contraseña y su confirmación coincidan.
	- Comprueba que el formato del correo electrónico sea válido.
	- Valida que el nombre de usuario no exista previamente en la base de datos.

- Inserción en la base de datos:
	- Prepara y ejecuta una sentencia INSERT para agregar al usuario a la base de datos.
	- Utiliza PreparedStatement para evitar inyecciones SQL.

- Feedback al usuario:
	- Muestra mensajes informativos al usuario, tanto para un registro exitoso como para errores (por ejemplo, usuario duplicado o fallo en la conexión).
	- Después de un registro exitoso, limpia el formulario o redirige al usuario al formulario de inicio de sesión.

Controladores Auxiliares de Sonido y Música

Clases: SonidoController.java y MusicaFondo.java

SonidoController:

- Se encarga de controlar la reproducción de sonidos para eventos específicos dentro de la aplicación (como clics, errores, y alertas).
- Utiliza la librería de audio de JavaFX para manejar archivos de sonido en formato .mp3 o .wav.
- Incluye métodos para reproducir, pausar y detener sonidos, los cuales pueden ser invocados desde otros controladores para mejorar la experiencia de usuario.

MusicaFondo:
- Gestiona la música ambiental que se reproduce en bucle durante el uso de la aplicación.
- Inicia automáticamente la reproducción al arrancar la aplicación.
- Permite pausar o cambiar la música según las preferencias del usuario.

Conexión y Validación de Usuarios

1. Conexión a la Base de Datos
Clase: Conexion.java
- Patrón Singleton: Implementa un patrón Singleton para gestionar la conexión a la base de datos. Esto asegura que solo haya una instancia de conexión activa a la base de datos en todo momento, lo que optimiza el rendimiento.
- JDBC: Utiliza JDBC para la ejecución de sentencias SQL y para manejar la conexión a la base de datos.

Métodos clave:

- Obtener conexión.
- Ejecutar consultas y actualizaciones en la base de datos.
- Cerrar recursos como conexiones y ResultSet.

2. Validación de Usuarios
Clase: ValidadorUsuario.java

- Contiene métodos estáticos para validar las credenciales de los usuarios durante el proceso de inicio de sesión.
- Utiliza la clase Conexion para realizar consultas a la tabla de usuarios y verificar si las credenciales proporcionadas coinciden con las almacenadas en la base de datos.
- El método principal, validarUsuario(String usuario, String contraseña), devuelve true si el usuario existe y la contraseña es correcta, o false en caso contrario.
- Maneja excepciones SQL y se asegura de cerrar los recursos correctamente.

Recursos Multimedia y Estilos

1. Recursos Multimedia
- Carpeta resource: Contiene archivos de sonido (.mp3) utilizados para efectos de clic, alertas, errores y música de fondo. Estos sonidos ayudan a mejorar la interacción del usuario con el sistema al ofrecer retroalimentación auditiva en eventos clave.
- Carpeta image: Incluye imágenes e íconos utilizados en las interfaces de usuario, como logotipos, fondos y botones gráficos. Estas imágenes se cargan dinámicamente para dar una apariencia atractiva a la interfaz y mejorar la experiencia visual del usuario.

2. Estilos CSS
- Archivo EstilosCss.css: Contiene reglas de estilo que se aplican a las vistas JavaFX para mejorar la apariencia general de la aplicación. Este archivo proporciona una capa de personalización visual que hace que la interfaz sea más intuitiva y agradable para el usuario.

Estos recursos multimedia y estilos son cargados automáticamente en la aplicación para proporcionar una experiencia más completa, atractiva y coherente para el usuario.

Conclusión
El proyecto Veterinaria2025BM es un sistema integral que cubre desde la gestión administrativa (usuarios, clientes, empleados, proveedores), pasando por la atención clínica (mascotas, citas, consultas, tratamientos, vacunaciones), hasta el control financiero (compras, facturación e informes).
Cada módulo está cuidadosamente diseñado para validar datos, interactuar con la base de datos mediante JDBC y ofrecer una interfaz gráfica moderna con JavaFX, apoyada en recursos visuales y auditivos para enriquecer la experiencia.

Elementos Utilizados en el Proyecto

Este proyecto hace uso de diversas tecnologías y enfoques de programación para crear una aplicación robusta y eficiente. A continuación, se describen los elementos clave utilizados en el desarrollo del sistema.

1. JavaFX (para la interfaz gráfica)
JavaFX se utilizó para construir la interfaz gráfica del sistema, aprovechando su flexibilidad y facilidad para crear interfaces visuales interactivas. Los principales componentes utilizados son:

- Stage y Scene: Son utilizados para gestionar las ventanas y escenas de la aplicación. Cada ventana del sistema se define como una escena dentro de un stage, facilitando el manejo de múltiples pantallas en la aplicación.

Controles UI:

- TextField: Para campos de texto que permiten la entrada de información por parte del usuario.
- PasswordField: Para la entrada de contraseñas, asegurando que el texto sea ocultado.
- ComboBox: Para permitir al usuario seleccionar entre una lista de opciones.
- TableView: Para mostrar datos en formato tabular, útil para mostrar listas de registros como clientes, productos, etc.
- DatePicker: Para seleccionar fechas de manera fácil y visual.
- Button: Para permitir la interacción del usuario con el sistema (por ejemplo, guardar, cancelar, etc.).

Archivos .fxml:
- Se utilizaron archivos .fxml para definir la estructura visual de cada ventana, separando la interfaz gráfica de la lógica del programa. Esto mejora la modularidad y la facilidad de mantenimiento.

2. FXML Loader
El FXML Loader se utiliza para cargar los archivos .fxml que contienen la definición de las vistas. Este componente se encarga de asociar los archivos .fxml con los controladores correspondientes, estableciendo una conexión entre la interfaz y la lógica del programa.

3. Patrón MVC (Modelo-Vista-Controlador)
El proyecto sigue el patrón MVC (Modelo-Vista-Controlador), que proporciona una estructura organizada y separada entre la interfaz de usuario, la lógica del negocio y los datos:

- Modelo: Representa las entidades del sistema como Cliente, Mascota, Empleado, Veterinario, Cita, Factura, etc. Estas clases son POJOs (Plain Old Java Objects), utilizadas para transportar y manipular datos entre la base de datos y la interfaz gráfica.
- Vista: Representa la interfaz gráfica del usuario, definida mediante archivos .fxml y gestionada por JavaFX.
- Controlador: Clases Java que gestionan la lógica y eventos de la interfaz, respondiendo a las acciones del usuario.

4. JDBC (Java Database Connectivity)
Para la conexión con la base de datos, el proyecto utiliza JDBC, que permite ejecutar consultas SQL y manejar las transacciones de forma eficiente.

- Se hace uso de PreparedStatement para prevenir inyecciones SQL y para manejar parámetros en las consultas de manera segura.
- Se implementa un manejo adecuado de transacciones y el cierre de recursos (conexiones, ResultSet, PreparedStatement) para garantizar el rendimiento y evitar fugas de recursos.

5. Validaciones y Manejo de Eventos
- Validaciones de campos de entrada: Se utilizan expresiones regulares y lógica programática para asegurar que los datos ingresados por los usuarios sean correctos, como verificar el formato de un correo electrónico o que una contraseña tenga la longitud adecuada.
- Manejo de eventos: El sistema responde a las interacciones del usuario mediante métodos anotados con @FXML que están vinculados a eventos como clics de botones, selecciones de combo boxes, etc.
- Uso de Alert de JavaFX: Para mostrar mensajes de retroalimentación al usuario, como alertas de éxito, error o advertencia.

6. Modelo de Datos (Clases POJO)
Las clases POJO (Plain Old Java Object) representan las entidades del sistema, como Clientes, Empleados, Mascotas, Veterinarios, Citas, Facturas, etc. Estas clases se utilizan para transportar los datos entre la base de datos y la interfaz de usuario, facilitando la manipulación de información dentro del sistema.

7. Recursos Multimedia
- Imágenes e íconos: El sistema utiliza imágenes y íconos cargados dinámicamente desde la carpeta de recursos. Estos se utilizan para elementos visuales en la interfaz, como botones gráficos, logotipos, y fondos de las ventanas.
- Sonidos: Se incorporan sonidos específicos para mejorar la experiencia del usuario, como clics, alertas de error y notificaciones. Estos sonidos son gestionados por clases especializadas que controlan la reproducción de los efectos de audio.

8. Manejo de Sesiones y Autenticación
El sistema controla el acceso de los usuarios mediante validación de credenciales en la base de datos. Esto incluye vistas y controladores específicos para el login y el registro de nuevos usuarios, asegurando que solo los usuarios autorizados puedan acceder a la aplicación.

9. Organización Modular
El sistema está organizado de manera modular, donde cada funcionalidad del sistema se encapsula en su propio módulo. Cada módulo está compuesto por su vista (archivo .fxml) y su controlador correspondiente (clase Java), lo que facilita la escalabilidad y el mantenimiento del proyecto.