package com.checkpointformcleaner.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.checkpointformcleaner.entity.Role;
import com.checkpointformcleaner.entity.User;
import com.checkpointformcleaner.service.EmailService;
import com.checkpointformcleaner.service.UserService;

@Controller
public class MainController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	//mivel csak a UserServiveImpl.java implementálja a UserService-t, ezért elég saját magát meghívni.
	private UserService userService;
	private EmailService emailService;
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	/**
	 * A @Qualifier("UserServiveImpl") ezt csak akkor kell használni, hogyha a UserService interface-t több 
	 * osztály is implementálja. ebben az esetben egyértelműsíteni kellene, hogy melyik implementációt 
	 * szeretném injektálni. Ha ezt nem így csináljuk, a Spring megzavarodhat!
	 * @param userService
	 */
	@Autowired
	//@Qualifier("UserServiveImpl")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("act", "active");
		model.addAttribute("starter", "Főoldal");
		model.addAttribute("descript", "Statisztikák");
		return "index";
	}

	@RequestMapping("/revenues")
	public String revenues(Model model) {
		model.addAttribute("actrev", "active");
		//return "revenues";
		return null;
	}

	@RequestMapping("/costs")
	public String costs(Model model) {
		model.addAttribute("actcost", "active");
		//return "costs";
		return null;
	}

	@RequestMapping("/buffet")
	public String buffet(Model model) {
		model.addAttribute("actbuf", "active");
		//return "buffet";
		return null;
	}	

	@RequestMapping("/reports")
	public String reports(Model model) {
		model.addAttribute("actrep", "active");
		//return "reports";
		return null;
	}	

	@RequestMapping("/stories")
	public String stories(Model model) {
		model.addAttribute("actstory", "active");
		return "stories";
	}	

	@RequestMapping("/bloggers")
	public String bloggers(Model model) {
		model.addAttribute("actbloggers", "active");
		return "bloggers";
	}	
	
	@RequestMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}
	
	//@RequestMapping(value = "/reg", method = RequestMethod.POST)
	//ez a klasszikus POST kezelés, ami a registration formtól jön
	@PostMapping("/reg")
	public String reg(@ModelAttribute User user) {
		//A @ModelAttribute-vel jelzem, h a user obj a frontendről jön, s a Model-nek kell visszaszállítania		
		log.info("Új user");
		log.debug(user.getFullName());
		log.debug(user.getEmail());
		userService.registerUser(user);
		//log.debug(user.getPassword());
		return "redirect:/index";
	}
	
	@RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
	public String activation(@PathVariable("code") String code, HttpServletResponse response ) {		
		String result = userService.userActivation(code);
		return "redirect:/index";
	}
	
}
