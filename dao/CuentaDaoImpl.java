package TP_Banco.dao;

import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.db.DataBaseConexion;

import java.sql.*;

public class CuentaDaoImpl implements CuentaDao{

    @Override
    public void crearCuenta(CuentaDto cuenta) {

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "INSERT INTO cuenta(saldo, user_id) VALUES(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, cuenta.getSaldo());
            stmt.setInt(2, cuenta.getUser_id());

            int filas = stmt.executeUpdate();
            if(filas > 0){
                System.out.println("Saldo inicial: " + cuenta.getSaldo() + ", asociada al usuario con ID: " + cuenta.getUser_id());
            }
            stmt.close();
            conn.close();
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }

    @Override
    public void verSaldo() {
        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cuenta");
             while (rs.next()){
                 int id = rs.getInt("id");
                 double saldo = rs.getDouble("saldo");
                 System.out.println("Numero de cuenta: " + id + " tiene un saldo de: " + saldo + " pesos");
             }
             //Libero recursos
             rs.close();
             stmt.close();
             conn.close();

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }
}
