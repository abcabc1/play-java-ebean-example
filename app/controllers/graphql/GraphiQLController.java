package controllers.graphql;

import play.mvc.Controller;
import play.mvc.Result;

public class GraphiQLController extends Controller {

    public Result index() {
        return ok(views.html.graphiql.render());
    }
}
