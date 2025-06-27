package TP_Banco;
import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.UserDao;
import TP_Banco.dao.UserDaoImpl;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws ErrorConexionDB {

        UserDao userDao = new UserDaoImpl();

        System.out.println("Bienvenido a la Sucursal del Banco Boedo.");

        Scanner sc = new Scanner(System.in);

        //LOGUIN
            System.out.println("Para ingresar debe escribir su email y pass.");

            System.out.println("Ingrese su email: ");
            String email = sc.nextLine();
            System.out.println("Ingrese su pass: ");
            String pass = sc.nextLine();

            userDao.searchUsers(email, pass);
            System.out.println("Menu de opciones:\n" +
                "1) Ver saldo \n" +
                "2) Depositar efectivo \n" +
                "3) Retirar efectivo");

            System.out.println("Ingrese 1, 2, o 3");
            int nro = sc.nextInt();

    }
}
