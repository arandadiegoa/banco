package TP_Banco.dao;

import TP_Banco.Exception.ErrorConexionDB;
import TP_Banco.dao.dto.UserDto;

public interface UserDao {
    void create(UserDto user)throws ErrorConexionDB;
    void searchUsers(String email, String pass);
}
