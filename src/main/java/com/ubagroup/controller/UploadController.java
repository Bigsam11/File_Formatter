package com.ubagroup.controller;


import java.io.*;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


import com.ubagroup.dao.FileDao;
import com.ubagroup.model.File;
import com.ubagroup.service.FileService;
import oracle.sql.BLOB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import com.ubagroup.service.impl.FileServiceImpl;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ubagroup.util.FileSystemStorageService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UploadController {
	
	@Autowired
	private FileSystemStorageService storageService;

	@Autowired
	private FileService fileService;
//	private DataHandler context;

	@Value("${uploadedFiles}")
	private String uploadedFilesPath;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView upload() {
		return new ModelAndView("upload");
	}



	@RequestMapping(value = "/files/list", method = RequestMethod.GET)
	public String listFiles(Model model) {
		List<Path> lodf = new ArrayList<>();
		List<File> uris = new ArrayList<>();
		
		try {
			lodf = storageService.listSourceFiles(storageService.getUploadLocation());
			for(Path pt : lodf) {
				File fl = new File();
				fl.setFile_location(MvcUriComponentsBuilder
						.fromMethodName(UploadController.class, "serveFile", pt.getFileName().toString() )
						.build()
						.toString());
				
				fl.setFile_name(pt.getFileName().toString());
				uris.add(fl);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("listOfEntries", uris);
		return "file_list :: urlFileList";
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
	Date curDate = new Date();
	String strDate = sdf.format(curDate);
	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@RequestMapping(value = "/files/upload", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request) throws IOException {
		storageService.store(file);

		java.io.File convFile = new java.io.File(file.getOriginalFilename());
		convFile.createNewFile();
		FileInputStream inputStream = new FileInputStream(convFile);
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();

String newFileName = uploadedFilesPath+ "\\" + "output_" + file.getOriginalFilename();
System.out.println("Output file path is: " + newFileName);
		FileWriter fileWriter = new FileWriter( newFileName);
	//	InputStream stream = part.getInputStream();

		Charset charset = Charset.forName("UTF-8");
		BufferedReader br = new BufferedReader(new FileReader(convFile));

		String st;
		int count = 1;
		String closingBal = "";
		while ((st = br.readLine()) != null) {
			if (st.contains("F61")) {
				String vd = br.readLine().substring(38, 49);
				String[] dcm = br.readLine().split("\\s+");
				String dc = dcm[4];
				String[] amt = br.readLine().split("\\s+");
				String amount = amt[2].replace(",", ".");
				String[] tt = br.readLine().split("\\s+");
				String tranType = tt[3];
				String[] idc = br.readLine().split("\\s+");
				String idCode = idc[3];
				String[] rao = br.readLine().split("\\s+");
				String refAcctOwn = rao[6];
				String[] rasi = br.readLine().split("\\s+");
				String refAcctServIns = rasi[7];
				String write = vd + "," + dc + "," + amount + "," + tranType + "," + idCode + "," + refAcctOwn + ","
						+ refAcctServIns + "\n";
				fileWriter.write(write);

				count++;
			}
			if (st.contains("F62M")) {
				br.readLine();
				br.readLine();
				br.readLine();
				String[] amt = br.readLine().split("\\s+");
				closingBal = amt[2].replace(",", ".");
			}
		}



		fileWriter.write("Closing_Balance," + closingBal);
		fileWriter.close();
		br.close();

//		FileService fileService = context.getBean(FileService.class);
//
//		fileService.insertFile();
//
//		List<File> files = new ArrayList<>();
//		fileService.insertFiles(files);

//		fileService.getAllFiles();
		File obje = new File();
		obje.setFile_name(file.getOriginalFilename());
		obje.setFile_content((Blob) inputStream);
//		obje.setFile_content(file.getOriginalFilename());
//		obje.setHrefSize(file.getSize());
		obje.setInputter_name("Tobiloba");

		fileService.insertFile(obje);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename());
		return "redirect:/";
	}
}
