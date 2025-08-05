package TP_Banco;
import TP_Banco.dao.MovimientoDaoImpl;
import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.UserDaoImpl;
import TP_Banco.ui.LoginFrame;

public class App {
    public static void main(String[] args) throws ErrorConexionDB {
        UserDaoImpl userDao = new UserDaoImpl();
        CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
        MovimientoDaoImpl movimientoDao = new MovimientoDaoImpl();

        userDao.setCuentaDao(cuentaDao);
        cuentaDao.setUserDao(userDao);


        //Iniciar GUI
        javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame(cuentaDao, movimientoDao, userDao));

    }
}
