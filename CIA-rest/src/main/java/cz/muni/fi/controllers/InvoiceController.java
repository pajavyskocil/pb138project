package cz.muni.fi.controllers;

import cz.fi.muni.CIA.InvoiceService;
import cz.fi.muni.CIA.OwnerService;
import cz.fi.muni.CIA.PersonService;
import cz.fi.muni.CIA.entities.Invoice;
import cz.fi.muni.CIA.entities.InvoiceType;
import cz.fi.muni.CIA.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controller for operations connected to Invoices.
 * Contains mappings for CRUD operations with Invoice entity.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class InvoiceController {
    
    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PersonService personService;

    @Autowired
    private OwnerService ownerService;

    /* Entry point into manager */
    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    public String showInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    /* Let user choose parameters for displayed invoices */
    @RequestMapping(value = "/invoices", method = RequestMethod.GET, params = {"listType"})
    public String showInvoicesByType(Model model, @RequestParam String listType) {
        InvoiceType invoiceType = listType.equals("expense") ? InvoiceType.EXPENSE : InvoiceType.INCOME;
        model.addAttribute("invoices", invoiceService.getInvoicesByType(invoiceType));
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    /* Let user choose parameters for displayed invoices */
    @RequestMapping(value = "/invoices", method = RequestMethod.GET, params = {"personId"})
    public String showInvoicesByUser(Model model, @RequestParam Long personId) {
        model.addAttribute("invoices", invoiceService.getInvoicesByPerson(personId));
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.GET, params = {"oldest", "newest"})
    public String showInvoicesInDates(Model model, @RequestParam String oldest, @RequestParam String newest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oldestDate = LocalDate.parse(oldest, formatter);
        LocalDate newestDate = LocalDate.parse(newest, formatter);
        model.addAttribute("invoices", invoiceService.getInvoicesInDateInterval(oldestDate, newestDate));
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    @RequestMapping(value = "/invoices", method = RequestMethod.GET, params = {"oldest", "newest", "listType"})
    public String showInvoicesByTypeAndDate(Model model, @RequestParam String listType, @RequestParam String oldest,
                                            @RequestParam String newest) {
        InvoiceType invoiceType = listType.equals("expense") ? InvoiceType.EXPENSE : InvoiceType.INCOME;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oldestDate = LocalDate.parse(oldest, formatter);
        LocalDate newestDate = LocalDate.parse(newest, formatter);
        model.addAttribute("invoices", invoiceService.getInvoicesByTypeAndDate(oldestDate, newestDate, invoiceType));
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    /* Let user choose parameters for displayed invoices */
    @RequestMapping(value = "/invoices", method = RequestMethod.GET, params = {"oldest", "newest", "personId"})
    public String showInvoicesByUserAndDate(Model model, @RequestParam Long personId, @RequestParam String oldest,
                                            @RequestParam String newest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oldestDate = LocalDate.parse(oldest, formatter);
        LocalDate newestDate = LocalDate.parse(newest, formatter);
        model.addAttribute("invoices", invoiceService
                .getInvoicesByPersonIdAndDate(oldestDate, newestDate, personId));
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    /* Let user choose parameters for displayed invoices */
    @RequestMapping(value = "/invoices", method = RequestMethod.GET, params = {"oldest", "newest", "personId", "listType"})
    public String showInvoicesByUserTypeAndDate(Model model, @RequestParam Long personId, @RequestParam String listType,
                                                @RequestParam String oldest, @RequestParam String newest) {
        InvoiceType invoiceType = listType.equals("expense") ? InvoiceType.EXPENSE : InvoiceType.INCOME;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate oldestDate = LocalDate.parse(oldest, formatter);
        LocalDate newestDate = LocalDate.parse(newest, formatter);
        model.addAttribute("invoices", invoiceService
                .getInvoicesByPersonIdAndTypeAndDate(oldestDate, newestDate, personId, invoiceType));
        model.addAttribute("title", "Invoices management");
        model.addAttribute("persons", personService.getAllPersons());

        return "invoices";
    }

    /* Start creating new Invoice - contains form to define new entry */
    @RequestMapping(value = "/createInvoice", method = RequestMethod.GET)
    public String startAddInvoice(Model model) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("persons", personService.getAllPersons());
        model.addAttribute("dateToday", LocalDate.now());
        model.addAttribute("action", "createInvoice");
        model.addAttribute("title", "Create invoice");

        return "invoiceDetail";
    }

    /* Start editing Invoice - contains form with pre-filled details that can be edited */
    @RequestMapping(value = "/editInvoice", method = RequestMethod.GET, params = {"id"})
    public String startEditInvoice(Model model, @RequestParam Long id) {
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        model.addAttribute("persons", personService.getAllPersons());
        model.addAttribute("dateToday", LocalDate.now());
        model.addAttribute("action", "editInvoice");
        model.addAttribute("title", "Edit invoice");

        return "invoiceDetail";
    }

    /* Start deleting person - contains form with pre-filled details, user has to approve deletion */
    @RequestMapping(value = "/deleteInvoice", method = RequestMethod.GET, params = {"id"})
    public String startDeleteInvoice(Model model, @RequestParam Long id) {
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        model.addAttribute("action", "deleteInvoice");
        model.addAttribute("title", "Delete invoice");

        return "invoiceDetail";
    }

    /* Show error */
    @RequestMapping(value = "/invoiceOperationFailed", method = RequestMethod.GET)
    public String invoiceOperationFailed() {
        return "invoiceDetail";
    }

    /* Processing of creating new Invoice entry */
    @RequestMapping(value = "/createInvoice", method = RequestMethod.POST)
    public String addInvoice(Model model, @ModelAttribute Invoice invoice,
                             @RequestParam(value="itemName[]") String[] itemName,
                             @RequestParam(value="itemCount[]") int[] itemCount,
                             @RequestParam(value="itemPrice[]") double[] itemPricePerUnit,
                             @RequestParam(value="itemDesc[]") String[] itemDescription,
                             @RequestParam Long secondPerson,
                             @RequestParam String type,
                             @RequestParam("dueTo") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate dueDate,
                             @RequestParam("issued") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate issued,
                             RedirectAttributes redirectAttributes, SessionStatus sessionStatus)
    {
        String message, viewName;

        try {
            setInvoiceItemsAndTotal(invoice, itemName, itemDescription, itemCount, itemPricePerUnit);
            setInvoiceAttrs(invoice, issued, dueDate, secondPerson, type);


            invoiceService.createInvoice(invoice);
            message = "Entry of invoice with id: " + invoice.getId() + " created!";
            viewName = "redirect:/accounting/invoices";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when creating entry in database, please try again!";
            viewName = "redirect:/accounting/invoiceOperationFailed";
            model.addAttribute("invoice", invoice);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Create invoice");
            redirectAttributes.addFlashAttribute("action", "createInvoice");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return viewName;
    }

    /* Processing of editing Invoice entry */
    @RequestMapping(value = "/editInvoice", method = RequestMethod.POST)
    public String editInvoice(Model model, @ModelAttribute Invoice invoice,
                              @RequestParam(value="itemName[]") String[] itemName,
                              @RequestParam(value="itemCount[]") int[] itemCount,
                              @RequestParam(value="itemPrice[]") double[] itemPricePerUnit,
                              @RequestParam(value="itemDesc[]") String[] itemDescription,
                              @RequestParam Long secondPerson,
                              @RequestParam String type,
                              @RequestParam("dueTo") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate dueDate,
                              @RequestParam("issued") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate issued,
                              RedirectAttributes redirectAttributes, SessionStatus sessionStatus)
    {
        String message, viewName;

        try {
            setInvoiceItemsAndTotal(invoice, itemName, itemDescription, itemCount, itemPricePerUnit);
            setInvoiceAttrs(invoice, issued, dueDate, secondPerson, type);

            invoiceService.editInvoice(invoice);
            message = "Entry of invoice with id: " + invoice.getId() + " edited!";
            viewName = "redirect:/accounting/invoices";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when storing entry in database, please try again!";
            viewName = "redirect:/accounting/invoiceOperationFailed";
            model.addAttribute("invoice", invoice);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Edit invoice");
            redirectAttributes.addFlashAttribute("action", "editInvoice");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return viewName;
    }

    /* Processing of deleting Invoice entry */
    @RequestMapping(value = "/deleteInvoice", method = RequestMethod.POST)
    public String deleteInvoice(Model model, @ModelAttribute Invoice invoice, RedirectAttributes redirectAttributes,
                               SessionStatus sessionStatus) {
        String message, viewName;

        try {
            invoiceService.deleteInvoice(invoice.getId());
            message = "Entry of invoice with id: " + invoice.getId() + " deleted!";
            viewName = "redirect:/accounting/invoices";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when deleting entry in database, please try again!";
            viewName = "redirect:/accounting/invoiceOperationFailed";
            model.addAttribute("invoice", invoice);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Delete invoice");
            redirectAttributes.addFlashAttribute("action", "deleteInvoice");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return viewName;
    }

    private void setInvoiceItemsAndTotal(Invoice invoice, String[] itemsName, String[] itemsDescription,
                                 int[] itemsCount, double[] itemsPrice) {
        double total = 0;
        for (int i = 0; i < itemsName.length; i++) {
            Item item = new Item();
            double totalPrice = itemsPrice[i] * itemsCount[i];
            total += totalPrice;

            item.setName(itemsName[i]);
            item.setCount(itemsCount[i]);
            item.setDescription(itemsDescription[i]);
            item.setPrice(itemsPrice[i]);
            item.setTotalPrice(totalPrice);

            invoice.addItem(item);
        }
        invoice.setPrice(total);
    }

    private void setInvoiceAttrs(Invoice invoice, LocalDate issuedDate, LocalDate dueDate, Long personId,
                                 String type) {
        invoice.setIssueDate(issuedDate);
        invoice.setDueDate(dueDate);
        invoice.setInvoiceType((type.equalsIgnoreCase("expense")? InvoiceType.EXPENSE : InvoiceType.INCOME));
        if (invoice.getInvoiceType().equals(InvoiceType.INCOME)) {
            invoice.setPayer(personService.getPersonById(personId));
            invoice.setRecipient(ownerService.getOwner());
        } else {
            invoice.setRecipient(personService.getPersonById(personId));
            invoice.setPayer(ownerService.getOwner());
        }
    }
}
