package TP_Banco.dao;

public interface UserDao {

    //void create(UserDto user)throws ErrorConexionDB;
    int findUserId(String email, String pass);
    String obtenerRolPorId(int userId);
}
