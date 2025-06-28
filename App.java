package TP_Banco;
import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.CuentaDao;
import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.UserDao;
import TP_Banco.dao.UserDaoImpl;
import TP_Banco.dao.dto.CuentaDto;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws ErrorConexionDB {

        UserDao userDao = new UserDaoImpl();
        CuentaDao cuentaDao = new CuentaDaoImpl();
        System.out.println("Bienvenido a la Sucursal del Banco Boedo");

        Scanner sc = new Scanner(System.in);

            //LOGUIN
            System.out.println("Para ingresar deberá autenticarse, ingrese email y password");

            System.out.println("Ingrese su email: ");
            String email = sc.nextLine();
            System.out.println("Ingrese su pass: ");
            String pass = sc.nextLine();

            int userId = userDao.searchUsers(email, pass);

            if(userId == -1){
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
                        cuentaDao.crearCuenta(new CuentaDto(saldo, userId));
                        break;
                    case 2:
                        System.out.println("Ver saldo");
                        cuentaDao.verSaldo();
                    break;
                    case 3:
                        System.out.println("Depositar efectivo");
                        System.out.println("Ingrese el saldo a depositar");
                        double deposito = sc.nextDouble();
                        cuentaDao.depositar(userId, deposito);
                        cuentaDao.verSaldo();
                        break;
                    case 4:
                        System.out.println("Retirar efectivo");
                        System.out.println("Ingrese el saldo a retirar");
                        double retiro = sc.nextDouble();
                        cuentaDao.retirar(userId, retiro);
                        cuentaDao.verSaldo();
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
}
