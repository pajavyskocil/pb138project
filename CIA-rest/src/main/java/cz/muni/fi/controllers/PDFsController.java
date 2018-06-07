package cz.muni.fi.controllers;

import cz.fi.muni.CIA.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Controller for pages displaying statistics.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class PDFsController {

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @RequestMapping(value = "/exportAddressBook")
    public ResponseEntity<byte[]> exportAddressBook() {
        byte[] contents = pdfGeneratorService.addressBookToPDF();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "contacts.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/exportInvoices")
    public ResponseEntity<byte[]> exportInvoices( @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
                                  @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to) {
        byte[] contents = pdfGeneratorService.invoicesToPDF(from, to);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "invoices" + from + "_" + to + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/exportInvoice")
    public ResponseEntity<byte[]> exportInvoice(@RequestParam Long id) {
        byte[] contents = pdfGeneratorService.invoiceToPDF(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = "invoice_" + id + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }
}
