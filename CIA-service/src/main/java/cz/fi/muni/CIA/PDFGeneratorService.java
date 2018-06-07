package cz.fi.muni.CIA;

import java.time.LocalDate;

/**
 * Interface for PDFGeneratorService
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public interface PDFGeneratorService {

    /**
     * Convert invoice to PDF
     * @param invoiceId id of invoice to be converted
     * @return PDF in array of bytes
     */
    byte[] invoiceToPDF(Long invoiceId);

    /**
     * Convert invoices in dates to PDF
     * @param oldest from what date invoices are included
     * @param newest to what date invoices are included
     * @return PDF in array of bytes
     */
    byte[] invoicesToPDF(LocalDate oldest, LocalDate newest);

    /**
     * Convert all persons to PDF
     * @return PDF in array of bytes
     */
    byte[] addressBookToPDF();
}
