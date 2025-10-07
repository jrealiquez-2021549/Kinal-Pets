
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.db;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;

public class Conexion {
    public static Conexion instancia;
    private Connection conexion;
    
    private Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/DB_Veterinaria?useSSL=false&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "admin";
            conexion = (Connection) DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException error) {
            StackTraceElement elemento = error.getStackTrace()[0];
            System.out.println("Error en: " + elemento.getClassName() + " linea " + elemento.getLineNumber());
            System.out.println("Mensaje: " + error.getMessage());
            error.printStackTrace();
        } catch (SQLException error) {
            StackTraceElement elemento = error.getStackTrace()[0];
            System.out.println("Error en: " + elemento.getClassName() + " linea " + elemento.getLineNumber());
            System.out.println("Mensaje: " + error.getMessage());
            error.printStackTrace();
        }
    }
    
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    
    public Connection getConexion() {
        return conexion;
    }
    
    public void setConexio(Connection conexion) {
        this.conexion = conexion;
    }
}