package TP_Banco.dao;
import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.dto.UserDto;
import TP_Banco.db.DataBaseConexion;
import java.sql.*;

public class UserDaoImpl implements UserDao {
    private CuentaDao cuentaDao;

    public void setCuentaDao(CuentaDao cuentaDao) {
        this.cuentaDao = cuentaDao;
    }

    public UserDaoImpl(){}

    @Override
    public void create(UserDto user) {

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "INSERT INTO users(name, email, pass) VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPass());

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario: " + user.getName() + ", se guardó de forma correcta en la BD.");
            }
            stmt.close();
            conn.close();

        } catch (SQLException | ErrorConexionDB e) {
            e.printStackTrace();
        }

    }

    @Override
    public int findUserId(String email,String pass ){
        String sql = "SELECT * FROM users WHERE email= ? AND pass= ?";
        try (Connection conn = DataBaseConexion.getInstance().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, pass);

                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    return rs.getInt("id");
                }
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public boolean userExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        }catch (SQLException | ErrorConexionDB e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String obtenerRolPorId(int userId) {
        String rol = "";
        String sql = "SELECT rol FROM users WHERE id = ?";

        try (Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    rol = rs.getString("rol");
                }
            }catch (SQLException | ErrorConexionDB e){
                e.printStackTrace();
        }

        return rol;
    }


}
