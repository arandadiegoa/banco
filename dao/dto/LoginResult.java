package TP_Banco.dao.dto;

/**
 * Clase que representa el resultado de un intento de inicio de sesión.
 *
 * <p>Incluye información sobre el ID del usuario, su rol, si el inicio fue exitoso,
 * y un posible mensaje de error.</p>
 */
public class LoginResult {
        private int userId; //id del usuario que intenta iniciar sesión
        private String rol; //define el rol
        private boolean success; //si el login es exitoso
        private String errorMessage;//Error en caso contrario

    // Constructor para inicializar el resultado del login.
        public LoginResult(int userId, String rol, boolean success, String errorMessage) {
            this.userId = userId;
            this.rol = rol;
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public int getUserId() { return userId; }
        public String getRol() { return rol; }
        public boolean isSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
    }

