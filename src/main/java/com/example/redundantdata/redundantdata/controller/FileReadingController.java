package com.example.redundantdata.redundantdata.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.redundant.webdata.GettingData;

@RestController
@RequestMapping(value="/change")
public class FileReadingController {

	
	GettingData gettingData=new GettingData();
	
	/*@RequestMapping(value="/abc",method=RequestMethod.GET )
	public void download(HttpServletResponse response) throws FileNotFoundException, IOException
	{
		File file=new File("C:\\Users/saurav.kumar/Downloads/Book2.xls");
		
	    // xls file
	    response.addHeader("Content-disposition", "attachment;filename=sample.xls");
	    response.setContentType("application/octet-stream");

	    // Copy the stream to the response's output stream.
	    IOUtils.copy(new FileInputStream(file), response.getOutputStream());
	    response.flushBuffer();
	}
	*/
	
	
	
	
	@RequestMapping(value="/",method=RequestMethod.POST )
	public  void fileHandling(@RequestParam("file") MultipartFile file,HttpServletResponse response) throws IOException, InvalidFormatException
	{
	System.out.println("hi there");

	  POIFSFileSystem fs = null;
	  Sheet sheet = null;
	  
	  Workbook wb = WorkbookFactory.create(new ByteArrayInputStream(file.getBytes()));
	
	  sheet=wb.getSheetAt(0);
	  
	  int rows = sheet.getPhysicalNumberOfRows();
	 
	  Workbook wb2 = new HSSFWorkbook();
	  
	  Sheet sheet2 = wb2.createSheet("newfile");
	  
	  ArrayList<String> dupList = new ArrayList<String>();
	  
	  
//	  StringBuilder url=new StringBuilder("https://www.");
	   System.out.println(rows);
	   int rowCount = 0;
	   for(Row row:sheet)
	   {
		   
		   Row rownew = sheet2.createRow(rowCount++);
		   int columnCount = 0;
		   for(Cell cell:row)
		   {

			   if(dupList.contains(cell.getStringCellValue()))
					   {
				   row.removeCell(cell);
				   rowCount--;
					   }
			   else
			   {
				   dupList.add(cell.getStringCellValue());
				   Cell cellnew1 = rownew.createCell(columnCount++);
				   cellnew1.setCellValue(cell.getStringCellValue());
				  
				 
		   }
			 
			   System.out.println(cell.getStringCellValue());
		   }
	   }
	  /* HashMap<Integer,String> hm=new HashMap<Integer,String>();  
	   hm.put(100,"Amit");  
	   hm.put(101,"Vijay");  
	  
	   rowCount=0;
	   for(Map.Entry m:hm.entrySet()){  
		   Row row=sheet2.getRow(rowCount);
		   int columnCount=1;
		   Cell cellnew2 = row.createCell(columnCount++);
		   cellnew2.setCellValue(m.getValue().toString());
		   rowCount++;
		  
	  
	   }  
	 */
	 
	  for(Row row:sheet2)
	   {
		  int columnCount=0;
		  StringBuilder url=new StringBuilder("https://www.");
		  Cell cell=row.getCell(0);
		  String email=cell.getStringCellValue();
		  String[] arrOfStr = email.split("@");
		  url.append(arrOfStr[1]);
		 Map<String,String> data= gettingData.getWebData(url.toString());
		Cell cellnew2 = row.createCell(columnCount++);
		   cellnew2.setCellValue(data.get("title"));
		   
		   
		   Cell cellnew3 = row.createCell(columnCount++);
		   cellnew3.setCellValue(data.get("keywords")); 
		   
		   
		   
		   Cell cellnew4 = row.createCell(columnCount++);
		   cellnew4.setCellValue(data.get("description")); 
		 
		 System.out.println(data);
		 System.out.println("data received");
		  
	   }
	   
	 /* 
	 FileOutputStream fos=new FileOutputStream("C:\\Users/saurav.kumar/Desktop/saurav.xls");
	 wb2.write(fos);
	 fos.close();*/
	  
	   HttpHeaders headers = new HttpHeaders();
     
   headers.add("Content-Disposition", String.format("attachment; filename=test.xls", "abc"));
   headers.add("Cache-Control", "no-cache, no-store, must-revalidate");   headers.add("Pragma", "no-cache");
  headers.add("Expires", "0");
  response.addHeader("Content-disposition", "attachment;filename=newfile2.xls");
	   response.setContentType("application/octet-stream");
	   wb2.write(response.getOutputStream());
	  
	   
	}

}
