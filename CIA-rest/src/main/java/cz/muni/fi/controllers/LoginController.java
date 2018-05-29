package cz.muni.fi.controllers;

import cz.fi.muni.CIA.OwnerService;
import cz.fi.muni.CIA.entities.Address;
import cz.fi.muni.CIA.entities.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.xml.bind.DatatypeConverter;
import java.nio.file.Files;

/**
 * Controller for entry point into Web Application.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class LoginController {

    @Autowired
    private OwnerService ownerService;

    /* User has to choose which manager he/she wants to access */
    @RequestMapping(value = "/")
    public String showHomeScreen(Model model) {
        model.addAttribute("hasOwner", ownerService.getOwner() != null);
        model.addAttribute("title", "Accounting");

        return "home";
    }

    /* Start adding new Owner - contains form to define owner entry */
    @RequestMapping(value = "/createOwner", method = RequestMethod.GET)
    public String startAddOwner(Model model, RedirectAttributes redirectAttributes) {
        String message, viewName;
        if (ownerService.getOwner() != null) {
            message = "Only one owner entry can be defined";
            viewName = "redirect:/accounting/";
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            viewName = "ownerDetail";
            model.addAttribute("owner", new Owner());
            model.addAttribute("action", "createOwner");
            model.addAttribute("title", "Create owner");
        }
        return viewName;
    }

    /* Start editing Owner - contains form with pre-filled details that can be edited */
    @RequestMapping(value = "/editOwner", method = RequestMethod.GET)
    public String startEditOwner(Model model, RedirectAttributes redirectAttributes) {
        String message, viewName;
        if (ownerService.getOwner() == null) {
            message = "No owner entry has been defined";
            viewName = "redirect:/accounting/";
            redirectAttributes.addFlashAttribute("message", message);
        } else {
            viewName = "ownerDetail";
            model.addAttribute("action", "editOwner");
            model.addAttribute("owner", ownerService.getOwner());
            model.addAttribute("title", "Edit owner");
        }
        return viewName;
    }

    /* Show error */
    @RequestMapping(value = "/ownerOperationFailed", method = RequestMethod.GET)
    public String ownerOperationFailed() {
        return "ownerDetail";
    }

    /* Processing of creating new Owner entry */
    @RequestMapping(value = "/createOwner", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String addOwner(Model model, @ModelAttribute Owner owner, @ModelAttribute Address address,
                           @RequestParam("logo") MultipartFile file, RedirectAttributes redirectAttributes,
                           SessionStatus sessionStatus) {
        String message, viewName;
        try {
            if (file != null) {
                String base64Logo = DatatypeConverter.printBase64Binary(file.getBytes());
                String logoPre = "data:" + file.getContentType() + "; base64,";
                owner.setLogoBASE64(logoPre + base64Logo);
            }
            owner.setAddress(address);
            ownerService.createOwner(owner);

            message = "Entry of owner with name: " + owner.getName() + " created!";
            viewName = "redirect:/accounting/";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when creating entry in database, please try again!";
            viewName = "redirect:/accounting/ownerOperationFailed";
            model.addAttribute("owner", owner);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Create owner");
            redirectAttributes.addFlashAttribute("action", "createOwner");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }

    /* Processing of editing Owner entry */
    @RequestMapping(value = "/editOwner", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String editOwner(Model model, @ModelAttribute Owner owner, @ModelAttribute Address address,
                            @RequestParam("logo") MultipartFile file, RedirectAttributes redirectAttributes,
                            SessionStatus sessionStatus) {
        String message, viewName;
        try {
            if (file != null) {
                String base64Logo = DatatypeConverter.printBase64Binary(file.getBytes());
                String logoPre = "data:" + file.getContentType() + "; base64,";
                owner.setLogoBASE64(logoPre + base64Logo);
            }
            owner.setAddress(address);
            ownerService.updateOwner(owner);

            message = "Entry of owner with name: " + owner.getName() + " edited!";
            viewName = "redirect:/accounting/";
            redirectAttributes.addFlashAttribute("alertType", "alert-success");
            sessionStatus.setComplete();
        } catch (Exception ex) {
            message = "Error has occurred when editing entry in database, please try again!";
            viewName = "redirect:/accounting/ownerOperationFailed";
            model.addAttribute("owner", owner);
            redirectAttributes.addFlashAttribute("alertType", "alert-danger");
            redirectAttributes.addFlashAttribute("title", "Edit owner");
            redirectAttributes.addFlashAttribute("action", "editOwner");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return viewName;
    }
}