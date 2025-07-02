package TP_Banco.dao.dto;

public class UserDto {
    private int id;
    private String name;
    private String email;
    private String pass;

    private String rol;

    public UserDto(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.rol = "cliente";
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

    public void setName(String name) {
        this.name = name;
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

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
