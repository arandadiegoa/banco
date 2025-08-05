package TP_Banco.dao;

import TP_Banco.dao.dto.UserDto;
import TP_Banco.exception.ErrorConexionDB;

public interface UserDao {

    boolean create(UserDto user)throws ErrorConexionDB;
    boolean userExists(String email);
    int findUserId(String email, String pass);
    String obtenerRolPorId(int userId);
}
