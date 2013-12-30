/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.dominicamodule.web.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.dominicamodule.Note;
import org.openmrs.module.dominicamodule.LabOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author levine
 */
@Controller
public class PatientOverviewPortletController {

    private final String SUCCESS_FORM_VIEW = "/module/dominicamodule/portlets/patientOverview";

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Here in PatientOverviewPortletController  get method.");
        ModelMap model = new ModelMap();
        model.put("today", Context.getDateFormat().format(new Date()));
        return new ModelAndView("/module/dominicamodule/portlets/patientOverview", model);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        model.put("today", Context.getDateFormat().format(new Date()));
        return new ModelAndView("/module/dominicamodule/portlets/patientOverview", model);
    }
}
