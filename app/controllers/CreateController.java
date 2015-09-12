package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.create;


/**
 * Created by Константин on 12.09.2015.
 */
public class CreateController extends Controller {

    @Security.Authenticated(Secured.class)
    public Result create() {
        return ok(create.render(""));
    }
}
