package services;

import com.google.inject.Singleton;
import dao.UserDAO;
import model.entity.User;
import model.forms.LoginForm;
import model.forms.RegistrationForm;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import services.exceptions.AuthException;
import services.exceptions.SystemException;

import javax.inject.Inject;

/**
 * Created by Константин on 12.09.2015.
 */
@Singleton
public class AuthService {

    private static final String INCORRECT_PASSWORD = "Incorrect password";
    private static final String USER_IS_NOT_FOUND = "User is not found";

    @Inject
    private UserDAO userDAO;

    public User authUser(LoginForm loginForm) throws AuthException, SystemException {
        User user =  userDAO.findByEmail(loginForm.getEmail());
        if (user == null) {
            throw new AuthException(USER_IS_NOT_FOUND);
        }
        if (loginForm.getPassword() == null) {
            throw new AuthException(INCORRECT_PASSWORD);
        }
        String passHash = DigestUtils.md5Hex(loginForm.getPassword());
        if (!passHash.equals(user.getPassHash())) {
            throw new AuthException(INCORRECT_PASSWORD);
        }
        return user;
    }

    public void addUser(RegistrationForm registrationForm) throws AuthException, SystemException {
        checkForm(registrationForm);
        User sameEmailUser = userDAO.findByEmail(registrationForm.getEmail());
        if (sameEmailUser != null) {
            throw new AuthException("User with the same email is found");
        }
        User user = new User();
        user.setEmail(registrationForm.getEmail());
        user.setName(registrationForm.getName());
        user.setPassHash(DigestUtils.md5Hex(registrationForm.getPassword1()));
        userDAO.save(user);
    }

    private void checkForm(RegistrationForm registrationForm) throws AuthException {
        if (registrationForm == null) throw new AuthException("No registration data");
        if (StringUtils.isEmpty(registrationForm.getEmail())) throw new AuthException("Email is empty");
        if (StringUtils.isEmpty(registrationForm.getName())) throw new AuthException("Name is empty");
        if (StringUtils.isEmpty(registrationForm.getPassword1())) throw new AuthException("Password is empty");
        if (!registrationForm.getPassword1().equals(registrationForm.getPassword2())) {
            throw new AuthException("Passwords are not equal");
        }
    }
}
