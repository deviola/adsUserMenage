package com.ostasz.ads.user.service.export;

import com.ostasz.ads.user.datamodel.entity.Contact;
import com.ostasz.ads.user.datamodel.entity.User;
import com.ostasz.ads.user.exception.FileExportException;
import com.ostasz.ads.user.repository.UserRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsExportService {

    private final String SHEET_NAME = "Users";
    private final UserRepository userRepository;

    public UserDetailsExportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Resource exportUsersToFile() throws FileExportException {
        try {
            List<User> users = Optional.ofNullable(userRepository.findAllWithContact())
                    .orElse(Collections.emptyList());
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            createHeaderRows(sheet);

            if (!users.isEmpty()) {
                fillInUserDetails(sheet, users);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes);

            return new InputStreamResource(inputStream);
        } catch (Exception e) {
            throw new FileExportException();
        }
    }

    private void createHeaderRows(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("First Name");
        headerRow.createCell(1).setCellValue("Last Name");
        headerRow.createCell(2).setCellValue("PESEL");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Private phone number");
        headerRow.createCell(5).setCellValue("Business phone number");
        headerRow.createCell(6).setCellValue("Home address");
        headerRow.createCell(7).setCellValue("Registered Address");
    }

    private void fillInUserDetails(Sheet sheet, List<User> users) {
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);

            Contact userContact = user.getContact();
            fillInCells(row, user, userContact);
        }

        for (int i = 0; i <= 8; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void fillInCells(Row row, User user, Contact contact) {
        row.createCell(0).setCellValue(user.getFirstName() != null ? user.getFirstName() : "");
        row.createCell(1).setCellValue(user.getLastName() != null ? user.getLastName() : "");
        row.createCell(2).setCellValue(user.getPesel());

        if (contact != null) {
            row.createCell(3).setCellValue(contact.getEmail() != null ? contact.getEmail() : "");
            row.createCell(4).setCellValue(contact.getPrivatePhoneNumber() != null ? contact.getPrivatePhoneNumber() : "");
            row.createCell(5).setCellValue(contact.getBusinessPhoneNumber() != null ? contact.getBusinessPhoneNumber() : "");
            row.createCell(6).setCellValue(contact.getHomeAddress() != null ? contact.getHomeAddress() : "");
            row.createCell(7).setCellValue(contact.getRegisteredAddress() != null ? contact.getRegisteredAddress() : "");
        }
    }

}
