package com.example.demo;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	private String username;

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
		return "registration2";
	}

	@GetMapping("/file_upload")
	public String showFileUpload(Model model) {
		List<Dataset> listData = datarepo.findAll();
		model.addAttribute("listData", listData);
		return "uploadfile";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("username") String username, RedirectAttributes ra) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		Dataset dataset = new Dataset();
		dataset.setName(Paths.get("").toString());
		String datasetsDir = System.getProperty("user.dir") + "/src/datasets/";
		while (Files.exists(Paths.get(datasetsDir + fileName)) == true) { // appends character to prevent override
			fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_1"
					+ fileName.substring(fileName.lastIndexOf("."));
		}
		File file = new File(datasetsDir + fileName);
		multipartFile.transferTo(file);
		dataset.setContent("/src/datasets/" + fileName);
		dataset.setName(fileName);
		dataset.setSize(multipartFile.getSize());
		dataset.setUploadTime(new Date());
		dataset.setUsername(username);

		datarepo.save(dataset);

		ra.addFlashAttribute("message", "File uploaded successfully!");

		return "redirect:/file_upload";
	}

	@GetMapping("/register_individual")
	public String showIndividualReg(Model model) {
		model.addAttribute("user", new User());
		return "individualreg";
	}

	@GetMapping("/register_business")
	public String showBusinessReg(Model model) {
		model.addAttribute("user", new User());
		return "businessreg";
	}

	@GetMapping("/account")
	public String showAccount(@AuthenticationPrincipal CustomUserDetails loggedUser) {
		if (loggedUser == null) {
			return "index";
		}
		String email = loggedUser.getFullName();
		this.username = email;
		User user = repo.findByUsername(this.username);
		if (user.getCompanyID() != 0 && user.getAccountType().equals("Company Admin")) {
			return "accountBus";
		}

		return "accountInd";
	}

	@PostMapping("/process_register/ind")
	public String processRegistrationInd(User user, RedirectAttributes ra) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		if (repo.findByUsername(user.getUsername()) != null) {
			ra.addFlashAttribute("message", "User already exists.");

			return "redirect:/register_individual";
		}

		if (user.getCompanyID() != 0) {
			if (repo.findByCompanyID(user.getCompanyID()) == null) {
				ra.addFlashAttribute("message", "Invalid company ID.");

				return "redirect:/register_individual";
			}
		}

		user.setAccountType("Individual");
		repo.save(user);
		return "register_success";
	}

	@PostMapping("/process_register/bus")
	public String processRegistrationBus(User user, RedirectAttributes ra) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		if (repo.findByUsername(user.getUsername()) != null) {
			ra.addFlashAttribute("message", "User already exists.");

			return "redirect:/register_business";
		} else {
			user.setAccountType("Company Admin");
			user.setCompanyID(new Random().nextInt(900000) + 100000);
			repo.save(user);
			return "register_success";
		}
	}

	@PostMapping("/user/updatepassword/{username}")
	public String updatePassword(@PathVariable String username, @RequestParam("newPassword") String newPassword) {
		System.out.println(newPassword);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = repo.findByUsername(username);
		user.setPassword(encoder.encode(newPassword));

		repo.save(user);
		return "accountInd";
	}

	@GetMapping("/main")
	public String viewUsersList(Model model) {
		// List<Dataset> listData = datarepo.findAll();
		// model.addAttribute("listData", listData);
		return "main2";
	}

	@RequestMapping("/main/{username}")
	public String showDataset(@PathVariable String username, Model model) {
		int companyID = repo.getCompanyID(username);
		if (companyID != 0) {
			User companyAdmin = repo.findByCompanyID(companyID);
			String companyUsername = companyAdmin.getUsername();
			List<Dataset> listData = datarepo.findByUsernameAndComp(username, companyUsername);
			model.addAttribute("listData", listData);
			return "main2";
		}
		List<Dataset> listData = datarepo.findByUsername(username);
		model.addAttribute("listData", listData);
		return "main2";
	}

	@RequestMapping("/main/delete_dataset/{id}")
	public String deleteDataset(@PathVariable String id) {
		datarepo.deleteFromId(id);
		return "redirect:/main";
	}

	@GetMapping("/charts/{id}")
	public String showCharts(@PathVariable String id) {

		return "charts";
	}

	@GetMapping("/download")
	public void downloadFile(@Param("id") int id, HttpServletResponse response) throws IOException {
		Dataset result = datarepo.findByDatasetId(id);

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Dispositison";
		String headerValue = "attachment; filename=" + result.getName();

		response.setHeader(headerKey, headerValue);

		ServletOutputStream outputStream = response.getOutputStream();

		// outputStream.write(result.getContent());

		outputStream.close();

	}

	@GetMapping("/tutorial")
	public String showTutorial() {
		return "tutorial";
	}

	@RequestMapping("/account/upgrade/{username}")
	public String upgradeAccount(@PathVariable String username, RedirectAttributes ra) {
		User user = repo.findByUsername(username);
		if (user == null) {
			ra.addFlashAttribute("message", "Failed to upgrade user");
			return "redirect:/accountBus";
		}

		user.setAccountType("Company Admin");
		repo.save(user);

		return "accountBus";
	}

	@RequestMapping("/account/delete/{username}")
	public String deleteAccount(@PathVariable String username) {

		if (repo.findCompanyAccount(username) != null) {
			int companyID = repo.getCompanyID(username);
			repo.updateCompanyID(companyID);
		}
		datarepo.deleteFromUsername(username);
		repo.deleteFromId(username);
		return "redirect:/home";
	}

	@RequestMapping("/account/update_password")
	public String updatePassword(@RequestParam("password") String password) {
		return "accountBus";

	}

	@GetMapping("/account/view")
	public String showView(Model model) {
		if (repo.findCompanyAccount(this.username) != null) {
			int companyID = repo.getCompanyID(this.username);
			List<User> userData = repo.getUsersWithCompanyID(companyID);
			model.addAttribute("userData", userData);
			return "accountBus";
		}

		return "accountBus";
	}

	@RequestMapping("/account/remove/{id}")
	public String removeIndividual(@PathVariable String id) {
		System.out.println(id);
		repo.removeCompanyID(id);
		return "redirect:/account";
	}

	// @GetMapping("/account/companyID")
	// public String showCompanyID(Model model) {
	// int companyID = repo.getCompanyID(this.username);
	//
	// User user = repo.findByUsername(this.username);
	//
	// System.out.println(user.getCompanyID());
	// model.addAttribute("user",user);
	//
	// return "accountBus";
	// }

	@GetMapping("/remove_duplicates")
	public String removeDuplicates(RedirectAttributes ra) {

		ra.addFlashAttribute("message", "Failed to remove duplicates");

		return "redirect:/main";

	}

	@GetMapping("/fill_missing")
	public String fillMissingValues(RedirectAttributes ra) {

		ra.addFlashAttribute("message", "Failed to fill in missing values");

		return "redirect:/main";

	}
}
