package cz.muni.fi;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for entry point into Web Application.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Component
public class LoginController {

    /* User has to choose which manager he/she wants to access */
    @RequestMapping(value = "/transit")
    public String showTransitScreen() {
        return "transit";
    }
}