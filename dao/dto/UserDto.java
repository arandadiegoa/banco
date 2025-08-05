package TP_Banco.dao.dto;

/**
 * Clase que representa un usuario del sistema bancario.
 *
 * <p>Contiene información básica como ID, nombre, correo electrónico y contraseña.</p>
 */

public class UserDto {
    private int id;
    private String name;
    private String email;
    private String pass;

    public UserDto(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }
    public UserDto (){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

}
