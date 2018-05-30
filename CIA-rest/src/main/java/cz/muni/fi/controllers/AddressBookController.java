package cz.muni.fi.controllers;

import cz.fi.muni.CIA.PersonService;
import cz.fi.muni.CIA.entities.Address;
import cz.fi.muni.CIA.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for operations connected to Address book.
 * Contains mappings for CRUD operations with Person entity.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class AddressBookController {

    @Autowired
    private PersonService personService;

    /* List entries for Person from database */
    @RequestMapping(value = "/addressBook", method = RequestMethod.GET)
    public String showAddressBook(Model model) {
        model.addAttribute("persons", personService.getAllPersons());
        model.addAttribute("title", "Person list");

        return "addressBook";
    }

    /* Start adding new Person - contains form to define person entry */
    @RequestMapping(value = "/createPerson", method = RequestMethod.GET)
    public String startAddPerson(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("action", "createPerson");
        model.addAttribute("title", "Create person");

        return "personDetail";
    }

    /* Start editing Person - contains form with pre-filled details that can be edited */
    @RequestMapping(value = "/editPerson", method = RequestMethod.GET, params = {"id"})
    public String startEditPerson(Model model, @RequestParam Long id) {
        model.addAttribute("action", "editPerson");
        model.addAttribute("person", personService.getPersonById(id));
        model.addAttribute("title", "Edit person");

        return "personDetail";
    }

    /* Start deleting person - contains form with pre-filled details, user has to approve deletion */
    @RequestMapping(value = "/deletePerson", method = RequestMethod.GET, params = {"id"})
    public String startDeletePerson(Model model, @RequestParam Long id) {
        model.addAttribute("action", "deletePerson");
        model.addAttribute("person", personService.getPersonById(id));
        model.addAttribute("title", "Delete person");

        return "personDetail";
    }

    /* Show error */
    @RequestMapping(value = "/personOperationFailed", method = RequestMethod.GET)
    public String personOperationFailed() {
        return "personDetail";
    }

    /* Processing of creating new Person entry */
    @RequestMapping(value = "/createPerson", method = RequestMethod.POST)
    public String addPerson(Model model, @ModelAttribute Person person, @ModelAttribute Address address,
                            RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        String message, viewName;
        try {
            person.setAddress(address);
            personService.createPerson(person);
            message = "Entry of person with name: " + person.getName() + " created!";
            viewName = "redirect:/accounting/addressBook";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when creating entry in database, please try again!";
            viewName = "redirect:/accounting/personOperationFailed";
            model.addAttribute("person", person);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Create person");
            redirectAttributes.addFlashAttribute("action", "createPerson");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }
    
    /* Processing of editing Person entry */
    @RequestMapping(value = "/editPerson", method = RequestMethod.POST)
    public String editPerson(Model model, @ModelAttribute Person person, @ModelAttribute Address address,
                             RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
        String message, viewName;
        try {
            person.setAddress(address);
            personService.editPerson(person);
            message = "Entry of person with name: " + person.getName() + " edited!";
            viewName = "redirect:/accounting/addressBook";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when editing entry in database, please try again!";
            viewName = "redirect:/accounting/personOperationFailed";
            model.addAttribute("person", person);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Edit person");
            redirectAttributes.addFlashAttribute("action", "editPerson");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }

    /* Processing of deleting Person entry */
    @RequestMapping(value = "/deletePerson", method = RequestMethod.POST)
    public String deletePerson(Model model, @ModelAttribute Person person, RedirectAttributes redirectAttributes,
                            SessionStatus sessionStatus) {
        String message, viewName;
        try {
            personService.deletePerson(person.getId());
            message = "Entry of person with name: " + person.getName() + " deleted!";
            viewName = "redirect:/accounting/addressBook";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when deleting entry in database, please try again!";
            viewName = "redirect:/accounting/personOperationFailed";
            model.addAttribute("person", person);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Delete person");
            redirectAttributes.addFlashAttribute("action", "deletePerson");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }
}
