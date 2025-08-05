package TP_Banco.dao;

import TP_Banco.dao.dto.MovimientoDto;
import TP_Banco.db.DataBaseConexion;
import TP_Banco.exception.ErrorConexionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link MovimientoDao} que gestiona
 * el registro y visualización de movimientos asociados a cuentas bancarias.
 *
 * <p>Utiliza JDBC para acceder a la base de datos y muestra los resultados
 * en componentes Swing como {@link JTable} y {@link JOptionPane}.</p>
 */

public class MovimientoDaoImpl implements MovimientoDao {

    //Registra un nuevo movimiento en la base de datos.
    @Override
    public void registrarMovimientos(MovimientoDto movimiento) {
        String sql = "INSERT INTO movimientos (cuenta_id, tipo, monto, descripcion) VALUES (?,?,?,?)";
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

    //Recupera los movimientos de una cuenta específica, ordenados por fecha descendente
    @Override
    public List<MovimientoDto> obtenerMovimientosIdCuenta(int cuentaId) {
        List<MovimientoDto> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? ORDER BY fecha DESC";

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

    //Recupera todas las transacciones realizadas en el sistema.
    @Override
    public List<MovimientoDto> obtenerTransaccionesRealizadas() {
        List<MovimientoDto> movimientos = new ArrayList<>();
        String sql = "SELECT * FROM movimientos ORDER BY fecha DESC";

        try (Connection conn = DataBaseConexion.getInstance().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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

    /*
        Muestra en una tabla todas las transacciones registradas del sistema.
        Utiliza Swing para crear una ventana con los datos tabulados.
    */
    @Override
    public void verTransacciones() {

        List<MovimientoDto> movimientos = obtenerTransaccionesRealizadas();

        if(movimientos.isEmpty()){
            JOptionPane.showMessageDialog(null,
                    "No se registran movimientos.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        JFrame frame = new JFrame("Transacciones realizadas por el sistema: ");
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnas = {"ID", "Cuenta ID", "Tipo", "Monto", "Fecha", "Descripción"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

            for(MovimientoDto mov : movimientos){
                Object[] fila = {
                        mov.getId(),
                        mov.getCuentaId(),
                        mov.getTipo(),
                        mov.getMonto(),
                        mov.getDescription()
                };
                model.addRow(fila);
            }

            //Crear JTable con el modelo
            JTable tabla = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(tabla);

            //Mostrar la tabla
            frame.add(scrollPane);
            frame.setVisible(true);
        }
}

