package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;
/**
 * Created by Константин on 12.09.2015.
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.AuthController.login());
    }
}
