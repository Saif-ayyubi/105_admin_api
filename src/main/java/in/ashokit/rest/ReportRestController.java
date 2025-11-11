package in.ashokit.rest;

import in.ashokit.dto.ReportFilterDto;
import in.ashokit.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/orders/report")
    public ResponseEntity<byte[]> generateExcelReport(@RequestBody ReportFilterDto reportFilterDto) throws Exception {//to download Excel file

        byte[] excelData = reportService.generateReport(reportFilterDto);//excel data in byte array

        String fileName = "Orders_Report_" + System.currentTimeMillis() + ".xls";
        //System.currentTimeMillis()==> when this report created (time)
        //".xls" ==> file extension(how do you want to save this file)

        HttpHeaders headers = new HttpHeaders();//creating HttpHeaders to download file directly
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));//setting excel document directly for downloading
        headers.setContentDispositionFormData("attachment", fileName);//sending data as attachment
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);

    }
}
