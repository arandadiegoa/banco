package TP_Banco.test;

import TP_Banco.dao.UserDao;
import TP_Banco.dao.UserDaoImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class UserDaoTest {
    @Test
    public void testLoguinCorrectUsers(){
        UserDao userDao = new UserDaoImpl();
        String mail = "elcuervo@gmail.com";
        String pass = "1234";

        int userId = userDao.searchUsers(mail, pass);
        assertTrue(userId > 0, "El usuario deberia estar registrado y contar con un id válido");
    }
    @Test
    public void testLoguinIncorrectUsers(){
        UserDao userDao = new UserDaoImpl();
        String mail = "pepe@pepe.com";
        String pass = "123456";

        int userId = userDao.searchUsers(mail, pass);
        assertEquals(-1, userId, "Usuario no registrado, debe registrarse y crear una cuenta");
    }
}
