package cz.fi.muni.CIA;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
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
import cz.fi.muni.CIA.entities.Person;

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

    public static byte[] invoiceToPDF(Invoice invoice) {
        StringBuilder sb = new StringBuilder();
        sb.append(invoice.getXmlRepresentation());
        sb.append(invoice.getPayer().getXmlRepresentation());
        sb.append(invoice.getRecipient().getXmlRepresentation());

        String htmlData = xmlToHtml("<pdfData>"+sb.toString()+"</pdfData>", ".XSL/invoice2html.xsl");

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
            HtmlConverter.convertToPdf(htmlData, os);
            return os.toByteArray();
		} catch (IOException e) {
			throw new GeneratorException("IO Error occured while generating PDF: " + e.toString());
		}
    }

    public static byte[] addressBookToPDF(List<Person> people) {
        StringBuilder sb = new StringBuilder();
        for(Person person : people) {
            sb.append(person.getXmlRepresentation());
        }

        String htmlData = xmlToHtml("<pdfData><addressBook>" + sb.toString() + "</addressBook></pdfData>", ".XSL/ab2html.xsl");

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
			HtmlConverter.convertToPdf(htmlData, os);
			return os.toByteArray();
		} catch (IOException e) {
			throw new GeneratorException("IO Error occured while generating PDF: " + e.toString());
		}
    }

    public static byte[] flowToPDF(List<Invoice> invoices, String mode, LocalDate from, LocalDate to) {
        if (!Arrays.asList("income","expense").contains(mode)) { mode = "balance"; }
        StringBuilder sb = new StringBuilder();
        for(Invoice invoice : invoices) {
            sb.append("<flowItem>")
                    .append(invoice.getXmlRepresentation())
                    .append(invoice.getPayer().getXmlRepresentation())
                    .append(invoice.getRecipient().getXmlRepresentation())
                    .append("</flowItem>");
        }
        String xmlData = "<pdfData><range><from>"+from.toString()+"</from><to>"+to.toString()+"</to></range><" + mode + ">" + sb.toString() + "</"+mode+"></pdfData>";
        String htmlData = xmlToHtml(xmlData, ".XSL/flow2html.xsl");

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()){
            HtmlConverter.convertToPdf(htmlData, os);
            return os.toByteArray();
		} catch (IOException e) {
			throw new GeneratorException("IO Error occured while generating PDF: " + e.toString());
		}
    }

    public static void batchInvoiceToPDF(List<Invoice> invoices) {
        for(Invoice invoice : invoices) {
            invoiceToPDF(invoice);
        }
    }
}