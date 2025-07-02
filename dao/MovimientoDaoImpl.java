package TP_Banco.dao;

import TP_Banco.dao.dto.MovimientoDto;
import TP_Banco.db.DataBaseConexion;
import TP_Banco.exception.ErrorConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDaoImpl implements MovimientoDao {

    @Override
    public void registrarMovimientos(MovimientoDto movimiento) {
        String sql = "INSERT INTO movimientos (cuenta_id, tipo, monto, descripcion) VALUES ?,?,?,?)";
        try(Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, movimiento.getCuentaId());
            stmt.setString(2, movimiento.getTipo());
            stmt.setDouble(3, movimiento.getMonto());
            stmt.setString(4, movimiento.getDescription());

            stmt.executeUpdate();
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }

    @Override
    public List<MovimientoDto> obtenerMovimientosIdCuenta(int cuentaId) {
        List<MovimientoDto> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? ORDER_BY fecha DESC";

        try (Connection conn = DataBaseConexion.getInstance().getConexion();
        PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, cuentaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                MovimientoDto mov = new MovimientoDto(
                        rs.getInt("id"),
                        rs.getInt("cuenta_id"),
                        rs.getString("tipo"),
                        rs.getDouble("monto"),
                        rs.getTimestamp("fecha"),
                        rs.getString("descripcion")
                );
                movimientos.add(mov);
            }
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return movimientos;
    }
}
