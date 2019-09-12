package com.evergrande.generator.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/index")
    public String index() {
        return "/index";
    }

    @RequestMapping(value = "/generator/list")
    public String generatorList() {
        return "/generator/list";
    }

    @RequestMapping(value = "/generator/form")
    public String generatorForm() {
        return "/generator/form";
    }
}
