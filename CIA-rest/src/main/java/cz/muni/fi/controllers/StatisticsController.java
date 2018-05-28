package cz.muni.fi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for pages displaying statistics.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Controller
public class StatisticsController {

    @Autowired
    //private StatsService statsService;

    /* Entry point into manager */
    @RequestMapping(value = "/listStatistics", method = RequestMethod.GET)
    public String listStatistics() {
        //TODO implement - get statistics from service
        return "listStatistics";
    }

    /* Let user choose type of statistics to be shown. User also defines time period of stats. */
    @RequestMapping(value = "/listStatistics", method = RequestMethod.POST)
    public String chooseTypeOfList() {
        return "listStatistics";
    }

    /* Request to generate PDF from the displayed statistics */
    @RequestMapping(value = "/generatePdf", method = RequestMethod.POST)
    public String generatePdf(RedirectAttributes redirectAttributes, SessionStatus sessionStatus) {
            //TODO implement - generate pdf

            //addressBookService.edit(person);
            return  "redirect:/showAddressBook";
    }
}
