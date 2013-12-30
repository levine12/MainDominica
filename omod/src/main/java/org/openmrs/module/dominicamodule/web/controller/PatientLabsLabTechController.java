package org.openmrs.module.dominicamodule.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.OrderType;

import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.ConceptEditor;
import org.openmrs.propertyeditor.DrugEditor;
import org.openmrs.propertyeditor.EncounterEditor;
import org.openmrs.propertyeditor.OrderTypeEditor;
import org.openmrs.propertyeditor.PatientEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "module/dominicamodule/patientLabsLabTech.form")
public class PatientLabsLabTechController {

    static Vector<Vector<Concept>> setConcepts = new Vector<Vector<Concept>>();
    static Vector<Concept> labSets = new Vector<Concept>();

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OrderType.class, new OrderTypeEditor());
        binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor("t",
                "f", true));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
                Integer.class, true));
        binder.registerCustomEditor(Concept.class, new ConceptEditor());
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(Context.getDateFormat(), true));
        binder.registerCustomEditor(User.class, new UserEditor());
        binder.registerCustomEditor(Patient.class, new PatientEditor());
        binder.registerCustomEditor(Encounter.class, new EncounterEditor());
        binder.registerCustomEditor(Drug.class, new DrugEditor());
    }

    @RequestMapping(method = RequestMethod.GET)
    protected ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Here in PatientLabsLabTechController  get method.");
        ModelMap model = setUpLabUI(request);

        return new ModelAndView("/module/dominicamodule/patientLabsLabTech", model);

    }

    private ModelMap setUpLabUI(HttpServletRequest request) throws APIException, NumberFormatException {
        ModelMap model = new ModelMap();
        Vector<MyLabOrderForJSP> currentOrders = new Vector<MyLabOrderForJSP>();
        for (MyLabOrder order : MyLabOrder.getLabOrders()) {
            if (!(order.isIsDiscontinued()) && !(order.isIsVoided()) && !(order.isIsFilled()) ) {
                System.out.println("*******order.getOrderDate() " + 
                        order.getOrderDate() + " order.getPatientId() " + order.getPatientId());
                currentOrders.add(new MyLabOrderForJSP(order.getOrderingPhysicianId(), order.getOrderDate(),
                        order.getOrderId(), order.getOrderInstructions(), order.getLabSetId(), order.getLabTestsId(), 
                        order.getPatientId()));
            }
        }
        model.put("current_orders", currentOrders);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response) {
        int orderId = Integer.valueOf(request.getParameter("orderId"));
        System.out.println("Filled Order: " + orderId);
        MyLabOrder order = MyLabOrder.getOrderForId(orderId);
        order.setIsFilled(true);
        
        ModelMap model = setUpLabUI(request);
        return new ModelAndView(
                "/module/dominicamodule/patientLabsLabTech", model);

    }

}
