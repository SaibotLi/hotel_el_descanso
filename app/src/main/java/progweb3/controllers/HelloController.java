package progweb3.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("/hello")
@RequestScoped
public class HelloController {

    @Inject
    Models models;

    @GET
    public String hello() {
        models.put("message", "¡Hola desde Jakarta MVC con Payara!");
        return "response.jsp";
    }
}