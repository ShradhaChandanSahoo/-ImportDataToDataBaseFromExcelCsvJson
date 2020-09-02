package com.shradha.DataImport.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.shradha.DataImport.model.User;
import com.shradha.DataImport.repository.SpringReadFileRepository;

@Service
@Transactional
public class SpringReadFileServiceImpl implements SpringReadFileService {

	@Autowired
	private  SpringReadFileRepository springReadFileRepository;

	@Override
	public List<User> findAll() {
		
		return (List<User>) springReadFileRepository.findAll();
	}

	@Override
	public boolean saveDataFromUploadFile(MultipartFile file) {
		System.out.println("=============Inside saveDataFromUploadFile===================");
		
		boolean isFlag = false;
		System.out.println("===========Original FinalName============="+file.getOriginalFilename());
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		System.out.println("============Extension Name is============"+extension);
		if(extension.equalsIgnoreCase("json")) {
			System.out.println("==============Inside Json============");
			System.out.println("================File=========="+file);
			isFlag = readDataFromJson(file);
		}
		else if (extension.equalsIgnoreCase("csv")) {
			isFlag = readDataFromCsv(file);
		}
		else if (extension.equalsIgnoreCase("xls")  || extension.equalsIgnoreCase("xlsx")) {
			isFlag = readDataFromExcel(file);
		}
		return isFlag;
	}

	private boolean readDataFromExcel(MultipartFile file) {
		Workbook workbook = getWookBook(file);
		Sheet sheet =workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.iterator();
		rows.next();
		while(rows.hasNext()) {
			Row row = rows.next();
			User user = new User();
			if(row.getCell(0).getCellType()== CellType.STRING) {
				user.setFirstName(row.getCell(0).getStringCellValue());
			}
			if(row.getCell(1).getCellType()== CellType.STRING) {
				user.setLastName(row.getCell(1).getStringCellValue());
			}
			if(row.getCell(2).getCellType()== CellType.STRING) {
				user.setEmail(row.getCell(2).getStringCellValue());
			}
			if(row.getCell(3).getCellType()== CellType.NUMERIC) {
				String phoneNumber = NumberToTextConverter.toText(row.getCell(3).getNumericCellValue());
				user.setPhoneNumber(phoneNumber);
			}else if(row.getCell(3).getCellType()== CellType.STRING) {
				user.setPhoneNumber(row.getCell(3).getStringCellValue());
			}
			user.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
			springReadFileRepository.save(user);
		}
		return true;
	}

	private Workbook getWookBook(MultipartFile file) {
		
		Workbook workbook = null;
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		
		try {
			if(extension.equalsIgnoreCase("xlsx")) {
				
				workbook = new XSSFWorkbook(file.getInputStream());
				
			}else if (extension.equalsIgnoreCase("xls")) {
				
				workbook = new HSSFWorkbook(file.getInputStream());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return workbook;
	}

	private boolean readDataFromCsv(MultipartFile file) {
		
		try {
			InputStreamReader reader = new InputStreamReader(file.getInputStream());
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			System.out.println(csvReader);
			List<String[]> rows = csvReader.readAll();
			System.out.println(rows);
			for(String[] row : rows) {
				System.out.println(row[0]);
				springReadFileRepository.save(new User(row[0], row[1], row[2], row[3],FilenameUtils.getExtension(file.getOriginalFilename())));
				System.out.println("==========Saved==========");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	private boolean readDataFromJson(MultipartFile file) {
		
		try {
			System.out.println("==============Inside readDataFromJson======================");
			InputStream inputStream = file.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			List<User> users = Arrays.asList(mapper.readValue(inputStream, User[].class));
			System.out.println(users);
			if(users!=null && users.size()>0) {
				for(User user : users) {
					
					user.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
					System.out.println(user);
					springReadFileRepository.save(user);
					System.out.println("===========Data Is Saved");
					
				}
			}
			return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
}
