package util.validator;

public class UserInformationValidator {

    private static final UserInformationValidator INSTANCE = new UserInformationValidator();

    public static UserInformationValidator getInstance() {
        return INSTANCE;
    }

    private UserInformationValidator() {}

    private static final String USERNAME_FORMAT_REGEX = "[A-Za-z0-9][A-Za-z0-9_-]{4,13}[A-Za-z0-9]";
    private static final String NAME_FORMAT_REGEX = ".{2,30}";
    private static final String EMAIL_FORMAT_REGEX = "[a-z][[a-z][0-9][-][_]]{3,25}[@][a-z]{2,10}[.][a-z]{2,4}";
    private static final String PASSWORD_FORMAT_REGEX = "[A-Za-z0-9][A-Za-z0-9_-]{6,28}[A-Za-z0-9]";

    public boolean validate(String email) {
        return email.matches(EMAIL_FORMAT_REGEX);
    }

    public boolean validate(String username, String password) {
        return username.matches(USERNAME_FORMAT_REGEX) &&
                password.matches(PASSWORD_FORMAT_REGEX);
    }

    public boolean validate(String firstName, String lastName, String email) {
        return firstName.matches(NAME_FORMAT_REGEX) &&
                lastName.matches(NAME_FORMAT_REGEX) &&
                email.matches(EMAIL_FORMAT_REGEX);
    }

    public boolean validate(String username, String firstName, String lastName,
                            String email, String password, String confirmedPassword) {
        return username.matches(USERNAME_FORMAT_REGEX) &&
                firstName.matches(NAME_FORMAT_REGEX) &&
                lastName.matches(NAME_FORMAT_REGEX) &&
                email.matches(EMAIL_FORMAT_REGEX) &&
                password.matches(PASSWORD_FORMAT_REGEX) &&
                password.equals(confirmedPassword);
    }

    public boolean validatePasswords(String currentPassword, String newPassword, String confirmedPassword) {
        return currentPassword.matches(PASSWORD_FORMAT_REGEX) &&
                newPassword.matches(PASSWORD_FORMAT_REGEX) &&
                confirmedPassword.matches(PASSWORD_FORMAT_REGEX) &&
                newPassword.equals(confirmedPassword);
    }
}
