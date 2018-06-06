package cz.fi.muni.CIA;

import java.time.LocalDate;

public interface PDFGeneratorService {

    byte[] invoiceToPDF(Long invoiceId);

    byte[] invoicesToPDF(LocalDate oldest, LocalDate newest);

    byte[] addressBookToPDF();
}
