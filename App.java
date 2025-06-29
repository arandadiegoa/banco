package TP_Banco;
import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.UserDaoImpl;

public class App {
    public static void main(String[] args) throws ErrorConexionDB {
        UserDaoImpl userDao = new UserDaoImpl();
        CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
        System.out.println("Bienvenido a la Sucursal del Banco Boedo");

        userDao.setCuentaDao(cuentaDao);
        cuentaDao.setUserDao(userDao);

        //LOGUIN
        System.out.println("Para ingresar deberá autenticarse, ingrese email y password");
        cuentaDao.login();
    }
}
