package org.openmrs.module.dominicamodule.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.activelist.ActiveListItem;
import org.openmrs.activelist.Allergy;
import org.openmrs.activelist.AllergySeverity;
import org.openmrs.activelist.AllergyType;
import org.openmrs.activelist.Problem;
import org.openmrs.activelist.ProblemModifier;
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
public class PatientAllergiesPortletController {

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
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Here in PatientAllergiesPortletController  get method.");
 //       ModelMap model = setUpCurrentAllergies(request);
        return populateModel(request);

    }

    private ModelAndView populateModel(HttpServletRequest request) {
        ModelMap model = new ModelMap();
        int patientId = Integer.valueOf(request.getParameter("patientId"));
        model.put("patientId", patientId);
        model.put("type", "allergy");
        Patient patient = Context.getPatientService().getPatient((Integer) model.get("patientId"));
        model.put("today", Context.getDateFormat().format(new Date()));
        //model.put("today",new Date());

        String type = (String) model.get("type");
        //List<Allergy> allergies = Context.getPatientService().getAllergies(patient);
        List<ActiveListItem> allAllergyItems = Context.getActiveListService().getActiveListItems((Person) patient, Allergy.ACTIVE_LIST_TYPE);
        List<Allergy> allergies = new Vector<Allergy>();
        for (ActiveListItem item : allAllergyItems) {
            allergies.add((Allergy) item);
        }
        List<List<Allergy>> ls = separate(allergies);
        model.put("allergies", ls.get(0));
        model.put("removedAllergies", ls.get(1));
        model.put("allergyTypes", AllergyType.values());
        model.put("allergySeverities", AllergySeverity.values());
        return new ModelAndView("/module/dominicamodule/portlets/patientAllergies", model);

    }

    private List<List<Allergy>> separate(List<Allergy> ls) {
        List<Allergy> active = new ArrayList<Allergy>();
        List<Allergy> removed = new ArrayList<Allergy>();

        for (Allergy item : ls) {
            if (item.getEndDate() == null) {
                System.out.println("PatientAllergiesPortletController active allergy: " + item.getAllergen().getName().getName());
                active.add(item);
            } else {
                System.out.println("PatientAllergiesPortletController ended allergy: " + item.getAllergen().getName().getName());
                removed.add(item);
            }
        }

        List<List<Allergy>> items = new ArrayList<List<Allergy>>(2);
        items.add(active);
        items.add(removed);
        return items;
    }

    private ModelMap setUpCurrentAllergies(HttpServletRequest request) throws APIException, NumberFormatException {
        int patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        Person person = p;
        List<ActiveListItem> allAllergies = Context.getActiveListService().getActiveListItems(person, Allergy.ACTIVE_LIST_TYPE);
        System.out.println("********************PatientAllergiesPortletController  Allergies for patient: " + p.getFamilyName());
        for (ActiveListItem allergyItem : allAllergies) {
            Allergy allergy = (Allergy) allergyItem;
            System.out.println("Allergy: " + allergy.getAllergen().getName().getName());
        }
        ModelMap model = new ModelMap();
        Vector<MyAllergyForJSP> currentAllergies = new Vector<MyAllergyForJSP>();
        for (ActiveListItem allergyItem : allAllergies) {
            currentAllergies.add(new MyAllergyForJSP(allergyItem));
        }
        model.put("current_allergies", currentAllergies);
        model.put("patientId", p);
        return model;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response) {
        //ModelMap model = setUpCurrentAllergies(request);
        //return new ModelAndView("/module/dominicamodule/portlets/patientAllergies", model);
        return populateModel(request);
    }

}
