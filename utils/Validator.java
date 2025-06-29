package TP_Banco.utils;

public class Validator {
    private boolean esTextoValido(String input) {
        return input.matches("[a-zA-Z]+");
    }

    public static boolean isEmailFormatValid(String email) {
        return email !=null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isPasswordRequirementsValid(String pass) {
        return pass !=null && pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
}
