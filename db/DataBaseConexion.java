package TP_Banco.db;

import TP_Banco.exception.ErrorConexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConexion {
    public static DataBaseConexion instance;
    private Connection conexion;

    //Configuracion BD
    private final String url = "jdbc:mysql://localhost:3306/banco";
    private final String users = "root";
    private final String password = "";

    private DataBaseConexion() throws ErrorConexionDB {
        try {
            conexion = DriverManager.getConnection(url, users, password);
        }catch (SQLException e){
            throw new ErrorConexionDB("Error al conectar a la Base de datos");
        }
    }

    //Metodo para obtener unica instancia
    public static DataBaseConexion getInstance() throws SQLException, ErrorConexionDB {
        if(instance == null || instance.getConexion().isClosed()){
            instance = new DataBaseConexion();
        }
        return instance;
    }

    //Metodo para obtener la conexion

    public Connection getConexion() throws ErrorConexionDB {
        try {
            return DriverManager.getConnection(url, users, password);
        }catch (SQLException e){
            throw new ErrorConexionDB("Error al conectar a la Base de datos");
        }
    }
}
