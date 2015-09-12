package controllers;

import controllers.routes;
import model.forms.LoginForm;
import play.data.Form;
import play.mvc.*;

import views.html.*;


public class AuthController extends Controller {

    private static final String USERNAME_SESSION_KEY = "email";

    public Result index() {
        return ok(index.render(session().get(USERNAME_SESSION_KEY)));
    }


    public Result login() {
        return ok(login.render(Form.form(LoginForm.class)));
    }

    public Result registration() {
        return ok(registration.render());
    }


    public Result authenticate() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session(USERNAME_SESSION_KEY, loginForm.get().getEmail());
            return Results.redirect(
                    routes.AuthController.index()
            );
        }
    }

    public Result logout() {
        session().clear();
        return redirect(
                routes.AuthController.login()
        );
    }

}
