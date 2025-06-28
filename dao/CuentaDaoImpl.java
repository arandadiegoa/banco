package TP_Banco.dao;

import TP_Banco.Exception.EmailInvalidException;
import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.dao.dto.UserDto;
import TP_Banco.db.DataBaseConexion;
import TP_Banco.utils.Validator;

import java.sql.*;
import java.util.Scanner;

public class CuentaDaoImpl implements CuentaDao{
    UserDao userDao = new UserDaoImpl();
    UserDto user;
    CuentaDto cuentaDto;
    Scanner sc = new Scanner(System.in);

    @Override
    public void login() {

        user = new UserDto();
        cuentaDto = new CuentaDto();

        do {
            System.out.println("Ingrese su email: ");
            user.setEmail(sc.nextLine());
            System.out.println("Ingrese su pass: ");
            user.setPass(sc.nextLine());
            try {
                if (!Validator.isEmailFormatValid(user.getEmail()) && !Validator.isPasswordRequirementsValid(user.getPass())) {
                    throw new EmailInvalidException("No cumple los parametros establecidos");
                }
            } catch (EmailInvalidException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (!Validator.isEmailFormatValid(user.getEmail()) && !Validator.isPasswordRequirementsValid(user.getPass()));

       cuentaDto.setUser_id(userDao.searchUsers(user.getEmail(), user.getPass()));

        if(cuentaDto.getUser_id() == -1){
            System.out.println("No se pudo autenticar ni registrar al usuario");
            return;
        }

        //Funcionalidad
        System.out.println("""
                Menu de opciones
                1) Crear cuenta
                2) Ver saldo
                3) Depositar efectivo
                4) Retirar efectivo
                5) Salir
                Ingrese la opción deseada: 1, 2, 3, 4, 5
            """);

        while (true) {
            int nro = sc.nextInt();
            switch (nro){
                case 1:
                    System.out.println("Crear cuenta");
                    System.out.println("Ingrese un depósito inicial en pesos");
                    double saldo = sc.nextDouble();
                    crearCuenta(new CuentaDto());
                    break;
                case 2:
                    System.out.println("Ver saldo");
                    verSaldo();
                    break;
                case 3:
                    System.out.println("Depositar efectivo");
                    System.out.println("Ingrese el saldo a depositar");
                    double deposito = sc.nextDouble();
                    depositar(cuentaDto.getUser_id(), deposito);
                    verSaldo();
                    break;
                case 4:
                    System.out.println("Retirar efectivo");
                    System.out.println("Ingrese el saldo a retirar");
                    double retiro = sc.nextDouble();
                    retirar(cuentaDto.getUser_id(), retiro);
                    verSaldo();
                    break;
                case 5:
                    System.out.println("Gracias por usar el sistema, hasta la próxima!!");
                    return;
                default:
                    System.out.println("Opción inválida, intente nuevamente");
            }
            System.out.println("Ingrese una opción para continuar");
        }
    }

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
              if(rs.next()){
                 double saldo = rs.getDouble("saldo");
                 System.out.println("Saldo disponible: " + saldo + " pesos");
             }
             //Libero recursos
             rs.close();
             stmt.close();
             conn.close();

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }

    @Override
    public double depositar(int userId, double newSaldo) {
        try{
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "UPDATE cuenta SET saldo = saldo + ? WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newSaldo);
            stmt.setInt(2, userId);

            int filas = stmt.executeUpdate();
            if(filas >0){
                System.out.println("Saldo depositado correctamente: " + newSaldo + " pesos.");
            }
            stmt.close();
            conn.close();
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double retirar(int userId, double newSaldo) {
        try{
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "UPDATE cuenta SET saldo = saldo - ? WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newSaldo);
            stmt.setInt(2, userId);

            int filas = stmt.executeUpdate();
            if(filas >0){
                System.out.println("Saldo retirado correctamente: " + newSaldo + " pesos.");
            }
            stmt.close();
            conn.close();
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return 0;
    }

}
