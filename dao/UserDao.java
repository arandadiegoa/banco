package TP_Banco.dao;

import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.dto.UserDto;

public interface UserDao {
    void create(UserDto user)throws ErrorConexionDB;
    int searchUsers(String email, String pass);
    String obtenerRolPorId(int userId);
}
