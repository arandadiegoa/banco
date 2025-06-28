package TP_Banco;
import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.CuentaDao;
import TP_Banco.dao.CuentaDaoImpl;

public class App {
    public static void main(String[] args) throws ErrorConexionDB {

        CuentaDao cuentaDao = new CuentaDaoImpl();

        System.out.println("Bienvenido a la Sucursal del Banco Boedo");

        //LOGUIN
        System.out.println("Para ingresar deberá autenticarse, ingrese email y password");
        cuentaDao.login();
    }
}
