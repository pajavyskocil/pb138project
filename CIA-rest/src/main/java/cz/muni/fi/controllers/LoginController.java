package cz.muni.fi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for entry point into Web Application.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class LoginController {

    /* User has to choose which manager he/she wants to access */
    @RequestMapping(value = "/start")
    public String showTransitScreen(Model model) {
        model.addAttribute("title", "Welcome");

        return "transit";
    }
}