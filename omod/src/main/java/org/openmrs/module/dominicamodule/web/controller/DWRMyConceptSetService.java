package org.openmrs.module.dominicamodule.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.list;
import java.util.List;
import java.util.Vector;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.api.context.Context;

/**
 *
 * @author levine
 */
public class DWRMyConceptSetService {

    public Collection<ConceptIdAndName> getLabSetConceptsForConceptId(int labSetConceptIdSelected) {

        System.out.println(" ******getLabSetConceptsForConceptId: " + labSetConceptIdSelected);
        List<Concept> concepts = PatientLabsPortletController.getConceptSet(labSetConceptIdSelected);
        for (Concept concept : concepts) {
            System.out.println(concept.getName().getName());
        }
        ArrayList<ConceptIdAndName> cons = new ArrayList<ConceptIdAndName>();
        for (Concept concept : concepts) {
            cons.add(new ConceptIdAndName(concept.getConceptId(), concept.getName().getName()));
            System.out.println(concept.getName().getName());
        }
        //cons.add(new ConceptIdAndName(1019, "xxxMy test****"));
        /*
         for (Concept concept : concepts) {
         cons.add(new ConceptIdAndName(concept.getConceptId(),concept.getName().getName()));
         System.out.println(concept.getName().getName());
         }
         */

        return cons;
    }

    public Collection<EditLabOrderItem> getCheckBoxConceptsForOrder(int orderId) {

        System.out.println("getCheckBoxConceptsForOrder: " + orderId);
        Vector<MyLabOrder> orders = MyLabOrder.getLabOrders();
        for (MyLabOrder order : orders) {
            if (order.getOrderId() == orderId) {
                /*
                 if (orderId == 0) {
                 order.setIsFilled(true);
                 }
                 */
                if (order.isIsFilled()) {
                    return null;
                }
                int labSetId = order.getLabSetId();
                Vector<Concept> allConceptsInSet = getConceptsForLabSet(labSetId);
                return getConceptsForJSP(orderId, labSetId, allConceptsInSet, order);
            }
        }
        return null;
    }

    private Vector<Concept> getConceptsForLabSet(int labSetId) {
        Concept labSetConcept = Context.getConceptService().getConcept(labSetId);
        List<ConceptSet> labSetMemberConceptSetItems = new Vector<ConceptSet>();
        for (ConceptSet labSetMemberConceptSetItem : labSetConcept.getConceptSets()) {
            labSetMemberConceptSetItems.add(labSetMemberConceptSetItem);
        }

        Vector<Concept> concepts = new Vector<Concept>();
        for (ConceptSet labSetConceptSetItem : labSetMemberConceptSetItems) {
            concepts.add(labSetConceptSetItem.getConcept());
        }
        return concepts;
    }

    private Collection<EditLabOrderItem> getConceptsForJSP(int orderId, int labSetId,
            Vector<Concept> allConceptsInSet, MyLabOrder order) {
        Concept labSetConcept = Context.getConceptService().getConcept(labSetId);
        ArrayList<Integer> checkedLabTestIds = order.getLabTestsId();
        Vector<EditLabOrderItem> conceptsForJSP = new Vector<EditLabOrderItem>();
        for (Concept conceptInSet : allConceptsInSet) {
            EditLabOrderItem item = new EditLabOrderItem(orderId, labSetConcept, conceptInSet, isConceptChecked(conceptInSet.getConceptId(), order));
            System.out.println(item);
            conceptsForJSP.add(item);
        }
        return conceptsForJSP;
    }

    private boolean isConceptChecked(int conceptIdInSet, MyLabOrder order) {
        int conId;
        for (Integer checkedConceptId : order.getLabTestsId()) {
            conId = checkedConceptId;
            if (conId == conceptIdInSet) {
                System.out.println("DWRMyConceptSetService - concept is checked: " + conId);
                return true;
            }
        }
        return false;
    }

}
