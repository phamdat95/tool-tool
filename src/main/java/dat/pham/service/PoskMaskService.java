package dat.pham.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import dat.pham.api.PoskMarkAPI;
import dat.pham.api.dto.poskmask.POResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PoskMaskService {
	private static String[] columns = {"Name", "Email", "Date Of Birth", "Salary"};
	private static final String[] listAuStates= {"NSW", "QLD","SA","TAS","VIC","WA"};
	private static final int SUBMIT_LIST_OWNERSHIP_SIZE = 20;
	private static final int SUBMIT_LIST_OWNERSHIP_HORSE_ID_COLUMN = 0;
	private static final int SUBMIT_LIST_OWNERSHIP_HORSE_NAME_COLUMN = 1;
	private static final int SUBMIT_LIST_OWNERSHIP_OWNER_ID_COLUMN = 2;
	private static final int SUBMIT_LIST_OWNERSHIP_EMAIL_COLUMN = 3;
	private static final int SUBMIT_LIST_OWNERSHIP_FINANCIAL_EMAIL_COLUMN = 4;
	private static final int SUBMIT_LIST_OWNERSHIP_FIRST_NAME_COLUMN = 5;
	private static final int SUBMIT_LIST_OWNERSHIP_LAST_NAME_COLUMN = 6;
	private static final int SUBMIT_LIST_OWNERSHIP_DISPLAY_NAME_COLUMN = 7;
	private static final int SUBMIT_LIST_OWNERSHIP_TYPE_COLUMN = 8;
	private static final int SUBMIT_LIST_OWNERSHIP_MOBILE_COLUMN = 9;
	private static final int SUBMIT_LIST_OWNERSHIP_PHONE_COLUMN = 10;
	private static final int SUBMIT_LIST_OWNERSHIP_FAX_COLUMN = 11;
	private static final int SUBMIT_LIST_OWNERSHIP_ADDRESS_COLUMN = 12;
	private static final int SUBMIT_LIST_OWNERSHIP_CITY_COLUMN = 13;
	private static final int SUBMIT_LIST_OWNERSHIP_STATE_COLUMN = 14;
	private static final int SUBMIT_LIST_OWNERSHIP_POSTCODE_COLUMN = 15;
	private static final int SUBMIT_LIST_OWNERSHIP_COUNTRY_COLUMN = 16;
	private static final int SUBMIT_LIST_OWNERSHIP_GST_COLUMN = 17;
	private static final int SUBMIT_LIST_OWNERSHIP_SHARES_COLUMN = 18;
	private static final int SUBMIT_LIST_OWNERSHIP_FROM_DATE_COLUMN = 19;

	private static final int ARDEX_LIST_OWNERSHIP_HORSE_NAME_COLUMN = 0;
	private static final int ARDEX_LIST_OWNERSHIP_SHARES_COLUMN = 1;
	private static final int ARDEX_LIST_OWNERSHIP_FROM_DATE_COLUMN = 2;
	private static final int ARDEX_LIST_OWNERSHIP_DISPLAY_NAME_COLUMN = 3;
	private static final int ARDEX_LIST_OWNERSHIP_ADDRESS_COLUMN = 4;
	private static final int ARDEX_LIST_OWNERSHIP_PHONE_COLUMN = 5;
	private static final int ARDEX_LIST_OWNERSHIP_FAX_COLUMN = 6;
	private static final int ARDEX_LIST_OWNERSHIP_MOBILE_COLUMN = 7;
	private static final int ARDEX_LIST_OWNERSHIP_EMAIL_COLUMN = 8;
	private static final int ARDEX_LIST_OWNERSHIP_GST_COLUMN = 9;
	@Autowired
	private PoskMarkAPI icabbiAPI;

	public void toolDucAnh(MultipartFile csvFile) throws IOException {
		InputStream inputStream = csvFile.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

		CSVReader reader = new CSVReader(inputStreamReader);
		String[] header = {"Horse ID", "Horse Name", "OwnerID", "Email","Financial Email", "First Name", "Last Name",
				"Display Name", "Type", "Mobile", "Phone", "Fax", "Address", "City", "State", "Postcode",
				"Country", "GST", "Shares", "From Date", "To Date"};
		List<String[]> nextLines = reader.readAll();
		nextLines.remove(0);
		List<String[]> datas = new ArrayList<>();

		for (int index = 0; index < nextLines.size()-1; index++) {
			String[] lineDataSubmits = new String[SUBMIT_LIST_OWNERSHIP_SIZE];

			if(index > 0) {
				String[] lineSplitValues = nextLines.get(index);
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_HORSE_ID_COLUMN] = "";
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_HORSE_NAME_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_HORSE_NAME_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_OWNER_ID_COLUMN] = "";
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_EMAIL_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_EMAIL_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_FINANCIAL_EMAIL_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_EMAIL_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_FIRST_NAME_COLUMN] = "";
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_LAST_NAME_COLUMN] = "";
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_DISPLAY_NAME_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_DISPLAY_NAME_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_TYPE_COLUMN] = "owner";
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_MOBILE_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_MOBILE_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_PHONE_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_PHONE_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_FAX_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_FAX_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_ADDRESS_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_ADDRESS_COLUMN];
				String[] splitAddresses = lineDataSubmits[SUBMIT_LIST_OWNERSHIP_ADDRESS_COLUMN].trim().split(" ");
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_CITY_COLUMN] = "";
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_STATE_COLUMN] = "";

				if (!StringUtils.isEmpty(lineDataSubmits[SUBMIT_LIST_OWNERSHIP_ADDRESS_COLUMN])) {
					for (String splitAddress : splitAddresses) {
						boolean isAuStatePresent;
						isAuStatePresent = Arrays.stream(listAuStates).anyMatch(l -> l.equalsIgnoreCase(splitAddress));
						if(isAuStatePresent) {
							lineDataSubmits[SUBMIT_LIST_OWNERSHIP_STATE_COLUMN] = splitAddress;
							break;
						}else {
							lineDataSubmits[SUBMIT_LIST_OWNERSHIP_STATE_COLUMN] ="";
						}
					}
					lineDataSubmits[SUBMIT_LIST_OWNERSHIP_POSTCODE_COLUMN] = "";
					lineDataSubmits[SUBMIT_LIST_OWNERSHIP_COUNTRY_COLUMN] = "";
				}

				if (!StringUtils.isEmpty(lineSplitValues[ARDEX_LIST_OWNERSHIP_GST_COLUMN])) {
					if (lineSplitValues[ARDEX_LIST_OWNERSHIP_GST_COLUMN].equalsIgnoreCase("N")) {
						lineDataSubmits[SUBMIT_LIST_OWNERSHIP_GST_COLUMN] = "FALSE";
					} else if (lineSplitValues[ARDEX_LIST_OWNERSHIP_GST_COLUMN].equalsIgnoreCase("Y")) {
						lineDataSubmits[SUBMIT_LIST_OWNERSHIP_GST_COLUMN] = "TRUE";
					} else {
						lineDataSubmits[SUBMIT_LIST_OWNERSHIP_GST_COLUMN] = "";
					}
				}
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_SHARES_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_SHARES_COLUMN];
				lineDataSubmits[SUBMIT_LIST_OWNERSHIP_FROM_DATE_COLUMN] = lineSplitValues[ARDEX_LIST_OWNERSHIP_FROM_DATE_COLUMN];
			}

			datas.add(lineDataSubmits);
		}

		File file = new File("/home/dat/Desktop/dat.csv");
		FileOutputStream fileOut = new FileOutputStream(file);
		PrintWriter out = new PrintWriter(fileOut);
		CSVWriter csvWriter = new CSVWriter(out);
		csvWriter.writeNext(header);
		csvWriter.writeAll(datas);
		csvWriter.flush();
		csvWriter.close();
	}

	public void get(MultipartFile file) {
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
		Sheet xsheet = workbook.getSheetAt(0);
		int totalRows = xsheet.getLastRowNum();
		for (int row = 1; row <= totalRows; row++) {
			String recipient = null;
			String subject = null;
			Row xrow = xsheet.getRow(row);
			int totalCells = xrow.getLastCellNum();
			for (int cell = 0; cell < totalCells; cell++) {
				if (xrow != null) {
					Cell xcell = xrow.getCell(cell, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (xcell != null) {
						switch (xcell.getCellType()) {
							case STRING:
								if (cell == 3) {
									recipient = xcell.getStringCellValue();
								}
								if (cell == 4)
									subject = xcell.getStringCellValue();
								break;
							default:
								break;
						}
					}
				}
			}
			String status = null;
			String messageId = null;
			try {
				List<POResponse> mr = icabbiAPI.get(recipient, "50", "0", "2019-09-01", "2019-09-30", subject)
						.execute().body().getMessages();

				if (!CollectionUtils.isEmpty(mr)) {
					status = mr.get(0).getStatus();
					messageId = mr.get(0).getMessageId();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Cell messageIdCell = xrow.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (messageIdCell == null) {
				messageIdCell = xrow.createCell(8);
				messageIdCell.setCellType(CellType.STRING);
				messageIdCell.setCellValue(messageId);
			}
			Cell statusCell = xrow.getCell(19, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (statusCell == null) {
				statusCell = xrow.createCell(19);
				statusCell.setCellType(CellType.STRING);
				statusCell.setCellValue(status);
			}
		}
		FileOutputStream fileOut = new FileOutputStream("/home/dat/Desktop/poi-generated-file.xlsx");
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getXLSX() throws IOException {
		Workbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("Contact");

		Font headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Row headerRow = sheet.createRow(0);

		// Create cells
		for(int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

		Row row = sheet.createRow(1);

		row.createCell(0)
				.setCellValue("dat");

		row.createCell(1)
				.setCellValue("\'0934");

		Cell dateOfBirthCell = row.createCell(2);
		dateOfBirthCell.setCellValue(new Date());
		dateOfBirthCell.setCellStyle(dateCellStyle);

		row.createCell(3)
				.setCellValue("1t");
		for(int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		FileOutputStream fileOut = new FileOutputStream("/home/dat/Desktop/poi-generated-file.xlsx");
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

}
