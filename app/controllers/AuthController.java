package controllers;

import model.entity.User;
import model.forms.LoginForm;
import model.forms.RegistrationForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.*;

import services.AuthService;
import services.exceptions.AuthException;
import services.exceptions.SystemException;
import views.html.*;

import javax.inject.Inject;


public class AuthController extends Controller {

    @Inject
    private AuthService authService;

    private static final String USERNAME_SESSION_KEY = "name";

    public Result index() {
        return ok(index.render(session().get(USERNAME_SESSION_KEY)));
    }


    public Result login() {
        return ok(login.render(Form.form(LoginForm.class), null));
    }

    public Result registration() {
        return ok(registration.render(Form.form(RegistrationForm.class), null));
    }

    @Transactional
    public Result addUser() {
        Form<RegistrationForm> registrationForm = Form.form(RegistrationForm.class).bindFromRequest();
        try {
            authService.addUser(registrationForm.get());
            session().clear();
            session(USERNAME_SESSION_KEY, registrationForm.get().getName());
            return Results.redirect(routes.AuthController.index());
        } catch (AuthException e) {
            return badRequest(registration.render(registrationForm, e.getMessage()));
        } catch (SystemException e) {
            return badRequest(registration.render(registrationForm, "System error"));
        }

    }


    @Transactional(readOnly = true)
    public Result authenticate() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
        try {
            User user = authService.authUser(loginForm.get());
            session().clear();
            session(USERNAME_SESSION_KEY, user.getName());
            return Results.redirect(
                    routes.AuthController.index()
            );
        } catch (AuthException e) {
            return badRequest(login.render(loginForm, e.getMessage()));
        } catch (SystemException e) {
            return badRequest(login.render(loginForm, "System error"));
        }
    }

    public Result logout() {
        session().clear();
        return redirect(
                routes.AuthController.login()
        );
    }

}
