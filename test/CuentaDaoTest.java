package TP_Banco.test;

import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.db.DataBaseConexion;
import TP_Banco.exception.ErrorConexionDB;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class CuentaDaoTest {

    public List<CuentaDto>obtenerCuentasPorId(int userId) {
        List<CuentaDto> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM cuenta WHERE user_id = ?";
        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                double saldo = rs.getDouble("saldo");
                cuentas.add(new CuentaDto(saldo, userId));
            }

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return cuentas;
    }

    @Test
    public void testObtenerCuentasUsers() {
        int userId = 13;

        List<CuentaDto> cuentas = obtenerCuentasPorId(userId);
        assertNotNull(cuentas);
        assertFalse(cuentas.isEmpty(), "El uusuario tiene que tener como mínimo una cuenta para operar");

        for (CuentaDto cuenta : cuentas) {
            assertEquals(userId, cuenta.getUser_id(), "El id del usuario es el correcto");
            assertTrue(cuenta.getSaldo() >= 0, "El saldo no se puede ser negativo");
        }


    }
}
