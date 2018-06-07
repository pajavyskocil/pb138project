package cz.muni.fi;

import cz.fi.muni.CIA.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    //private AddressBookService addressBookService;

    /* List entries for Person from database */
    @RequestMapping(value = "/showAddressBook", method = RequestMethod.GET)
    public String showAddressBook(Model model) {
        //TODO implement - list all people from DB
        return "addressBook";
    }

    /* Start adding new Person - contains form to define person entry */
    @RequestMapping(value = "/createPerson", method = RequestMethod.GET)
    public String startAddPerson(Model model) {
        model.addAttribute("person", new Person());
        return "personDetail";
    }

    /* Start editing Person - contains form with pre-filled details that can be edited */
    @RequestMapping(value = "/editPerson", method = RequestMethod.GET)
    public String startEditPerson(Model model) {
        //todo implement - get person from db by ID
        return "personDetail";
    }

    /* Start deleting person - contains form with pre-filled details, user has to approve deletion */
    @RequestMapping(value = "/deletePerson", method = RequestMethod.GET)
    public String startDeletePerson(Model model) {
        //todo implement - get person from db by ID
        return "personDetail";
    }

    /* Show error */
    @RequestMapping(value = "/personOperationFailed", method = RequestMethod.GET)
    public String personOperationFailed() {
        return "personDetail";
    }

    /* Processing of creating new Person entry */
    @RequestMapping(value = "/createPerson", method = RequestMethod.POST)
    public String addPerson(@ModelAttribute Person person, RedirectAttributes redirectAttributes,
                            SessionStatus sessionStatus) {
        //TODO implement - create person
        String message, viewName;
        try {
            //addressBookService.create(person);
            message = "Person created. Person id :" + person.getId();
            viewName = "redirect:/showAddressBook";
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Person create failed";
            viewName = "redirect:/personOperationFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }

    /* Processing of editing Person entry */
    @RequestMapping(value = "/editPerson", method = RequestMethod.POST)
    public String editPerson(@ModelAttribute Person person, RedirectAttributes redirectAttributes,
                            SessionStatus sessionStatus) {
        //TODO implement - edit person
        String message, viewName;
        try {
            //addressBookService.edit(person);
            message = "Person edited. Person id :" + person.getId();
            viewName = "redirect:/showAddressBook";
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Person edit failed";
            viewName = "redirect:/personOperationFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }

    /* Processing of deleting Person entry */
    @RequestMapping(value = "/deletePerson", method = RequestMethod.POST)
    public String deletePerson(@ModelAttribute Person person, RedirectAttributes redirectAttributes,
                            SessionStatus sessionStatus) {
        //TODO implement - delete person
        String message, viewName;
        try {
            //addressBookService.delete(person);
            message = "Person deleted. Person id :" + person.getId();
            viewName = "redirect:/showAddressBook";
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Person delete failed";
            viewName = "redirect:/personOperationFailed";
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }
}
