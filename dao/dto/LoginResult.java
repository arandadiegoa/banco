package TP_Banco.dao.dto;

public class LoginResult {
        private int userId;
        private String rol;
        private boolean success;
        private String errorMessage;

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

