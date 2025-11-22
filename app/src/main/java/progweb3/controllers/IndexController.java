package progweb3.controllers;

import jakarta.mvc.Controller;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("/")
public class IndexController {

    @GET
    public String index() {
        return "index.jsp";
    }
}
