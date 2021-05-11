package com.example.demo;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private DatasetRepository datarepo;
	
	private int companyID;
		
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/home")
	public String viewHome() {
		return "index";
	}
	
	@GetMapping("/register_type")
	public String showRegisterType() {
		return "registration";
	}
	
	@GetMapping("/file_upload")
	public String showFileUpload(Model model) {
		List<Dataset> listData = datarepo.findAll();
		model.addAttribute("listData", listData);
		return "uploadfile";
	}
	
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("username") String username,
			RedirectAttributes ra) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		
		Dataset dataset = new Dataset();
		dataset.setName(fileName);
		
		dataset.setContent(multipartFile.getBytes());
		dataset.setSize(multipartFile.getSize());
		dataset.setUploadTime(new Date());
		dataset.setUsername(username);
	
		datarepo.save(dataset);
		
		ra.addFlashAttribute("message","File uploaded successfully!");
		
		return "redirect:/file_upload";
	}
	@GetMapping("/register_individual")
	public String showIndividualReg(Model model) {
		model.addAttribute("user", new User());
		return "individualregistration";
	}
	
	@GetMapping("/register_business")
	public String showBusinessReg(Model model) {
		model.addAttribute("user", new User());
		return "businessregistration";
	}
	
	@GetMapping("/account")
	public String showAccount() {
		return "account";
	}
	@PostMapping("/process_register/ind")
	public String processRegistrationInd(User user, RedirectAttributes ra) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		if(repo.findByUsername(user.getUsername()) != null) {
			ra.addFlashAttribute("message","User already exists.");
			
			return "redirect:/register_individual";
		}
		else {
			user.setAccountType(0);
			repo.save(user);
			return "register_success";
		}
	}
	
	@PostMapping("/process_register/bus")
	public String processRegistrationBus(User user, RedirectAttributes ra) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		if(repo.findByUsername(user.getUsername()) != null) {
			ra.addFlashAttribute("message","User already exists.");
			
			return "redirect:/register_business";
		}
		else {
			user.setAccountType(1);
			user.setCompanyID(new Random().nextInt(900000) + 100000);
			repo.save(user);
			return "register_success";
		}
	}
	
	@GetMapping("/main")
	public String viewUsersList(Model model) {		
//		List<Dataset> listData = datarepo.findAll();
//		model.addAttribute("listData", listData);
		return "main";
	}
	
	@RequestMapping("/main/{username}")
	public String showDataset(@PathVariable String username, Model model) {
		List<Dataset> listData= datarepo.findByUsername(username);
		model.addAttribute("listData", listData);
		return "main";
	}
	
	@RequestMapping("/main/delete_dataset/{id}")
	public String deleteDataset(@PathVariable String id) {
		datarepo.deleteFromId(id);
		return "redirect:/main";
	}

	@GetMapping("/charts")
	public String showCharts(){
		return "charts";
	}
	
	@GetMapping("/download")
	public void downloadFile(@Param("id") int id, HttpServletResponse response) throws IOException {
		Dataset result = datarepo.findByDatasetId(id);
		
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename="+result.getName();
		
		response.setHeader(headerKey, headerValue);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		outputStream.write(result.getContent());
		
		outputStream.close();
		
	}
}
