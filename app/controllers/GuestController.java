package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Created by Константин on 12.09.2015.
 */
public class GuestController extends Controller {

    public Result index() {
        return ok(index.render(session().get(Constants.USERNAME_SESSION_KEY)));
    }
}
