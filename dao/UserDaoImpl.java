package TP_Banco.dao;

import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.dto.UserDto;
import TP_Banco.db.DataBaseConexion;
import java.sql.*;
import java.util.Scanner;

public class UserDaoImpl implements UserDao {
    Scanner sc = new Scanner(System.in);
    @Override
    public void create(UserDto user) {

        try{
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "INSERT INTO users(name, email, pass) VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPass());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario: " + user.getName() + ", se guardó de forma correcta en la BD.");
            }
            stmt.close();
            conn.close();

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }

    }
    @Override
    public int searchUsers(String email, String pass) {
        //Identificar al usuario
        int userId = -1; //valor inicial por defecto

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql= "SELECT * FROM users WHERE email= ? AND pass= ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            //Reemplazo lo valores para evitar SQL Injection.
            stmt.setString(1, email);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
               userId = rs.getInt("id");
                System.out.println("Bienvenido : " + rs.getString("name") + " su identificacion es: " + userId );
            }else {
                System.out.println("Usuario no registrado, debe registrarse");

                System.out.println("Ingrese su nombre: ");
                String name = sc.nextLine();

                UserDao userDao = new UserDaoImpl();
                userDao.create(new UserDto(name, email, pass));
            }

        } catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return userId;
    }

}
