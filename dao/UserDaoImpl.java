package TP_Banco.dao;
import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.dao.dto.UserDto;
import TP_Banco.db.DataBaseConexion;
import java.sql.*;
import java.util.Scanner;

public class UserDaoImpl implements UserDao {
    Scanner sc = new Scanner(System.in);
    private CuentaDao cuentaDao;

    public void setCuentaDao(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }

    public UserDaoImpl(){}

    @Override
    public void create(UserDto user) {

        try {
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

        } catch (SQLException | ErrorConexionDB e) {
            e.printStackTrace();
        }

    }

    @Override
    public int searchUsers(String email, String pass) {
        //Identificar al usuario
        int userId = -1;


        String sql = "SELECT * FROM users WHERE email= ? AND pass= ?";

        try (Connection conn = DataBaseConexion.getInstance().getConexion()) {

            //Realizo busqueda de usuario en la BD
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                //Reemplazo lo valores para evitar SQL Injection.
                stmt.setString(1, email);
                stmt.setString(2, pass);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                        System.out.println("Bienvenido : " + rs.getString("name") + " su identificacion es: " + userId);
                        return userId;
                    }
                }
            }

            //Si No lo encuentra
            System.out.println("Usuario no registrado, debe registrarse y crear una cuenta");
            System.out.println(
                    "Desea registrarse? \n" +
                    "1) si \n" +
                    "2) no \n"
            );

            int ingressUser = sc.nextInt();
            sc.nextLine();
            if(ingressUser == 1){
                System.out.println("Ingrese su nombre: ");
                String name = sc.nextLine();
                //Usuario creado
                create(new UserDto(name, email, pass));
            }else {
                System.out.println("No puede utilizar el sistema");
                System.exit(0);
            }

            //Realizo la consulta por el nuevo usuario generado

            try (   Connection conn2 = DataBaseConexion.getInstance().getConexion();
                    PreparedStatement stmt2 = conn2.prepareStatement(sql)) {

                    stmt2.setString(1, email);
                    stmt2.setString(2, pass);

                try (ResultSet resultSet = stmt2.executeQuery()) {

                    if (resultSet.next()) {
                        userId = resultSet.getInt("id");
                        System.out.println("Bienvenido : " + resultSet.getString("name") + " su identificacion es: " + userId);

                        System.out.println("Crear cuenta");
                        System.out.println("Ingrese un depósito inicial en pesos");
                        double saldo = sc.nextDouble();
                        cuentaDao.crearCuenta(new CuentaDto(saldo, userId));
                    }
                }
            }

        } catch (SQLException | ErrorConexionDB e) {
            e.printStackTrace();
        }
        return userId;
    }
}
