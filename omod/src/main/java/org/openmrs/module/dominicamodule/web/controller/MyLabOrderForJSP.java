package org.openmrs.module.dominicamodule.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;

/**
 *
 * @author levine
 */
public class MyLabOrderForJSP {

    private Date orderDate;
    private int orderId;
    private String orderInstructions;

    private Person orderingPhysician;
    private Person patient;

    private Concept labSet;
    private ArrayList<Concept> labTests = new ArrayList<Concept>();

    private boolean isDiscontinued;
    private boolean isVoided;
    private boolean isFilled;

    public MyLabOrderForJSP(int orderingPhysicianId, Date orderDate, int orderId,  String orderInstructions, 
            int labSetId, ArrayList<Integer> labTestsId, boolean isDiscontinued, boolean isVoided, boolean isFilled) {
        this.orderingPhysician = Context.getPersonService().getPerson(orderingPhysicianId);
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.orderInstructions = orderInstructions;
        this.isDiscontinued = isDiscontinued;
        this.isFilled = isFilled;
        this.isVoided = isVoided; 
        labSet = Context.getConceptService().getConcept(labSetId);
        for (Integer labTest : labTestsId) {
            labTests.add(Context.getConceptService().getConcept(labTest));
        }
    }

    MyLabOrderForJSP(int orderingPhysicianId, Date orderDate, int orderId, String orderInstructions, int labSetId, 
            ArrayList<Integer> labTestsId, int patientId) {
        this.orderingPhysician = Context.getPersonService().getPerson(orderingPhysicianId);
        this.patient = Context.getPersonService().getPerson(patientId);
        this.orderDate = orderDate;
        this.orderId = orderId;
        labSet = Context.getConceptService().getConcept(labSetId);
        for (Integer labTest : labTestsId) {
            labTests.add(Context.getConceptService().getConcept(labTest));
        }
        this.orderInstructions = orderInstructions;
    }

    public Person getOrderingPhysician() {
        return orderingPhysician;
    }

    public Person getPatient() {
        return patient;
    }

    public String getOrderInstructions() {
        return orderInstructions;
    }

    public void setOrderInstructions(String orderInstructions) {
        this.orderInstructions = orderInstructions;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public Concept getLabSet() {
        return labSet;
    }

    public ArrayList<Concept> getLabTests() {
        return labTests;
    }

    public boolean isIsDiscontinued() {
        return isDiscontinued;
    }

    public boolean isIsVoided() {
        return isVoided;
    }

    public boolean isIsFilled() {
        return isFilled;
    }

}
