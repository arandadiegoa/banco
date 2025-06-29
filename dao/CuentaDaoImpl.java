package TP_Banco.dao;

import TP_Banco.Exception.InvalidIngressException;
import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.db.DataBaseConexion;
import TP_Banco.utils.Validator;

import java.sql.*;
import java.util.Scanner;

public class CuentaDaoImpl implements CuentaDao{
    private UserDao userDao;
    private int userId;
    CuentaDto cuentaDto;
    Scanner sc = new Scanner(System.in);

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public CuentaDaoImpl(){}

    @Override
    public void login() {
        String email, pass;

        do {
            System.out.println("Ingrese su email: ");
            email = sc.nextLine();
            System.out.println("Ingrese su pass: ");
            pass = sc.nextLine();
            try {
                if (!Validator.isEmailFormatValid(email) && !Validator.isPasswordRequirementsValid(pass)) {
                    throw new InvalidIngressException("No cumple los parametros establecidos");
                }
            } catch (InvalidIngressException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (!Validator.isEmailFormatValid(email) && !Validator.isPasswordRequirementsValid(pass));

        userId = userDao.searchUsers(email, pass);

        if(userId == -1){
            System.out.println("No se pudo autenticar ni registrar al usuario");
            return;
        }

        System.out.println("Usuario autenticado con ID: " + userId);

        //Funcionalidad
        System.out.println("""
                Menu de opciones
                1) Crear cuenta
                2) Ver saldo
                3) Depositar efectivo
                4) Retirar efectivo
                5) Trasnferir
                6) Salir
                Ingrese la opción deseada: 1, 2, 3, 4, 5 o 6
            """);

        while (true) {
            int nro = sc.nextInt();
            switch (nro){
                case 1:
                    System.out.println("Crear cuenta");
                    System.out.println("Ingrese un depósito inicial en pesos");
                    double saldo = sc.nextDouble();
                    crearCuenta(new CuentaDto(saldo, userId));
                    break;
                case 2:
                    System.out.println("Ver saldo");
                    verSaldo(userId);
                    break;
                case 3:
                    System.out.println("Depositar efectivo");
                    System.out.println("Ingrese el saldo a depositar");
                    double deposito = sc.nextDouble();
                    depositar(userId, deposito);
                    verSaldo(userId);
                    break;
                case 4:
                    System.out.println("Retirar efectivo");
                    System.out.println("Ingrese el saldo a retirar");
                    double retiro = sc.nextDouble();
                    retirar(userId, retiro);
                    verSaldo(userId);
                    break;
                case 5:
                    System.out.println("Transferir");
                    transferir(userId);
                    break;
                case 6:
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
    public void verSaldo(int userId) {

        String sql= "SELECT * FROM cuenta WHERE user_id = ?";

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

              while (rs.next()){
                  int cuentaId = rs.getInt("id");
                 double saldo = rs.getDouble("saldo");
                 System.out.println("Cuenta nro: " + cuentaId + " tiene un saldo disponible de " + saldo + " pesos");
             }
             //Libero recursos
             rs.close();

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

    @Override
    public double transferir(int userId) {

        //Ver cuentas
        String sql= "SELECT * FROM cuenta WHERE user_id = ?";
        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Cuentas disponibles");
            while (rs.next()){
                int cuentaId = rs.getInt("id");
                double saldo = rs.getDouble("saldo");
                System.out.println("Cuenta nro: " + cuentaId + " tiene un saldo disponible de " + saldo + " pesos");
            }

            System.out.println("Ingrese el id de la cuenta origen");
            int cuentaOrigen = sc.nextInt();

            System.out.println("Ingrese el id de la cuenta origen");
            int cuentaDestino = sc.nextInt();

            System.out.println("Ingrese el monto a transferir");
            double dinero = sc.nextDouble();

            //Validar saldo cuenta origen
            String validarSaldoSql = "SELECT * FROM cuenta WHERE id= ? AND user_id = ?";
            PreparedStatement stmtValidar = conn.prepareStatement(validarSaldoSql);
            stmtValidar.setInt(1, cuentaOrigen);
            stmtValidar.setInt(2, userId);
            ResultSet rsVerificar = stmtValidar.executeQuery();

            if(rsVerificar.next()){
                double saldoCuentaOrigen = rsVerificar.getDouble("saldo");

                if(saldoCuentaOrigen >= dinero){
                    //Retirar
                    String sqlRetirarDinero = "UPDATE cuenta SET saldo = saldo - ? WHERE id= ?";
                    PreparedStatement stmtRestar = conn.prepareStatement(sqlRetirarDinero);
                    stmtRestar.setDouble(1, dinero);
                    stmtRestar.setInt(2, cuentaOrigen);
                    stmtRestar.executeUpdate();

                    //Depositar
                    String sqlDepositarDinero = "UPDATE cuenta SET saldo = saldo + ? WHERE id= ?";
                    PreparedStatement stmtSumar = conn.prepareStatement(sqlDepositarDinero);
                    stmtSumar.setDouble(1, dinero);
                    stmtSumar.setInt(2, cuentaDestino);
                    stmtSumar.executeUpdate();

                    System.out.println("Transferencia exitosa de: " + dinero + " de cuenta " + cuentaOrigen
                    + " a cuenta " + cuentaDestino);
                } else {
                    System.out.println("No tiene saldo suficiente para transferir");
                }
            }

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }

        return 0;
    }

}
