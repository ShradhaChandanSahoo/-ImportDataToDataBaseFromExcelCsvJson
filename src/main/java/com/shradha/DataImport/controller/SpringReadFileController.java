package com.shradha.DataImport.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shradha.DataImport.model.User;
import com.shradha.DataImport.service.SpringReadFileService;


@Controller
public class SpringReadFileController {

		@Autowired
		private SpringReadFileService springReadFileService;
		
		@GetMapping("/")
		private String home(Model theModel) {
			
			theModel.addAttribute("user", new User());
			List<User> theUser=springReadFileService.findAll();
			theModel.addAttribute("users", theUser);
			
			return "view/users";
		}
		
		@PostMapping("/fileupload")
		private String uploadFile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
			System.out.println("==============File================="+user.getFile());
			boolean isFlag = springReadFileService.saveDataFromUploadFile(user.getFile());
			
			if(isFlag) {
				redirectAttributes.addFlashAttribute("successmessage", "File Upload Successfully!!!");
			}else {
				redirectAttributes.addFlashAttribute("errormessage", "File Upload Not Done, Please Try Again!!!");
			}
			
			return "redirect:/";
			
		}
}
