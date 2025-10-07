-- drop database if exists DB_Veterinaria;
create database DB_Veterinaria;
use DB_Veterinaria;

create table Clientes (
	codigoCliente int not null auto_increment,
    nombreCliente varchar(75) not null,
    apellidoCliente varchar(75) not null,
    telefonoCliente varchar(8) not null,
    direccionCliente varchar(200) not null,
    correoCliente varchar(80),
    fechaRegistro date not null,
    primary key PK_codigoCliente (codigoCliente)
);

create table Veterinarios (
	codigoVeterinario int not null auto_increment,
    nombreVeterinario varchar(75) not null,
    apellidoVeterinario varchar(75) not null,
    especialidad varchar(100) not null,
    telefonoVeterinario varchar(8) not null,
    correoVeterinario varchar(100) not null,
    fechaIngreso date not null,
    estado enum('Activo','Inactivo'),
    primary key PK_codigoVeterinario (codigoVeterinario)
);

create table Proveedores (
	codigoProveedor int not null auto_increment,
    nombreProveedor varchar(100) not null,
    direccionProveedor varchar(255) not null,
    telefonoProveedor varchar(10) not null,
    correoProveedor varchar(100) not null,
    primary key PK_codigoProveedor (codigoProveedor)
);

create table Vacunas (
	codigoVacuna int not null auto_increment,
    nombreVacuna varchar(100) not null,
    descripcion varchar(255),
    dosis varchar(100),
    frecuenciaMeses int,
    primary key PK_codigoVacuna (codigoVacuna)
);

create table Empleados (
	codigoEmpleado int not null auto_increment,
    nombreEmpleado varchar(50) not null,
    apellidoEmpleado varchar(50) not null,
    cargo varchar(50) not null,
    telefonoEmpleado varchar(20) not null,
    correoEmpleado varchar(100),
    primary key PK_codigoEmpleado (codigoEmpleado)
);

create table Mascotas (
	codigoMascota int not null auto_increment,
    nombreMascota varchar(50) not null,
    especie varchar(30) not null,
    raza varchar(50) not null,
    sexo enum('Macho','Hembra'),
    fechaNacimiento date not null,
    color varchar(30),
    pesoActualKg decimal(5,2),
    codigoCliente int not null,
    primary key PK_codigoMascota (codigoMascota),
    constraint FK_Mascotas_Clientes foreign key (codigoCliente) 
		references Clientes (codigoCliente)
);

create table Consultas (
	codigoConsulta int not null auto_increment,
    fechaConsulta datetime not null,
    motivo varchar(255) not null,
    diagnostico varchar(255) not null,
    observaciones varchar(255),
    codigoMascota int not null,
    codigoVeterinario int not null,
    primary key PK_codigoConsulta (codigoConsulta),
    constraint FK_Consultas_Mascotas foreign key (codigoMascota)
		references Mascotas (codigoMascota),
	constraint FK_Consultas_Veterinarios foreign key (codigoVeterinario)
		references Veterinarios (codigoVeterinario)
);

create table Tratamientos (
	codigoTratamiento int not null auto_increment,
    descripcion varchar(255) not null,
    fechaInicio date not null,
    fechaFin date not null,
    medicamentosIndicados varchar(255),
    codigoConsulta int not null,
    primary key PK_codigoTratamiento (codigoTratamiento),
    constraint FK_Tratamientos_Consultas foreign key (codigoConsulta)
		references Consultas (codigoConsulta)
);

create table Citas (
	codigoCita int not null auto_increment,
    fechaCita date not null,
    horaCita time not null,
    motivo varchar(255) not null,
    estado enum('Pendiente','Completado','Cancelado'),
    codigoMascota int not null,
    codigoVeterinario int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Mascotas foreign key (codigoMascota)
		references Mascotas (codigoMascota),
	constraint FK_Citas_Veterinarios foreign key (codigoVeterinario)
		references Veterinarios (codigoVeterinario)
);

create table Vacunaciones (
	codigoVacunacion int not null auto_increment,
    fechaAplicacion date not null,
    observaciones varchar(250),
    codigoMascota int not null,
    codigoVacuna int not null,
    codigoVeterinario int not null,
    primary key codigoVacunacion (codigoVacunacion),
    constraint FK_Vacunaciones_Mascotas foreign key (codigoMascota) 
		references Mascotas (codigoMascota),
	constraint FK_Vacunaciones_Vacunas foreign key (codigoVacuna) 
		references Vacunas (codigoVacuna),
	constraint FK_Vacunaciones_Veterinarios foreign key (codigoVeterinario)
		references Veterinarios (codigoVeterinario)
);

create table Medicamentos (
	codigoMedicamento int not null auto_increment,
    nombreMedicamento varchar(100) not null,
    descripcion varchar(250) not null,
    stock int not null,
    precioUnitario decimal(10,2) not null,
    fechaVencimiento date,
    codigoProveedor int not null,
    primary key PK_codigoMedicamento (codigoMedicamento),
    constraint FK_Medicamentos_Proveedores foreign key (codigoProveedor)
		references Proveedores (codigoProveedor)
);

create table Recetas (
	codigoReceta int not null auto_increment,
    dosis varchar(100) not null,
    frecuencia varchar(100) not null,
    duracionDias int not null,
    indicaciones varchar(255),
    codigoConsulta int,
    codigoMedicamento int,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Recetas_Consultas foreign key (codigoConsulta)
		references Consultas (codigoConsulta),
	constraint FK_Recetas_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos (codigoMedicamento)
);

create table Facturas (
	codigoFactura int not null auto_increment,
    fechaEmision date not null,
    total decimal(10,2) default 0.00,
    metodoPago enum('Efectivo', 'Tarjeta', 'Transferencia'),
    codigoCliente int not null,
    codigoEmpleado int not null,
    primary key PK_codigoFactura (codigoFactura),
    constraint FK_Facturas_Clientes foreign key (codigoCliente) 
		references Clientes (codigoCliente),
	constraint FK_Facturas_Empleados foreign key (codigoEmpleado) 
		references Empleados (codigoEmpleado)
);

create table Compras (
	codigoCompra int not null auto_increment,
    fechaCompra date not null,
    total decimal(10,2) not null,
    detalle varchar(255) not null,
    codigoProveedor int not null,
    primary key PK_codigoCompra (codigoCompra),
    constraint FK_Compras_Proveedores foreign key (codigoProveedor)
		references Proveedores (codigoProveedor)
);

create table Usuarios (
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(50),
    contraseñaUsuario varchar(100),
    nivelAcceso enum('Empleado', 'Administrador', 'Propietario'),
    primary key PK_codigoUsuario (codigoUsuario)
);

-- ------------------------- PROCEDIMIENTOS ALMACENADOS -------------------------
-- PROCEDIMIENTOS ALMACENADOS (CLIENTES) -------------------------
-- AGREGAR CLIENTE
Delimiter $$
create procedure sp_AgregarCliente (
	in nombreCliente varchar(75),
    in apellidoCliente varchar(75),
    in telefonoCliente varchar(8),
    in direccionCliente varchar(200),
    in correoCliente varchar(80),
    in fechaRegistro date)
    begin
		insert into Clientes (nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, correoCLiente, fechaRegistro)
			values (nombreCliente, apellidoCliente, telefonoCliente, direccionCliente, correoCLiente, fechaRegistro);
    end$$
Delimiter ;
call sp_AgregarCliente('Julio', 'Realiquez', '31100319', 'Avenida Reforma 12-34, Zona 9, Ciudad', 'jrealiquez@gmail.com', '2025-05-10');
call sp_AgregarCliente('Ana', 'García', '09456789', 'Calle 5, Zona 15, Ciudad de Guatemala', 'agarcia@gmail.com', '2025-07-21');
call sp_AgregarCliente('Luis', 'Martínez', '47010222', 'Calle 10, Colonia El Milagro, Mixco', 'lmartinez@yahoo.com', '2025-06-15');
call sp_AgregarCliente('Laura', 'López', '82500821', 'Avenida Las Américas 9-23, Zona 14, Ciudad', 'llopez@hotmail.com', '2025-08-05');
call sp_AgregarCliente('Juan', 'Hernández', '33100988', 'Calle del Sol 22, Zona 11, Ciudad', 'jhernandez@gmail.com', '2025-09-10');
call sp_AgregarCliente('Marta', 'Pérez', '21766123', 'Calle Los Olivos 5-32, Santa Catarina Pinula', 'mperez@gmail.com', '2025-04-20');
call sp_AgregarCliente('Eduardo', 'Jiménez', '08912345', 'Avenida Las Gardenias 7-8, Zona 13, Ciudad', 'ejimenez@yahoo.com', '2025-10-30');
call sp_AgregarCliente('Isabel', 'Rodríguez', '42111234', 'Calle del Árbol 14, Villa Nueva', 'irodriguez@gmail.com', '2025-03-15');
call sp_AgregarCliente('Sofía', 'Ríos', '56234567', 'Carretera a El Salvador Km. 18, San Salvador', 'srios@gmail.com', '2025-02-05');
call sp_AgregarCliente('Mario', 'Vásquez', '76345678', 'Calle 8, Zona 7, Ciudad', 'mvasquez@hotmail.com', '2025-01-28');
call sp_AgregarCliente('Paola', 'Fernández', '45478912', 'Calle San Juan 15-23, Antigua Guatemala', 'pfernandez@gmail.com', '2025-11-19');
call sp_AgregarCliente('Ricardo', 'Sánchez', '23123456', 'Avenida 4, Zona 2, Ciudad', 'rsanchez@hotmail.com', '2025-12-10');
call sp_AgregarCliente('Verónica', 'Gómez', '11987654', 'Calle Central 33-3, Zona 10, Ciudad', 'vgomez@gmail.com', '2025-01-17');
call sp_AgregarCliente('Pedro', 'Torres', '56654321', 'Avenida Santa Fe 1-8, Zona 12, Ciudad', 'ptorres@yahoo.com', '2025-04-02');
call sp_AgregarCliente('Patricia', 'Méndez', '16567890', 'Calle 1, Zona 18, Ciudad', 'pmendez@gmail.com', '2025-09-25');

-- LISTAR CLIENTES
Delimiter $$
create procedure sp_ListarClientes ()
	begin
		select*from Clientes;
    end$$
Delimiter ;
call sp_ListarClientes();

-- ELIMINAR CLIENTE
Delimiter $$
create procedure sp_EliminarCliente (
	in codCliente int)
    begin
		delete from Clientes where codigoCliente = codCliente;
    end$$
Delimiter ;
call sp_EliminarCliente(1);

-- BUSCAR CLIENTE
Delimiter $$
create procedure sp_BuscarCliente (
    in codCliente int)
    begin
        select * from Clientes where codigoCliente = codCliente;
    end$$
Delimiter ;
call sp_BuscarCliente(2);

-- EDITAR CLIENTE
Delimiter $$
create procedure sp_EditarCliente (
	in codCliente int,
	in nomCliente varchar(75),
    in apelCliente varchar(75),
    in telefCliente varchar(8),
    in direcCliente varchar(200),
    in corCliente varchar(80),
    in fechRegistro date)
    begin
		update Clientes set nombreCliente = nomCliente, apellidoCliente = apelCliente, telefonoCliente = telefCliente, 
			direccionCliente = direcCliente, correoCliente= corCliente, fechaRegistro = fechRegistro 
				where codigoCliente = codCliente;
    end$$
Delimiter ;
call sp_EditarCliente(2, 'Gabriel', 'Noriega', '45482010', 'Zona 3, Barrio el Gallito', 'gnoriega-2021549@kinal.edu.gt', '2025-01-22');

-- PROCEDIMIENTOS ALMACENADOS (VETERINARIOS) -------------------------
-- AGREGAR VETERINARIO
Delimiter $$
create procedure sp_AgregarVeterinario (
    in nombreVeterinario varchar(75),
    in apellidoVeterinario varchar(75),
	in especialidad varchar(100),
    in telefonoVeterinario varchar(8),
    in correoVeterinario varchar(100),
    in fechaIngreso date,
    in estado enum('Activo','Inactivo'))
    begin
		insert into Veterinarios (nombreVeterinario, apellidoVeterinario, especialidad, telefonoVeterinario, correoVeterinario, fechaIngreso, estado) 
			values (nombreVeterinario, apellidoVeterinario, especialidad, telefonoVeterinario, correoVeterinario, fechaIngreso, estado);
    end$$
Delimiter ;
call sp_AgregarVeterinario ('Jonathan', 'Jick', 'Medicina Interna', '44444444', 'jjick-2021464@gmail.com', '2025-04-24', 'Activo');
call sp_AgregarVeterinario('José', 'López', 'Cirugía', '31234567', 'jlopez@gmail.com', '2025-06-05', 'Activo');
call sp_AgregarVeterinario('Carla', 'Morales', 'Dermatología', '31345678', 'cmorales@yahoo.com', '2025-05-15', 'Activo');
call sp_AgregarVeterinario('Miguel', 'Gutiérrez', 'Medicina General', '31456789', 'mgutierrez@gmail.com', '2025-04-10', 'Activo');
call sp_AgregarVeterinario('Lucía', 'Castro', 'Cardiología', '31567890', 'lcastro@gmail.com', '2025-07-03', 'Inactivo');
call sp_AgregarVeterinario('Felipe', 'Ramírez', 'Oncología', '31122334', 'framirez@gmail.com', '2025-03-25', 'Activo');
call sp_AgregarVeterinario('María', 'Hernández', 'Radiología', '31678901', 'mhernandez@hotmail.com', '2025-08-12', 'Activo');
call sp_AgregarVeterinario('Carlos', 'Pérez', 'Pediatría Veterinaria', '31789012', 'cperez@gmail.com', '2025-10-20', 'Activo');
call sp_AgregarVeterinario('Roberto', 'Jiménez', 'Cirugía', '31890123', 'rjimenez@yahoo.com', '2025-09-18', 'Activo');
call sp_AgregarVeterinario('Andrea', 'García', 'Medicina Preventiva', '31901234', 'agarcia@gmail.com', '2025-06-25', 'Activo');
call sp_AgregarVeterinario('Raúl', 'Sánchez', 'Anestesiología', '32012345', 'rsanchez@gmail.com', '2025-11-05', 'Inactivo');
call sp_AgregarVeterinario('Sandra', 'Vásquez', 'Medicina General', '32123456', 'svasquez@gmail.com', '2025-01-17', 'Activo');
call sp_AgregarVeterinario('Antonio', 'Díaz', 'Dermatología', '32234567', 'adiaz@yahoo.com', '2025-07-11', 'Activo');
call sp_AgregarVeterinario('Esteban', 'Mendoza', 'Radiología', '32345678', 'emendoza@gmail.com', '2025-02-28', 'Activo');
call sp_AgregarVeterinario('Natalia', 'López', 'Pediatría Veterinaria', '32456789', 'nlopez@gmail.com', '2025-04-07', 'Activo');

-- LISTAR VETERINARIOS
Delimiter $$
create procedure sp_ListarVeterinarios ()
	begin
		select*from Veterinarios;
    end$$
Delimiter ;
call sp_ListarVeterinarios();

-- ELIMINAR VETERINARIO
Delimiter $$
create procedure sp_EliminarVeterinario (
	in codVeterinario int)
    begin
		delete from Veterinarios where codigoVeterinario = codVeterinario;
    end$$
Delimiter ;
call sp_EliminarVeterinario(1);

-- BUSCAR VETERINARIO
Delimiter $$
create procedure sp_BuscarVeterinario (
    in codVeterinario int)
    begin
        select * from Veterinarios where codigoVeterinario = codVeterinario;
    end$$
Delimiter ;
call sp_BuscarVeterinario(2);

-- EDITAR VETERINARIO
Delimiter $$
create procedure sp_EditarVeterinario (
	in codVeterinario int,
	in nomVeterinario varchar(75),
    in apelVeterinario varchar(75),
    in especial varchar(100),
    in telefVeterinario varchar(8),
	in corVeterinario varchar(100),
    in fechIngreso date,
    esta enum('Activo','Inactivo'))
    begin
		update Veterinarios set nombreVeterinario = nomVeterinario, apellidoVeterinario = apelVeterinario, especialidad = especial, 
			telefonoVeterinario = telefVeterinario, correoVeterinario = corVeterinario, fechaIngreso = fechIngreso, estado = esta
				where codigoVeterinario = codVeterinario;
    end$$
Delimiter ;
call sp_EditarVeterinario(1, 'Caelia', 'Yasaka', 'Anestesiología', '64200420', 'cyasaka-2021564@kinal.edu.gt', '2020-04-14', 'Activo');

-- PROCEDIMIENTOS ALMACENADOS (PROVEEDORES) -------------------------
-- AGREGAR PROVEEDOR
Delimiter $$
create procedure sp_AgregarProveedor (
    in nombreProveedor varchar(100),
    in direccionProveedor varchar(255),
    in telefonoProveedor varchar(10),
    in correoProveedor varchar(100))
    begin
        insert into Proveedores (nombreProveedor, direccionProveedor, telefonoProveedor, correoProveedor) 
            values (nombreProveedor, direccionProveedor, telefonoProveedor, correoProveedor);
    end$$
Delimiter ;
call sp_AgregarProveedor('Kinal Pets Company', 'Avenida Las Palmas 12, Zona 16, Ciudad', '02122017', 'contacto@kinalpetscompany.com');
call sp_AgregarProveedor('MedicVet', 'Carretera a El Salvador Km. 20, Ciudad', '31234567', 'ventas@medicvet.com');
call sp_AgregarProveedor('Farmavet', 'Calle Nueva 5-6, Santa Catarina Pinula', '31345678', 'info@farmavet.com');
call sp_AgregarProveedor('PetCare', 'Calle 1, Zona 8, Ciudad', '31456789', 'atencion@petcare.com');
call sp_AgregarProveedor('VetSupply', 'Avenida Central 4-12, Zona 12, Ciudad', '31567890', 'ventas@vetsupply.com');
call sp_AgregarProveedor('MedVet', 'Calle de Los Olivos 3-23, Villa Nueva', '31678901', 'contacto@medvet.com');
call sp_AgregarProveedor('VaccineCo', 'Carretera a El Salvador Km. 17, San Salvador', '31789012', 'ventas@vaccineco.com');
call sp_AgregarProveedor('ProVet', 'Calle de la Paz 14-32, Ciudad', '31890123', 'info@provet.com');
call sp_AgregarProveedor('PetMed', 'Avenida Libertador 8-4, Zona 5, Ciudad', '31901234', 'atencion@petmed.com');
call sp_AgregarProveedor('AnimalCare', 'Calle de Los Árboles 10-6, Ciudad', '32012345', 'contacto@animalcare.com');
call sp_AgregarProveedor('Zootecnica', 'Calle Las Palmas 15-9, Zona 7, Ciudad', '32123456', 'info@zootecnica.com');
call sp_AgregarProveedor('VetTech', 'Avenida Las Flores 20-12, Ciudad', '32234567', 'ventas@vettech.com');
call sp_AgregarProveedor('PetHealth', 'Calle del Sol 11-20, Ciudad', '32345678', 'contacto@pethealth.com');
call sp_AgregarProveedor('AnimalPro', 'Avenida El Castillo 22-18, Antigua Guatemala', '32456789', 'info@animalpro.com');
call sp_AgregarProveedor('MedicVet', 'Calle 6, Zona 14, Ciudad', '32567890', 'ventas@medicvet.com');


-- LISTAR PROVEEDORES
Delimiter $$
create procedure sp_ListarProveedores ()
    begin
        select * from Proveedores;
    end$$
Delimiter ;
call sp_ListarProveedores();
-- ELIMINAR PROVEEDOR
Delimiter $$
create procedure sp_EliminarProveedor (
    in codProveedor int)
    begin
        delete from Proveedores where codigoProveedor = codProveedor;
    end$$
Delimiter ;
call sp_EliminarProveedor(1);

-- BUSCAR PROVEEDOR
Delimiter $$
create procedure sp_BuscarProveedor (
    in codProveedor int)
    begin
        select * from Proveedores where codigoProveedor = codProveedor;
    end$$
Delimiter ;
call sp_BuscarProveedor(2);

-- EDITAR PROVEEDOR
Delimiter $$
create procedure sp_EditarProveedor (
    in codProveedor int,
    in nomProveedor varchar(100),
    in direcProveedor varchar(255),
    in telefProveedor varchar(8),
    in corProveedor varchar(100))
    begin
        update Proveedores set nombreProveedor = nomProveedor, direccionProveedor = direcProveedor, 
			telefonoProveedor = telefProveedor, correoProveedor = corProveedor
				where codigoProveedor = codProveedor;
    end$$
Delimiter ;
call sp_EditarProveedor(1, 'Pedigree', 'Zona 1, Proceres', '24340648', 'pedigree@gmail.com');

-- PROCEDIMIENTOS ALMACENADOS (VACUNAS) -------------------------
-- AGREGAR VACUNA
Delimiter $$
create procedure sp_AgregarVacuna (
    in nombreVacuna varchar(100),
    in descripcion varchar(255),
    in dosis varchar(100),
    in frecuenciaMeses int)
    begin
        insert into Vacunas (nombreVacuna, descripcion, dosis, frecuenciaMeses)
            values (nombreVacuna, descripcion, dosis, frecuenciaMeses);
    end$$
Delimiter ;
call sp_AgregarVacuna('Rabia', 'Vacuna contra la rabia, prevención de infección viral.', '1 dosis', 12);
call sp_AgregarVacuna('Parvovirus', 'Vacuna contra el parvovirus, enfermedad viral grave.', '2 dosis', 6);
call sp_AgregarVacuna('Hepatitis Canina', 'Vacuna contra la hepatitis viral canina.', '1 dosis', 12);
call sp_AgregarVacuna('Leptospirosis', 'Vacuna contra la leptospirosis, infección bacteriana.', '1 dosis', 6);
call sp_AgregarVacuna('Distemper', 'Vacuna contra el moquillo canino.', '2 dosis', 12);
call sp_AgregarVacuna('Adenovirus Canino', 'Vacuna contra adenovirus tipo 1 y 2.', '1 dosis', 12);
call sp_AgregarVacuna('Coronavirus Canino', 'Vacuna contra el coronavirus canino.', '1 dosis', 12);
call sp_AgregarVacuna('Feline Leukemia', 'Vacuna contra leucemia felina, una enfermedad viral grave.', '2 dosis', 12);
call sp_AgregarVacuna('Tos de perrera', 'Vacuna para prevenir la tos de perrera.', '1 dosis', 6);
call sp_AgregarVacuna('Toxoplasmosis', 'Vacuna contra la toxoplasmosis en gatos.', '1 dosis', 12);
call sp_AgregarVacuna('Tricomoniasis', 'Vacuna contra la tricomoniasis en gatos.', '1 dosis', 12);
call sp_AgregarVacuna('FIP (Peritonitis Infecciosa Felina)', 'Vacuna contra FIP, enfermedad mortal en gatos.', '2 dosis', 12);
call sp_AgregarVacuna('Rinotraqueítis Felina', 'Vacuna contra rinotraqueítis viral felina.', '1 dosis', 6);
call sp_AgregarVacuna('Leishmaniasis', 'Vacuna contra la leishmaniasis canina.', '1 dosis', 12);

-- LISTAR VACUNAS
Delimiter $$
create procedure sp_ListarVacunas ()
    begin
        select * from Vacunas;
    end$$
Delimiter ;
call sp_ListarVacunas();

-- ELIMINAR VACUNA
Delimiter $$
create procedure sp_EliminarVacuna (
    in codVacuna int)
    begin
        delete from Vacunas where codigoVacuna = codVacuna;
    end$$
Delimiter ;
call sp_EliminarVacuna(1);

-- BUSCAR VACUNA
Delimiter $$
create procedure sp_BuscarVacuna (
    in codVacuna int)
    begin
        select * from Vacunas where codigoVacuna = codVacuna;
    end$$
Delimiter ;
call sp_BuscarVacuna(1);

-- EDITAR VACUNA
Delimiter $$
create procedure sp_EditarVacuna (
    in codVacuna int,
    in nomVacuna varchar(100),
    in descVacuna varchar(255),
    in dosisVacuna varchar(100),
    in frecVacuna int)
    begin
        update Vacunas set nombreVacuna = nomVacuna, descripcion = descVacuna, dosis = dosisVacuna, frecuenciaMeses = frecVacuna
			where codigoVacuna = codVacuna;
    end$$
Delimiter ;
call sp_EditarVacuna(1, 'Distemper', 'La vacuna contra el distemper protege a los perros del moquillo canino, una enfermedad viral grave.', '1 ml por inyección', 4);

-- PROCEDIMIENTOS ALMACENADOS (EMPLEADOS) -------------------------
-- AGREGAR EMPLEADO
Delimiter $$
create procedure sp_AgregarEmpleado (
    in nombreEmpleado varchar(50),
    in apellidoEmpleado varchar(50),
    in cargo varchar(50),
    in telefonoEmpleado varchar(20),
    in correoEmpleado varchar(100))
    begin
        insert into Empleados (nombreEmpleado, apellidoEmpleado, cargo, telefonoEmpleado, correoEmpleado)
            values (nombreEmpleado, apellidoEmpleado, cargo, telefonoEmpleado, correoEmpleado);
    end$$
Delimiter ;
call sp_AgregarEmpleado('Caelia', 'Yasaka', 'Veterinaria', '20098765', 'cyasaka@gmail.com');
call sp_AgregarEmpleado('Paola', 'Pineda', 'Recepcionista', '03123456', 'ppineda@gmail.com');
call sp_AgregarEmpleado('Miguel', 'Serrano', 'Gerente', '12234567', 'mserrano@gmail.com');
call sp_AgregarEmpleado('Ana', 'Martínez', 'Veterinario', '55345678', 'amartinez@gmail.com');
call sp_AgregarEmpleado('Raúl', 'Hernández', 'Asistente Veterinario', '10456789', 'rhernandez@gmail.com');
call sp_AgregarEmpleado('Carla', 'Castro', 'Administrativa', '17567890', 'ccastro@gmail.com');
call sp_AgregarEmpleado('Sofía', 'Ríos', 'Recepcionista', '33678901', 'srios@gmail.com');
call sp_AgregarEmpleado('Roberto', 'Pérez', 'Veterinario', '41789012', 'rperez@gmail.com');
call sp_AgregarEmpleado('Verónica', 'López', 'Veterinario', '11890123', 'vlopez@gmail.com');
call sp_AgregarEmpleado('Eduardo', 'Gutiérrez', 'Gerente', '77901234', 'egutierrez@gmail.com');
call sp_AgregarEmpleado('Sandra', 'Ramírez', 'Veterinario', '44012345', 'sramirez@gmail.com');
call sp_AgregarEmpleado('Carlos', 'Jiménez', 'Veterinario', '01123456', 'cjimenez@gmail.com');
call sp_AgregarEmpleado('Juan', 'Torres', 'Asistente Veterinario', '13234567', 'jtorres@gmail.com');
call sp_AgregarEmpleado('Paola', 'Vásquez', 'Administrativa', '23345678', 'pvasquez@gmail.com');
call sp_AgregarEmpleado('Felipe', 'Sánchez', 'Veterinario', '78456789', 'fsanchez@gmail.com');

-- LISTAR EMPLEADOS
Delimiter $$
create procedure sp_ListarEmpleados ()
    begin
        select * from Empleados;
    end$$
Delimiter ;
call sp_ListarEmpleados();

-- ELIMINAR EMPLEADO
Delimiter $$
create procedure sp_EliminarEmpleado (
    in codEmpleado int)
    begin
        delete from Empleados where codigoEmpleado = codEmpleado;
    end$$
Delimiter ;
call sp_EliminarEmpleado(0);

-- BUSCAR EMPLEADO
Delimiter $$
create procedure sp_BuscarEmpleado (
    in codEmpleado int)
    begin
        select * from Empleados where codigoEmpleado = codEmpleado;
    end$$
Delimiter ;
call sp_BuscarEmpleado(1);

-- EDITAR EMPLEADO
Delimiter $$
create procedure sp_EditarEmpleado (
    in codEmpleado int,
    in nomEmpleado varchar(50),
    in apelEmpleado varchar(50),
    in cargoEmpleado varchar(50),
    in telefEmpleado varchar(20),
    in correoEmpleado varchar(100))
    begin
        update Empleados set nombreEmpleado = nomEmpleado, apellidoEmpleado = apelEmpleado, cargo = cargoEmpleado, 
            telefonoEmpleado = telefEmpleado, correoEmpleado = correoEmpleado
				where codigoEmpleado = codEmpleado;
    end$$
Delimiter ;
call sp_EditarEmpleado(1, 'Rodrigo', 'del Valle', 'Médico', '13202810', 'rdelvalle-2025001@kinal.edu.gt');

-- PROCEDIMIENTOS ALMACENADOS (MASCOTAS) -------------------------
-- AGREGAR MASCOTA
Delimiter $$
create procedure sp_AgregarMascota (
	in nomMascota varchar(50),
	in especieMascota varchar(30),
	in razaMascota varchar(50),
	in sexoMascota enum('Macho','Hembra'),
	in fechaNac date,
	in colorMascota varchar(30),
	in peso decimal(5,2),
	in codCliente int
)
begin
	insert into Mascotas(nombreMascota, especie, raza, sexo, fechaNacimiento, color, pesoActualKg, codigoCliente)
	values (nomMascota, especieMascota, razaMascota, sexoMascota, fechaNac, colorMascota, peso, codCliente);
end$$
Delimiter ;
call sp_AgregarMascota('Marilo', 'Perro', 'Snauzer', 'Macho', '2025-11-10', 'Gris', 3.90, 2);
call sp_AgregarMascota('Mia', 'Gato', 'Persa', 'Hembra', '2021-11-10', 'Blanco', 3.80, 2);
call sp_AgregarMascota('Bobby', 'Perro', 'Beagle', 'Macho', '2022-04-25', 'Marrón', 18.20, 3);
call sp_AgregarMascota('Simba', 'Gato', 'Maine Coon', 'Macho', '2022-02-05', 'Naranja', 7.40, 4);
call sp_AgregarMascota('Coco', 'Perro', 'Chihuahua', 'Hembra', '2021-12-15', 'Café', 4.50, 5);
call sp_AgregarMascota('Luna', 'Gato', 'Siames', 'Hembra', '2022-06-20', 'Gris', 5.10, 6);
call sp_AgregarMascota('Max', 'Perro', 'Doberman', 'Macho', '2022-07-10', 'Negro', 30.00, 7);
call sp_AgregarMascota('Leo', 'Gato', 'Bengalí', 'Macho', '2021-09-30', 'Marrón con manchas', 6.60, 8);
call sp_AgregarMascota('Bella', 'Perro', 'Poodle', 'Hembra', '2021-05-15', 'Blanco', 8.30, 9);
call sp_AgregarMascota('Toby', 'Gato', 'Maine Coon', 'Macho', '2021-12-25', 'Gris', 7.90, 10);
call sp_AgregarMascota('Rocky', 'Perro', 'Pitbull', 'Macho', '2021-10-05', 'Azul', 27.50, 11);
call sp_AgregarMascota('Sasha', 'Gato', 'Persa', 'Hembra', '2022-03-12', 'Blanco', 4.20, 12);
call sp_AgregarMascota('Duke', 'Perro', 'Rottweiler', 'Macho', '2022-08-15', 'Negro con marrón', 32.10, 13);
call sp_AgregarMascota('Milo', 'Gato', 'Sphynx', 'Macho', '2021-07-28', 'Sin pelo', 4.50, 14);
call sp_AgregarMascota('Chloe', 'Perro', 'Golden Retriever', 'Hembra', '2022-09-04', 'Dorado', 28.20, 15);

-- LISTAR MASCOTAS
Delimiter $$
create procedure sp_ListarMascotas()
begin
	select * from Mascotas;
end$$
Delimiter ;
call sp_ListarMascotas();

-- ELIMINAR MASCOTA
Delimiter $$
create procedure sp_EliminarMascota(
	in codMascota int
)
begin
	delete from Mascotas where codigoMascota = codMascota;
end$$
Delimiter ;
call sp_EliminarMascota(0);

-- BUSCAR MASCOTA
Delimiter $$
create procedure sp_BuscarMascota(
	in codMascota int
)
begin
	select * from Mascotas where codigoMascota = codMascota;
end$$
Delimiter ;
call sp_BuscarMascota(1);

-- EDITAR MASCOTA
Delimiter $$
create procedure sp_EditarMascota (
	in codMascota int,
	in nomMascota varchar(50),
	in especieMascota varchar(30),
	in razaMascota varchar(50),
	in sexoMascota enum('Macho','Hembra'),
	in fechaNac date,
	in colorMascota varchar(30),
	in peso decimal(5,2),
	in codCliente int
)
begin
	update Mascotas set nombreMascota = nomMascota, especie = especieMascota, raza = razaMascota, sexo = sexoMascota,
		fechaNacimiento = fechaNac, color = colorMascota, pesoActualKg = peso, codigoCliente = codCliente
			where codigoMascota = codMascota;
end$$
Delimiter ;
call sp_EditarMascota(2, 'Mia', 'Gato', 'Persa', 'Hembra', '2021-11-10', 'Blanco', 3.80, 2);

-- PROCEDIMIENTOS ALMACENADOS (CONSULTAS) -------------------------
-- AGREGAR CONSULTA
Delimiter $$
create procedure sp_AgregarConsulta (
	in fechConsulta datetime,
    in motivConsulta varchar(255),
    in diagConsulta varchar(255),
    in obsConsulta varchar(255),
    in codMascota int,
    in codVeterinario int)
	begin
		insert into Consultas (fechaConsulta, motivo, diagnostico, observaciones, codigoMascota, codigoVeterinario)
			values (fechConsulta, motivConsulta, diagConsulta, obsConsulta, codMascota, codVeterinario);
	end$$
Delimiter ;
call sp_AgregarConsulta('2023-04-01 10:00:00', 'Revisión general', 'Todo en orden', 'Ninguna', 2, 2);
call sp_AgregarConsulta('2023-05-03 14:30:00', 'Vacunación', 'Vacuna contra rabia aplicada', 'Requiere reposo', 2, 2);
call sp_AgregarConsulta('2023-06-10 16:00:00', 'Revisión de pelaje', 'Enfermedad leve en piel', 'Usar shampoo especial', 3, 3);
call sp_AgregarConsulta('2023-07-18 11:15:00', 'Chequeo de peso', 'Sobrepeso leve', 'Control de dieta', 4, 4);
call sp_AgregarConsulta('2023-08-22 13:45:00', 'Urgencias', 'Accidente con herida', 'Cuidado de la herida', 5, 5);
call sp_AgregarConsulta('2023-09-02 09:30:00', 'Vacunación', 'Vacuna contra parvovirus aplicada', 'Requiere reposo', 6, 6);
call sp_AgregarConsulta('2023-10-05 15:00:00', 'Control de peso', 'Peso adecuado', 'Control mensual', 7, 7);
call sp_AgregarConsulta('2023-11-11 12:00:00', 'Chequeo ocular', 'No presenta infecciones', 'Revisión anual', 8, 8);
call sp_AgregarConsulta('2023-12-20 17:00:00', 'Revisión dental', 'Sin problemas dentales', 'Mantener higiene', 9, 9);
call sp_AgregarConsulta('2023-01-10 14:00:00', 'Urgencias', 'Infección en la piel', 'Antibióticos', 10, 10);
call sp_AgregarConsulta('2023-02-13 10:30:00', 'Chequeo general', 'Problemas en las patas', 'Control de actividad', 11, 11);
call sp_AgregarConsulta('2023-03-15 13:00:00', 'Revisión de dientes', 'Casi sin sarro', 'Limpiar dientes', 12, 12);
call sp_AgregarConsulta('2023-04-20 11:30:00', 'Vacunación', 'Vacuna contra leptospirosis aplicada', 'Reposo', 13, 13);
call sp_AgregarConsulta('2023-05-25 09:00:00', 'Control de salud', 'Salud perfecta', 'Revisión anual', 14, 14);
call sp_AgregarConsulta('2023-06-30 18:00:00', 'Chequeo de estrés', 'Estrés moderado', 'Terapia relajante', 15, 15);

-- LISTAR CONSULTAS
Delimiter $$
create procedure sp_ListarConsultas ()
	begin
		select * from Consultas;
	end$$
Delimiter ;
call sp_ListarConsultas();

-- ELIMINAR CONSULTA
Delimiter $$
create procedure sp_EliminarConsulta (
	in codConsulta int)
	begin
		delete from Consultas where codigoConsulta = codConsulta;
	end$$
Delimiter ;
call sp_EliminarConsulta(0);

-- BUSCAR CONSULTA
Delimiter $$
create procedure sp_BuscarConsulta (
	in codConsulta int)
	begin
		select * from Consultas where codigoConsulta = codConsulta;
	end$$
Delimiter ;
call sp_BuscarConsulta(1);

-- EDITAR CONSULTA
Delimiter $$
create procedure sp_EditarConsulta (
	in codConsulta int,
	in fechConsulta datetime,
    in motivConsulta varchar(255),
    in diagConsulta varchar(255),
    in obsConsulta varchar(255),
    in codMascota int,
    in codVeterinario int)
	begin
		update Consultas set fechaConsulta = fechConsulta, motivo = motivConsulta, diagnostico = diagConsulta, 
			observaciones = obsConsulta, codigoMascota = codMascota, codigoVeterinario = codVeterinario 
				where codigoConsulta = codConsulta;
	end$$
Delimiter ;
call sp_EditarConsulta(2, '2023-04-01 10:00:00', 'Revisión general', 'Todo en orden', 'Ninguna', 2, 2);

-- PROCEDIMIENTOS ALMACENADOS (TRATAMIENTOS) -------------------------
-- AGREGAR TRATAMIENTO
Delimiter $$
create procedure sp_AgregarTratamiento (
	in descTratamiento varchar(255),
	in fechaInicio date,
	in fechaFin date,
	in medsIndicados varchar(255),
	in codConsulta int)
	begin
		insert into Tratamientos (descripcion, fechaInicio, fechaFin, medicamentosIndicados, codigoConsulta)
			values (descTratamiento, fechaInicio, fechaFin, medsIndicados, codConsulta);
	end$$
Delimiter ;
call sp_AgregarTratamiento('Tratamiento antibiótico para infección de piel', '2023-04-02', '2023-04-12', 'Amoxicilina 500mg cada 8 horas', 2);
call sp_AgregarTratamiento('Terapia de desparasitación', '2023-05-04', '2023-05-14', 'Albendazol 100mg por 5 días', 2);
call sp_AgregarTratamiento('Tratamiento para dermatitis', '2023-06-11', '2023-06-18', 'Clorfeniramina y shampoo medicado', 3);
call sp_AgregarTratamiento('Dieta especial para control de peso', '2023-07-19', '2023-08-19', 'Alimento bajo en grasa', 4);
call sp_AgregarTratamiento('Antibióticos por mordida', '2023-08-23', '2023-09-02', 'Cefalexina 500mg cada 12 horas', 5);
call sp_AgregarTratamiento('Rehidratación y vitaminas', '2023-09-03', '2023-09-07', 'Suero oral y complejo B', 6);
call sp_AgregarTratamiento('Control de ansiedad', '2023-10-06', '2023-11-06', 'Fluoxetina 10mg diarios', 7);
call sp_AgregarTratamiento('Limpieza ocular y gotas', '2023-11-12', '2023-11-19', 'Gotas oftálmicas cada 8h', 8);
call sp_AgregarTratamiento('Higiene bucal y enjuague', '2023-12-21', '2023-12-28', 'Enjuague y cepillado', 9);
call sp_AgregarTratamiento('Tratamiento para sarna', '2023-01-11', '2023-01-25', 'Ivermectina tópica', 10);
call sp_AgregarTratamiento('Terapia para displasia', '2023-02-14', '2023-03-14', 'Condroprotectores y antiinflamatorios', 11);
call sp_AgregarTratamiento('Profilaxis dental', '2023-03-16', '2023-03-16', 'Limpieza profunda', 12);
call sp_AgregarTratamiento('Tratamiento leptospirosis', '2023-04-21', '2023-04-30', 'Doxiciclina 100mg cada 12h', 13);
call sp_AgregarTratamiento('Revisión nutricional', '2023-05-26', '2023-06-26', 'Cambio de dieta y suplementos', 14);
call sp_AgregarTratamiento('Control de estrés', '2023-07-01', '2023-07-15', 'Feromonas y ejercicios', 15);

-- LISTAR TRATAMIENTOS
Delimiter $$
create procedure sp_ListarTratamientos ()
	begin
		select * from Tratamientos;
	end$$
Delimiter ;
call sp_ListarTratamientos();

-- ELIMINAR TRATAMIENTO
Delimiter $$
create procedure sp_EliminarTratamiento (
	in codTratamiento int)
	begin
		delete from Tratamientos where codigoTratamiento = codTratamiento;
	end$$
Delimiter ;
call sp_EliminarTratamiento(0);

-- BUSCAR TRATAMIENTO
Delimiter $$
create procedure sp_BuscarTratamiento (
	in codTratamiento int)
	begin
		select * from Tratamientos where codigoTratamiento = codTratamiento;
	end$$
Delimiter ;
call sp_BuscarTratamiento(1);

-- EDITAR TRATAMIENTO
Delimiter $$
create procedure sp_EditarTratamiento (
	in codTratamiento int,
	in descTratamiento varchar(255),
	in fechaInicio date,
	in fechaFin date,
	in medsIndicados varchar(255),
	in codConsulta int)
	begin
		update Tratamientos set descripcion = descTratamiento, fechaInicio = fechaInicio, 
			fechaFin = fechaFin, medicamentosIndicados = medsIndicados, codigoConsulta = codConsulta
				where codigoTratamiento = codTratamiento;
	end$$
Delimiter ;
call sp_EditarTratamiento(2, 'Tratamiento antibiótico para infección de piel', '2023-04-02', '2023-04-12', 'Amoxicilina 500mg cada 8 horas', 2);

-- PROCEDIMIENTOS ALMACENADOS (CITAS) -------------------------
-- AGREGAR CITA
Delimiter $$ 
create procedure sp_AgregarCita (
	in fechaCita date,
	in horaCita time,
	in motivoCita varchar(255),
	in estadoCita enum('Pendiente','Completado','Cancelado'),
	in codMascota int,
	in codVeterinario int)
	begin
		insert into Citas (fechaCita, horaCita, motivo, estado, codigoMascota, codigoVeterinario)
			values (fechaCita, horaCita, motivoCita, estadoCita, codMascota, codVeterinario);
	end$$
Delimiter ;
call sp_AgregarCita('2023-04-01', '10:00:00', 'Chequeo anual', 'Pendiente', 2, 2);
call sp_AgregarCita('2023-05-03', '14:00:00', 'Vacunación', 'Completado', 2, 2);
call sp_AgregarCita('2023-06-11', '16:30:00', 'Revisión de piel', 'Completado', 3, 3);
call sp_AgregarCita('2023-07-18', '11:00:00', 'Control de peso', 'Pendiente', 4, 4);
call sp_AgregarCita('2023-08-22', '13:45:00', 'Emergencia', 'Completado', 5, 5);
call sp_AgregarCita('2023-09-02', '09:15:00', 'Vacunación', 'Completado', 6, 6);
call sp_AgregarCita('2023-10-05', '15:00:00', 'Revisión mensual', 'Pendiente', 7, 7);
call sp_AgregarCita('2023-11-11', '12:30:00', 'Chequeo ocular', 'Pendiente', 8, 8);
call sp_AgregarCita('2023-12-20', '17:45:00', 'Revisión dental', 'Completado', 9, 9);
call sp_AgregarCita('2023-01-10', '14:20:00', 'Emergencia', 'Completado', 10, 10);
call sp_AgregarCita('2023-02-13', '10:15:00', 'Chequeo general', 'Completado', 11, 11);
call sp_AgregarCita('2023-03-15', '13:00:00', 'Limpieza dental', 'Completado', 12, 12);
call sp_AgregarCita('2023-04-20', '11:45:00', 'Vacunación', 'Pendiente', 13, 13);
call sp_AgregarCita('2023-05-25', '09:00:00', 'Consulta anual', 'Pendiente', 14, 14);
call sp_AgregarCita('2023-06-30', '18:15:00', 'Revisión emocional', 'Completado', 15, 15);

-- LISTAR CITAS
Delimiter $$
create procedure sp_ListarCitas ()
	begin
		select * from Citas;
	end$$
Delimiter ;
call sp_ListarCitas();

-- ELIMINAR CITA
Delimiter $$
create procedure sp_EliminarCita (
	in codCita int)
	begin
		delete from Citas where codigoCita = codCita;
	end$$
Delimiter ;
call sp_EliminarCita(0);

-- BUSCAR CITA
Delimiter $$
create procedure sp_BuscarCita (
	in codCita int)
	begin
		select * from Citas where codigoCita = codCita;
	end$$
Delimiter ;
call sp_BuscarCita(1);

-- EDITAR CITA
Delimiter $$
create procedure sp_EditarCita (
	in codCita int,
	in fechaCita date,
	in horaCita time,
	in motivoCita varchar(255),
	in estadoCita enum('Pendiente','Completado','Cancelado'),
	in codMascota int,
	in codVeterinario int)
	begin
		update Citas set fechaCita = fechaCita, horaCita = horaCita, motivo = motivoCita,
			estado = estadoCita, codigoMascota = codMascota, codigoVeterinario = codVeterinario
				where codigoCita = codCita;
	end$$
Delimiter ;
call sp_EditarCita(2, '2023-04-01', '10:00:00', 'Chequeo anual', 'Pendiente', 2, 2);

-- PROCEDIMIENTOS ALMACENADOS (VACUNACIONES) -------------------------
-- AGREGAR VACUNACIÓN
Delimiter $$
create procedure sp_AgregarVacunacion (
	in fechaAplicacion date,
	in observaciones varchar(250),
	in codMascota int,
	in codVacuna int,
	in codVeterinario int)
	begin
		insert into Vacunaciones (fechaAplicacion, observaciones, codigoMascota, codigoVacuna, codigoVeterinario)
			values (fechaAplicacion, observaciones, codMascota, codVacuna, codVeterinario);
	end$$
Delimiter ;
call sp_AgregarVacunacion('2023-04-01', 'Sin efectos adversos', 2, 2, 2);
call sp_AgregarVacunacion('2023-05-03', 'Leve somnolencia post vacuna', 2, 2, 2);
call sp_AgregarVacunacion('2023-06-11', 'Reacción leve en el sitio de aplicación', 3, 3, 3);
call sp_AgregarVacunacion('2023-07-18', 'Vacunación exitosa', 4, 4, 4);
call sp_AgregarVacunacion('2023-08-22', 'Vacuna de emergencia aplicada', 5, 5, 5);
call sp_AgregarVacunacion('2023-09-02', 'Seguimiento necesario', 6, 6, 6);
call sp_AgregarVacunacion('2023-10-05', 'Aplicación sin contratiempos', 7, 7, 7);
call sp_AgregarVacunacion('2023-11-11', 'Revisión programada en 3 meses', 8, 8, 8);
call sp_AgregarVacunacion('2023-12-20', 'Segunda dosis requerida', 9, 9, 9);
call sp_AgregarVacunacion('2023-01-10', 'Paciente respondió bien', 10, 10, 10);
call sp_AgregarVacunacion('2023-02-13', 'No se reportan anomalías', 11, 11, 11);
call sp_AgregarVacunacion('2023-03-15', 'Aplicación sin problemas', 12, 12, 12);
call sp_AgregarVacunacion('2023-04-20', 'Requiere seguimiento', 13, 13, 13);
call sp_AgregarVacunacion('2023-05-25', 'Observaciones positivas', 14, 14, 14);
call sp_AgregarVacunacion('2023-06-30', 'Bien tolerada', 14, 14, 14);

-- LISTAR VACUNACIONES
Delimiter $$
create procedure sp_ListarVacunaciones ()
	begin
		select * from Vacunaciones;
	end$$
Delimiter ;
call sp_ListarVacunaciones();

-- ELIMINAR VACUNACIÓN
Delimiter $$
create procedure sp_EliminarVacunacion (
	in codVacunacion int)
	begin
		delete from Vacunaciones where codigoVacunacion = codVacunacion;
	end$$
Delimiter ;
call sp_EliminarVacunacion(0);

-- BUSCAR VACUNACIÓN
Delimiter $$
create procedure sp_BuscarVacunacion (
	in codVacunacion int)
	begin
		select * from Vacunaciones where codigoVacunacion = codVacunacion;
	end$$
Delimiter ;
call sp_BuscarVacunacion(1);

-- EDITAR VACUNACIÓN
Delimiter $$
create procedure sp_EditarVacunacion (
	in codVacunacion int,
	in fechaAplicacion date,
	in observaciones varchar(250),
	in codMascota int,
	in codVacuna int,
	in codVeterinario int)
	begin
		update Vacunaciones set fechaAplicacion = fechaAplicacion, observaciones = observaciones, 
			codigoMascota = codMascota, codigoVacuna = codVacuna, codigoVeterinario = codVeterinario
				where codigoVacunacion = codVacunacion;
	end$$
Delimiter ;
call sp_EditarVacunacion(2, '2023-04-01', 'Sin efectos adversos', 2, 2, 2);

-- PROCEDIMIENTOS ALMACENADOS (MEDICAMENTOS) -------------------------
-- AGREGAR MEDICAMENTO
Delimiter $$
create procedure sp_AgregarMedicamento (
	in nomMedicamento varchar(100),
	in descripcion varchar(250),
	in stock int,
	in precioUnitario decimal(10,2),
	in fechaVenc date,
	in codProveedor int)
	begin
		insert into Medicamentos (nombreMedicamento, descripcion, stock, precioUnitario, fechaVencimiento, codigoProveedor)
			values (nomMedicamento, descripcion, stock, precioUnitario, fechaVenc, codProveedor);
	end$$
Delimiter ;
call sp_AgregarMedicamento('Amoxicilina 500mg', 'Antibiótico de amplio espectro', 100, 3.50, '2025-06-30', 2);
call sp_AgregarMedicamento('Ivermectina', 'Antiparasitario de amplio uso', 150, 4.75, '2025-11-15', 2);
call sp_AgregarMedicamento('Clorfeniramina', 'Antihistamínico para alergias', 200, 1.25, '2025-08-01', 3);
call sp_AgregarMedicamento('Doxiciclina', 'Antibiótico para infecciones bacterianas', 120, 2.80, '2026-01-01', 4);
call sp_AgregarMedicamento('Fluoxetina 10mg', 'Tratamiento para ansiedad', 90, 5.00, '2026-03-10', 5);
call sp_AgregarMedicamento('Shampoo medicado', 'Tratamiento de piel y pelo', 75, 6.90, '2025-12-05', 6);
call sp_AgregarMedicamento('Vitaminas Complejo B', 'Suplemento vitamínico', 100, 1.10, '2025-07-01', 2);
call sp_AgregarMedicamento('Condroprotector Plus', 'Mejorar articulaciones', 60, 7.80, '2026-04-15', 3);
call sp_AgregarMedicamento('Enjuague bucal veterinario', 'Limpieza dental', 80, 3.20, '2025-10-25', 4);
call sp_AgregarMedicamento('Albendazol 100mg', 'Antiparasitario', 130, 1.90, '2026-06-01', 5);
call sp_AgregarMedicamento('Suero oral veterinario', 'Rehidratación', 110, 2.50, '2025-09-10', 9);
call sp_AgregarMedicamento('Gotas oftálmicas', 'Uso ocular', 95, 4.20, '2025-08-20', 2);
call sp_AgregarMedicamento('Feromonas relajantes', 'Control de estrés', 50, 8.90, '2026-02-01', 3);
call sp_AgregarMedicamento('Limpieza dental avanzada', 'Kit dental', 45, 6.50, '2025-11-11', 4);
call sp_AgregarMedicamento('Comida baja en grasa', 'Dieta especial', 70, 12.00, '2025-12-30', 5);

-- LISTAR MEDICAMENTOS
Delimiter $$
create procedure sp_ListarMedicamentos ()
	begin
		select * from Medicamentos;
	end$$
Delimiter ;
call sp_ListarMedicamentos();

-- ELIMINAR MEDICAMENTO
Delimiter $$
create procedure sp_EliminarMedicamento (
	in codMedicamento int)
	begin
		delete from Medicamentos where codigoMedicamento = codMedicamento;
	end$$
Delimiter ;
call sp_EliminarMedicamento(0);

-- BUSCAR MEDICAMENTO
Delimiter $$
create procedure sp_BuscarMedicamento (
	in codMedicamento int)
	begin
		select * from Medicamentos where codigoMedicamento = codMedicamento;
	end$$
Delimiter ;
call sp_BuscarMedicamento(1);

-- EDITAR MEDICAMENTO
Delimiter $$
create procedure sp_EditarMedicamento (
	in codMedicamento int,
	in nomMedicamento varchar(100),
	in descripcion varchar(250),
	in stock int,
	in precioUnitario decimal(10,2),
	in fechaVenc date,
	in codProveedor int)
	begin
		update Medicamentos set nombreMedicamento = nomMedicamento, descripcion = descripcion, stock = stock, 
			precioUnitario = precioUnitario, fechaVencimiento = fechaVenc, codigoProveedor = codProveedor
				where codigoMedicamento = codMedicamento;
	end$$
Delimiter ;
call sp_EditarMedicamento(2, 'Amoxicilina 500mg', 'Antibiótico de amplio espectro', 100, 3.50, '2025-06-30', 2);

-- PROCEDIMIENTOS ALMACENADOS (RECETAS) -------------------------
-- AGREGAR RECETA
Delimiter $$
create procedure sp_AgregarReceta (
	in dosis varchar(100),
	in frecuencia varchar(100),
	in duracionDias int,
	in indicaciones varchar(255),
	in codConsulta int,
	in codMedicamento int)
	begin
		insert into Recetas (dosis, frecuencia, duracionDias, indicaciones, codigoConsulta, codigoMedicamento)
			values (dosis, frecuencia, duracionDias, indicaciones, codConsulta, codMedicamento);
	end$$
Delimiter ;
call sp_AgregarReceta('500mg cada 8h', '3 veces al día', 10, 'Administrar con alimentos', 2, 2);
call sp_AgregarReceta('1 tableta diaria', '1 vez al día', 7, 'Evitar exposición al sol', 2, 2);
call sp_AgregarReceta('Aplicar tópicamente', '2 veces al día', 14, 'No lamer la zona tratada', 3, 3);
call sp_AgregarReceta('1 cápsula diaria', '1 vez al día', 30, 'Acompañar con comida', 4, 4);
call sp_AgregarReceta('500mg cada 12h', '2 veces al día', 7, 'Mantener hidratación', 5, 5);
call sp_AgregarReceta('20ml diarios', '1 vez al día', 5, 'Administrar con jeringa', 6, 6);
call sp_AgregarReceta('10mg diarios', '1 vez al día', 21, 'No interrumpir el tratamiento', 7, 7);
call sp_AgregarReceta('1 gota en cada ojo', '3 veces al día', 5, 'Limpiar el ojo antes de aplicar', 8, 8);
call sp_AgregarReceta('Enjuagar después de comida', '2 veces al día', 7, 'No tragar el enjuague', 9, 9);
call sp_AgregarReceta('1 tableta cada 12h', '2 veces al día', 10, 'Evitar sobre dosificación', 10, 10);
call sp_AgregarReceta('Condroprotector 1 medida', '1 vez al día', 30, 'Mezclar con alimento', 11, 11);
call sp_AgregarReceta('Aplicar 1 vez semanal', '1 vez a la semana', 4, 'Bañar previamente', 12, 12);
call sp_AgregarReceta('Antibiótico 100mg', '2 veces al día', 10, 'No suspender sin indicación', 13, 13);
call sp_AgregarReceta('Feromonas ambientales', 'Diario', 15, 'Usar en área donde duerme', 14, 14);
call sp_AgregarReceta('Alimento especial 100g', '2 veces al día', 30, 'Evitar otros alimentos', 15, 15);

-- LISTAR RECETAS
Delimiter $$
create procedure sp_ListarRecetas ()
	begin
		select * from Recetas;
	end$$
Delimiter ;
call sp_ListarRecetas();

-- ELIMINAR RECETA
Delimiter $$
create procedure sp_EliminarReceta (
	in codReceta int)
	begin
		delete from Recetas where codigoReceta = codReceta;
	end$$
Delimiter ;
call sp_EliminarReceta(0);

-- BUSCAR RECETA
Delimiter $$
create procedure sp_BuscarReceta (
	in codReceta int)
	begin
		select * from Recetas where codigoReceta = codReceta;
	end$$
Delimiter ;
call sp_BuscarReceta(1);

-- EDITAR RECETA
Delimiter $$
create procedure sp_EditarReceta (
	in codReceta int,
	in dosis varchar(100),
	in frecuencia varchar(100),
	in duracionDias int,
	in indicaciones varchar(255),
	in codConsulta int,
	in codMedicamento int)
	begin
		update Recetas set dosis = dosis, frecuencia = frecuencia, duracionDias = duracionDias, 
			indicaciones = indicaciones, codigoConsulta = codConsulta, codigoMedicamento = codMedicamento
				where codigoReceta = codReceta;
end$$
Delimiter ;
call sp_EditarReceta(2, '500mg cada 8h', '3 veces al día', 10, 'Administrar con alimentos', 2, 2);

-- PROCEDIMIENTOS ALMACENADOS (FACTURAS) -------------------------
-- AGREGAR FACTURA
Delimiter $$
create procedure sp_AgregarFactura (
	in fechaEmision date,
	in total decimal(10,2),
	in metodoPago enum('Efectivo', 'Tarjeta', 'Transferencia'),
	in codCliente int,
	in codEmpleado int)
	begin
		insert into Facturas (fechaEmision, total, metodoPago, codigoCliente, codigoEmpleado)
			values (fechaEmision, total, metodoPago, codCliente, codEmpleado);
	end$$
Delimiter ;
call sp_AgregarFactura('2023-04-01', 150.00, 'Efectivo', 2, 2);
call sp_AgregarFactura('2023-05-04', 200.00, 'Tarjeta', 2, 2);
call sp_AgregarFactura('2023-06-12', 175.50, 'Transferencia', 3, 3);
call sp_AgregarFactura('2023-07-19', 220.00, 'Tarjeta', 4, 4);
call sp_AgregarFactura('2023-08-23', 300.00, 'Efectivo', 5, 5);
call sp_AgregarFactura('2023-09-03', 90.00, 'Transferencia', 6, 6);
call sp_AgregarFactura('2023-10-06', 250.75, 'Tarjeta', 7, 7);
call sp_AgregarFactura('2023-11-12', 180.00, 'Efectivo', 8, 8);
call sp_AgregarFactura('2023-12-21', 130.00, 'Tarjeta', 9, 9);
call sp_AgregarFactura('2023-01-11', 210.00, 'Transferencia', 10, 10);
call sp_AgregarFactura('2023-02-15', 95.00, 'Efectivo', 11, 11);
call sp_AgregarFactura('2023-03-16', 155.00, 'Tarjeta', 12, 12);
call sp_AgregarFactura('2023-04-22', 115.00, 'Transferencia', 13, 13);
call sp_AgregarFactura('2023-05-27', 310.00, 'Efectivo', 14, 14);
call sp_AgregarFactura('2023-07-02', 135.00, 'Tarjeta', 15, 15);

-- LISTAR FACTURAS
Delimiter $$
create procedure sp_ListarFacturas ()
	begin
		select * from Facturas;
	end$$
Delimiter ;
call sp_ListarFacturas();

-- ELIMINAR FACTURA
Delimiter $$
create procedure sp_EliminarFactura (
	in codFactura int)
	begin
		delete from Facturas where codigoFactura = codFactura;
	end$$
Delimiter ;
call sp_EliminarFactura(0);

-- BUSCAR FACTURA
Delimiter $$
create procedure sp_BuscarFactura (
	in codFactura int)
	begin
		select * from Facturas where codigoFactura = codFactura;
	end$$
Delimiter ;
call sp_BuscarFactura(1);

-- EDITAR FACTURA
Delimiter $$
create procedure sp_EditarFactura (
	in codFactura int,
	in fechaEmision date,
	in total decimal(10,2),
	in metodoPago enum('Efectivo', 'Tarjeta', 'Transferencia'),
	in codCliente int,
	in codEmpleado int)
begin
	update Facturas set fechaEmision = fechaEmision, total = total, metodoPago = metodoPago, 
		codigoCliente = codCliente, codigoEmpleado = codEmpleado
	where codigoFactura = codFactura;
end$$
Delimiter ;
call sp_EditarFactura(2, '2023-04-01', 150.00, 'Efectivo', 2, 2);

-- PROCEDIMIENTOS ALMACENADOS (COMPRAS) -------------------------
-- AGREGAR COMPRA
Delimiter $$
create procedure sp_AgregarCompra (
	in fechaCompra date,
	in total decimal(10,2),
	in detalle varchar(255),
	in codProveedor int)
	begin
		insert into Compras (fechaCompra, total, detalle, codigoProveedor)
			values (fechaCompra, total, detalle, codProveedor);
	end$$
Delimiter ;
call sp_AgregarCompra('2023-03-10', 1500.00, 'Compra de antibióticos y vitaminas', 2);
call sp_AgregarCompra('2023-04-15', 820.00, 'Shampoos y antiparasitarios', 2);
call sp_AgregarCompra('2023-05-20', 1250.50, 'Compra de material quirúrgico', 3);
call sp_AgregarCompra('2023-06-25', 980.00, 'Alimento especializado y suplementos', 4);
call sp_AgregarCompra('2023-07-30', 1600.00, 'Vacunas anuales', 5);
call sp_AgregarCompra('2023-08-10', 700.00, 'Feromonas y calmantes', 7);
call sp_AgregarCompra('2023-09-15', 890.00, 'Medicamentos para alergias y ojos', 2);
call sp_AgregarCompra('2023-10-20', 1400.00, 'Limpieza y desinfección', 3);
call sp_AgregarCompra('2023-11-25', 1100.00, 'Condroprotectores y calmantes', 4);
call sp_AgregarCompra('2023-12-30', 950.00, 'Reabastecimiento de antibióticos', 5);
call sp_AgregarCompra('2023-01-05', 1050.00, 'Vitaminas y alimento', 9);
call sp_AgregarCompra('2023-02-09', 770.00, 'Material de limpieza', 2);
call sp_AgregarCompra('2023-03-13', 1340.00, 'Reposición de stock general', 3);
call sp_AgregarCompra('2023-04-18', 1190.00, 'Suero y desparasitantes', 4);
call sp_AgregarCompra('2023-05-22', 1230.00, 'Tratamientos dermatológicos', 5);

-- LISTAR COMPRAS
Delimiter $$
create procedure sp_ListarCompras ()
	begin
		select * from Compras;
	end$$
Delimiter ;
call sp_ListarCompras();

-- ELIMINAR COMPRA
Delimiter $$
create procedure sp_EliminarCompra (
	in codCompra int)
	begin
		delete from Compras where codigoCompra = codCompra;
	end$$
Delimiter ;
call sp_EliminarCompra(0);

-- BUSCAR COMPRA
Delimiter $$
create procedure sp_BuscarCompra (
	in codCompra int)
	begin
		select * from Compras where codigoCompra = codCompra;
	end$$
Delimiter ;
call sp_BuscarCompra(1);

-- EDITAR COMPRA
Delimiter $$
create procedure sp_EditarCompra (
	in codCompra int,
	in fechaCompra date,
	in total decimal(10,2),
	in detalle varchar(255),
	in codProveedor int)
	begin
		update Compras set fechaCompra = fechaCompra, total = total, detalle = detalle, 
			codigoProveedor = codProveedor where codigoCompra = codCompra;
	end$$
Delimiter ;
call sp_EditarCompra(2, '2023-03-10', 1500.00, 'Compra de antibióticos y vitaminas', 2);

-- PROCEDIMIENTOS ALMACENADOS (USUARIOS) -------------------------
-- AGREGAR USUARIO
Delimiter $$
create procedure sp_AgregarUsuario (
	in nombreUsuario varchar(50),
	in contraseñaUsuario varchar(100),
    in nivelAcceso enum('Empleado', 'Administrador', 'Propietario')
    )
	begin
		insert into Usuarios (nombreUsuario, contraseñaUsuario, nivelAcceso)
			values (nombreUsuario, contraseñaUsuario, nivelAcceso);
	end$$
Delimiter ;
call sp_AgregarUsuario('jrealiquez-2021549@gmail.com', 'Caelia_as@1980', 'Propietario');
call sp_AgregarUsuario('pnoriega-2019054@gmail.com', 'Spider_Dog@2020', 'Administrador');
call sp_AgregarUsuario('cyasaka-2021064@gmail.com', 'Proxy_jr@6404', 'Propietario');

-- LISTAR COMPRAS
Delimiter $$
create procedure sp_ListarUsuarios ()
	begin
		select * from Usuarios;
	end$$
Delimiter ;
call sp_ListarUsuarios();

-- ELIMINAR COMPRA
Delimiter $$
create procedure sp_EliminarUsuario (
	in codUsuario int)
	begin
		delete from Usuarios where codigoUsuario = codUsuario;
	end$$
Delimiter ;
call sp_EliminarUsuario(4);

-- BUSCAR COMPRA
Delimiter $$
create procedure sp_BuscarUsuario (
	in codUsuario int)
	begin
		select * from Usuarios where codigoUsuario = codUsuario;
	end$$
Delimiter ;
call sp_BuscarUsuario(1);

-- EDITAR COMPRA
Delimiter $$
create procedure sp_EditarUsuario (
	in codUsuario int,
	in nomUsuario varchar(50),
	in contUsuario varchar(100),
    in nAcceso enum('Empleado', 'Administrador', 'Propietario')
    )
	begin
		update Usuarios set nombreUsuario = nomUsuario, contraseñaUsuario = contUsuario, nivelAcceso = nAcceso
			where codigoUsuario = codUsuario;
	end$$
Delimiter ;
call sp_EditarUsuario(1, 'jrealiquez-2021002@gmail.com', 'Celia_as@1980', 'Administrador');