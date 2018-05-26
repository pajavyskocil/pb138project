package cz.fi.muni.CIA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.itextpdf.html2pdf.HtmlConverter;

import cz.fi.muni.CIA.Exceptions.GeneratorException;
import cz.fi.muni.CIA.entities.Invoice;

public class PDFGenerator {
    private static String xmlToHtml(String xmlData, String xsltPath) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File(xsltPath));
            Transformer transformer = factory.newTransformer(xslt);
            Source text = new StreamSource(new StringReader(xmlData));

            StringWriter writer = new StringWriter();
            transformer.transform(text, new StreamResult(writer));

            return writer.toString();

        } catch(TransformerConfigurationException e) {
            throw new GeneratorException("Error occured during XSL Transformer configuration: " + e.toString());
        } catch(TransformerException e) {
            throw new GeneratorException("Error occured during XSL Transformation: " + e.toString());
        }
    }

    public static void invoiceToPDF(Invoice invoice) {
        StringBuilder sb = new StringBuilder();
        sb.append(invoice.getXmlRepresentation());
        sb.append(invoice.getPayer().getXmlRepresentation());
        sb.append(invoice.getRecipient().getXmlRepresentation());

        String htmlData = xmlToHtml("<pdfData>"+sb.toString()+"</pdfData>", "CIA-persistence/src/main/resources/xml-to-html.xsl");

        try (FileOutputStream fos = new FileOutputStream("CIA-persistence/pdf/invoice"+invoice.getId().toString()+".pdf")){
			HtmlConverter.convertToPdf(htmlData, fos);

		} catch (IOException e) {
			throw new GeneratorException("IO Error occured during PDF generation: " + e.toString());
		}
    }

    public static void batchInvoiceToPDF(List<Invoice> invoices) {
        for(Invoice invoice : invoices) {
            invoiceToPDF(invoice);
        }
    }
}