package cz.muni.fi;

import cz.fi.muni.CIA.entities.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for operations connected to Invoices.
 * Contains mappings for CRUD operations with Invoice entity.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class InvoiceController {
    
    @Autowired
    //private InvoiceService invoiceService;

    /* Entry point into manager */
    @RequestMapping(value = "/listInvoices", method = RequestMethod.GET)
    public String showInvoices() {
        return "listInvoices";
    }

    /* Let user choose parameters for displayed invoices */
    @RequestMapping(value = "/listInvoices", method = RequestMethod.POST)
    public String showInvoices(Model model) {
        //todo implement - list all finances from db created in chosen date period
        return "listInvoices";
    }

    /* Start creating new Invoice - contains form to define new entry */
    @RequestMapping(value = "/createInvoice", method = RequestMethod.GET)
    public String startAddInvoice(Model model) {
        model.addAttribute("invoice", new Invoice());
        return "invoiceDetail";
    }

    /* Start editing Invoice - contains form with pre-filled details that can be edited */
    @RequestMapping(value = "/editInvoice", method = RequestMethod.GET)
    public String startEditInvoice(Model model) {
        //todo implement - get invoice from db by ID
        return "invoiceDetail";
    }

    /* Start deleting person - contains form with pre-filled details, user has to approve deletion */
    @RequestMapping(value = "/deleteInvoice", method = RequestMethod.GET)
    public String startDeleteInvoice(Model model) {
        //todo implement - get invoice from db by ID
        return "invoiceDetail";
    }

    /* Show error */
    @RequestMapping(value = "/invoiceOperationFailed", method = RequestMethod.GET)
    public String invoiceOperationFailed() {
        return "invoiceDetail";
    }

    /* Processing of creating new Invoice entry */
    @RequestMapping(value = "/createInvoice", method = RequestMethod.POST)
    public String addInvoice(@ModelAttribute Invoice invoice, RedirectAttributes redirectAttributes,
                            SessionStatus sessionStatus) {
        //TODO implement - create invoice
        String message, viewName;
        try {
            //addressBookService.create(invoice);
            message = "Invoice created. Invoice id :" + invoice.getId();
            viewName = "redirect:/listInvoices";
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Invoice create failed";
            viewName = "redirect:/invoiceOperationFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }

    /* Processing of editing Invoice entry */
    @RequestMapping(value = "/editInvoice", method = RequestMethod.POST)
    public String editInvoice(@ModelAttribute Invoice invoice, RedirectAttributes redirectAttributes,
                             SessionStatus sessionStatus) {
        //TODO implement - edit invoice
        String message, viewName;
        try {
            //addressBookService.edit(invoice);
            message = "Invoice edited. Invoice id :" + invoice.getId();
            viewName = "redirect:/listInvoices";
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Invoice edit failed";
            viewName = "redirect:/invoiceOperationFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }

    /* Processing of deleting Invoice entry */
    @RequestMapping(value = "/deleteInvoice", method = RequestMethod.POST)
    public String deleteInvoice(@ModelAttribute Invoice invoice, RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus) {
        //TODO implement - delete invoice
        String message, viewName;
        try {
            //addressBookService.delete(invoice);
            message = "Invoice deleted. Invoice id :" + invoice.getId();
            viewName = "redirect:/listInvoices";
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Invoice delete failed";
            viewName = "redirect:/invoiceOperationFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }
}
