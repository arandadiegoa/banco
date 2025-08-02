package TP_Banco.dao;

import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.dto.UserDto;

public interface UserDao {
    void create(UserDto user)throws ErrorConexionDB;
    int findUserId(String email, String pass);
    boolean userExists(String email);
    String obtenerRolPorId(int userId);
}
