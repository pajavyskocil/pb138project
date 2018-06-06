package cz.fi.muni.CIA;

import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.Person;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    private InvoiceService invoiceService;
    private PersonService personService;

    @Inject
    public PDFGeneratorServiceImpl(InvoiceService invoiceService, PersonService personService) {
        this.invoiceService = invoiceService;
        this.personService = personService;
    }

    @Override
    public byte[] invoiceToPDF(Long invoiceId) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        return PDFGenerator.invoiceToPDF(invoice);
    }

    @Override
    public byte[] invoicesToPDF(LocalDate oldest, LocalDate newest) {
        List<Invoice> invoices = invoiceService.getInvoicesInDateInterval(oldest, newest);
        return PDFGenerator.flowToPDF(invoices, "balance", oldest, newest);
    }

    @Override
    public byte[] addressBookToPDF() {
        List<Person> persons = personService.getAllPersons();
        return PDFGenerator.addressBookToPDF(persons);
    }
}
