package com.basicit.controller


import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class HtmlController() {

  @GetMapping("")
  fun root(): ModelAndView =  ModelAndView("forward:/index")

  @GetMapping("index")
  fun index(): ModelAndView =  ModelAndView("index")

}
