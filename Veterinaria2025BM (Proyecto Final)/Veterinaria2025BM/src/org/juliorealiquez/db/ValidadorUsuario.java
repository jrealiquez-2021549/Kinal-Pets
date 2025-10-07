
// Realizado por: Julio Gabriel Realiquez Noriega - 2021549 - IN5BM
package org.juliorealiquez.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidadorUsuario {

    public static String validarCredenciales(String nombreUsuario, String contrasena) {
        String sql = "SELECT nivelAcceso FROM Usuarios WHERE nombreUsuario = ? AND contraseñaUsuario = ?";
        String nivelAcceso = null;

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nivelAcceso = rs.getString("nivelAcceso");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error al validar credenciales: " + e.getMessage());
            e.printStackTrace();
        }

        return nivelAcceso;
    }
}