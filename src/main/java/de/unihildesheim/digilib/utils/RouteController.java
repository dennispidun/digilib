package de.unihildesheim.digilib.utils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {

    @RequestMapping(value = "/{path:[^\\.]*}/**")
    public String forward() {
        return "forward:/index.html";
    }
}
