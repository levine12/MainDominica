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
import org.openmrs.module.dominicamodule.LabOrder;
import org.openmrs.module.dominicamodule.LabOrderService;
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
public class PatientLabsPortletController {

    static Vector<Vector<Concept>> setConcepts = new Vector<Vector<Concept>>();
    static Vector<Concept> labSets = new Vector<Concept>();

    static LabOrderService service() {
        return Context.getService(LabOrderService.class);
    }

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
        System.out.println("Here in PatientLabsPortletController  get method.");
        ModelMap model = setUpLabUI(request);
        LabOrder order = new LabOrder();
        System.out.println("Saving lab order");
        order.setLabSetId(1010);
        service().saveLabOrder(order);
        System.out.println("Saved lab order");
        return new ModelAndView("/module/dominicamodule/portlets/patientLabs", model);

    }

    private ModelMap setUpLabUI(HttpServletRequest request) throws APIException, NumberFormatException {
        int patientId = Integer.valueOf(request.getParameter("patientId"));
        Patient p = Context.getPatientService().getPatient(patientId);
        String concepts = Context.getAdministrationService().getGlobalProperty("dominicamodule.labsetsproperty");
        StringTokenizer conTok = new StringTokenizer(concepts, " ,");

        labSets = new Vector<Concept>();
        int nextCon;
        String nextTok;
        Concept con;
        while (conTok.hasMoreElements()) {
            nextTok = conTok.nextToken();
            nextCon = Integer.parseInt(nextTok);
            con = Context.getConceptService().getConcept(nextCon);
            System.out.println("*********PatientLabsPortletController - adding labset: " + con.getName().getName());
            labSets.add(con);
            setConcepts.add(getConceptsForSet(con));
        }

        //labSets.add(cbc);
        //labSets.add(lipid);
        //Vector<Vector<Concept>> setConcepts = new Vector<Vector<Concept>>();
        //setConcepts.add(getConceptsForSet(cbc));
        //setConcepts.add(getConceptsForSet(lipid));
        ModelMap model = new ModelMap();
        Vector<MyLabOrderForJSP> priorOrders = new Vector<MyLabOrderForJSP>();
        for (MyLabOrder order : MyLabOrder.getOrdersForPatientId(patientId)) {
            if (!(order.isIsDiscontinued()) && !(order.isIsVoided())) {
                priorOrders.add(new MyLabOrderForJSP(order.getOrderingPhysicianId(), order.getOrderDate(),
                        order.getOrderId(), order.getOrderInstructions(), order.getLabSetId(), order.getLabTestsId(), order.isIsDiscontinued(),
                        order.isIsVoided(), order.isIsFilled()));
            }
        }
        model.put("prior_orders", priorOrders);
        model.put("labsets", labSets);
        return model;
    }

    private static Vector<Concept> getConceptsForSet(Concept labSet) {
        List<ConceptSet> labSetMemberConceptSetItems = new Vector<ConceptSet>();
        for (ConceptSet labSetMemberConceptSetItem : labSet.getConceptSets()) {
            labSetMemberConceptSetItems.add(labSetMemberConceptSetItem);
        }

        Vector<Concept> concepts = new Vector<Concept>();
        for (ConceptSet labSetConceptSetItem : labSetMemberConceptSetItems) {
            concepts.add(labSetConceptSetItem.getConcept());
            //System.out.println("Concept: " + conSet.getConcept().getName().getName());
            //System.out.println("Concept: " + conSet.getConcept().getShortestName(Locale.ENGLISH, Boolean.TRUE));
        }
        return concepts;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView post(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("voidOrder") != null) {
            int orderId = Integer.valueOf(request.getParameter("sendOrderId"));
            System.out.println("******************************PatientLabsPortletController...VOID"
                    + "  Order id: " + orderId);
            MyLabOrder order = MyLabOrder.getOrderForId(orderId);
            order.setVoidDate(new Date());
            order.setIsVoided(true);
            order.setVoidReason(request.getParameter("voidreason"));
        } else if (request.getParameter("discontinueOrder") != null) {
            int orderId = Integer.valueOf(request.getParameter("sendOrderId"));
            System.out.println("******************************PatientLabsPortletController...DISCONTINUE"
                    + "Order id: " + orderId);
            MyLabOrder order = MyLabOrder.getOrderForId(orderId);
            order.setDiscontinueDate(new Date());
            order.setIsDiscontinued(true);
            order.setDiscontinuedReason(request.getParameter("discontinuereason"));
        } else {
            Integer patientId = Integer.valueOf(request.getParameter("patientId"));
            Patient p = Context.getPatientService().getPatient(patientId);
            System.out.println("*************PatientLabsPortletController...POST: " + request.getParameter("labIds1"));

            int labSetId = 0;
            String labTestStrings;

            MyLabOrder labOrder;
            if (request.getParameter("labIds1") != null) {
                labOrder = MyLabOrder.getOrderForId(Integer.valueOf(request.getParameter("sendOrderId")));
                labOrder.setOrderInstructions(request.getParameter("instructionsedit"));
                labTestStrings = request.getParameter("labIds1");
            } else {
                labOrder = new MyLabOrder();
                labOrder.setOrderInstructions(request.getParameter("instructions"));
                labTestStrings = request.getParameter("labIds");
            }
            if (labTestStrings != null) {
                System.out.println("LabIds: " + labTestStrings);
                ArrayList<Integer> labTestsId = new ArrayList<Integer>();
                System.out.println("*************PatientLabsPortletController...selected item: " + labTestStrings);
                StringTokenizer ids = new StringTokenizer(labTestStrings, " ,");
                if (ids.hasMoreElements()) {
                    labSetId = Integer.parseInt(ids.nextToken());
                }
                while (ids.hasMoreElements()) {
                    int testId = Integer.parseInt(ids.nextToken());
                    labTestsId.add(testId);
                    System.out.println("Concept: " + testId);
                }
                labOrder.setLabSetId(labSetId);
                labOrder.setLabTestsId(labTestsId);
                labOrder.setOrderDate(new Date());
                User doctor = Context.getUserContext().getAuthenticatedUser();
                labOrder.setOrderingPhysicianId(doctor.getId());
                labOrder.setPatientId(patientId);
                labOrder.printOrder();
                request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Lab Order.saved");
            }
        }
        /*       
         Integer patientId = Integer.valueOf(request.getParameter("patientId"));
         Patient p = Context.getPatientService().getPatient(patientId);
         System.out.println("Here in PatientLabsPortletController post method.");
         String filledIds = request.getParameter("orderIds");
         StringTokenizer ids = new StringTokenizer(filledIds, " ,");
         int orderId;

         while (ids.hasMoreTokens()) {
         orderId = Integer.valueOf(ids.nextToken());
         System.out.println("Filled order: " + orderId + " Name: " + Context.getConceptService().getConcept(orderId).getName().getName());

         }
         */
        ModelMap model = setUpLabUI(request);

        return new ModelAndView(
                "/module/dominicamodule/portlets/patientLabs", model);

    }

    static public Vector<Concept> getConceptSet(int labSetConceptIdSelected) {
        System.out.println("PatientLabsPortletController...found labset with id: " + labSetConceptIdSelected);
        Concept labset = Context.getConceptService().getConcept(labSetConceptIdSelected);
        return getConceptsForSet(labset);
    }

}
