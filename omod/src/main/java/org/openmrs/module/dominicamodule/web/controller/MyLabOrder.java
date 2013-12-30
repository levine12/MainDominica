package org.openmrs.module.dominicamodule.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import org.openmrs.Concept;
import org.openmrs.Patient;

/**
 *
 * @author levine
 *
 * This class simulates information stored in the DB
 */
public class MyLabOrder {

    static private Vector<MyLabOrder> labOrders = new Vector<MyLabOrder>();

    static public Vector<MyLabOrder> getLabOrders() {
        return labOrders;
    }

    private Date orderDate;
    private String orderInstructions;
    private int orderId;

    private boolean isDiscontinued = false;
    private String discontinuedReason;
    private Date discontinueDate;

    private boolean isVoided = false;
    private String voidReason;
    private Date voidDate;

    private boolean isFilled = false;

    private int labSetId;
    private ArrayList<Integer> labTestsId;

    private int orderingPhysicianId;
    private int patientId;

    private static int orderCount = 0;

    public MyLabOrder() {
        labOrders.add(this);
        orderId = orderCount++;
    }

    public void printOrder() {
        System.out.println("*****************ORDER FOR: " + patientId
                + "  " + orderDate + "  Physician: " + orderingPhysicianId
                + "  Lab Set: " + labSetId + " Instructions: " + orderInstructions);
        for (Integer test : labTestsId) {
            System.out.println(test);
        }

    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderInstructions() {
        return orderInstructions;
    }

    public void setOrderInstructions(String orderInstructions) {
        this.orderInstructions = orderInstructions;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isIsDiscontinued() {
        return isDiscontinued;
    }

    public void setIsDiscontinued(boolean isDiscontinued) {
        this.isDiscontinued = isDiscontinued;
    }

    public String getDiscontinuedReason() {
        return discontinuedReason;
    }

    public void setDiscontinuedReason(String discontinuedReason) {
        this.discontinuedReason = discontinuedReason;
    }

    public boolean isIsVoided() {
        return isVoided;
    }

    public void setIsVoided(boolean isVoided) {
        this.isVoided = isVoided;
    }

    public String getVoidReason() {
        return voidReason;
    }

    public void setVoidReason(String voidReason) {
        this.voidReason = voidReason;
    }

    public boolean isIsFilled() {
        return isFilled;
    }

    public void setIsFilled(boolean isFilled) {
        this.isFilled = isFilled;
    }

    public int getLabSetId() {
        return labSetId;
    }

    public void setLabSetId(int labSetId) {
        this.labSetId = labSetId;
    }

    public ArrayList<Integer> getLabTestsId() {
        return labTestsId;
    }

    public void setLabTestsId(ArrayList<Integer> labTestsId) {
        this.labTestsId = labTestsId;
    }

    public int getOrderingPhysicianId() {
        return orderingPhysicianId;
    }

    public void setOrderingPhysicianId(int orderingPhysicianId) {
        this.orderingPhysicianId = orderingPhysicianId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Date getDiscontinueDate() {
        return discontinueDate;
    }

    public void setDiscontinueDate(Date discontinueDate) {
        this.discontinueDate = discontinueDate;
    }

    public Date getVoidDate() {
        return voidDate;
    }

    public void setVoidDate(Date voidDate) {
        this.voidDate = voidDate;
    }

    public static MyLabOrder getOrderForId(int orderId) {
        for (MyLabOrder order : labOrders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }
    
       public static Vector<MyLabOrder> getOrdersForPatientId(int patientId) {
           Vector<MyLabOrder> orders = new Vector<MyLabOrder>();
        for (MyLabOrder order : labOrders) {
            if (order.getPatientId() == patientId) {
                orders.add(order);
            }
        }
        return orders;
    }
}
