package org.openmrs.module.dominicamodule;

import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dominicamodule.db.GenericDAO;
import org.openmrs.module.dominicamodule.impl.NoteServiceImpl;

/**
 * This is the interface for our database interaction with Note objects. The
 * methods in here must be implemented by our {@link NoteServiceImpl} class.
 * <br/>
 * <br/>
 * This class is referenced from /metadata/moduleApplicationContext.xml so that
 * developers can call
 * <code>Context.getService(NoteService.class).saveNote(note)</code> <br/>
 * <br/>
 * We extend {@link OpenmrsService} so that the onStartup and onShutdown methods
 * can be called while our service is being created for the first time
 *
 */
public interface LabOrderService extends OpenmrsService {

    /**
     * This persists our LabOrder object to the database. If
     * {@link LabOrder#getId()} is null, then an "insert" is done. If it is
     * non-null, then a sql update is done by hibernate. <br/>
     * <br/>
     * The "@should" in this javadoc below is an openmrs-specific thing that has
     * to do with unit tests. The content of the @should says what the unit
     * tests should be testing. See {@link LabOrderServiceTest} for the matching
     * unit test.
     *
     * @param labOrder The {@link LabOrder} object to save in the database.
     * @return The LabOrder that was saved. {@link LabOrder#getId()} will now be
     * filled in if it was null.
     *
     * @should create a LabOrder
     */
    public LabOrder saveLabOrder(LabOrder labOrder);

    /**
     * Get a {@link LabOrder} object by primary key id.
     *
     * @param id the primary key integer id to look up on
     * @return the found LabOrder object which matches the row with the given
     * id. If no row with the given id exists, null is returned.
     */
    public LabOrder getLabOrder(Integer id);

    /**
     * Get a {@link LabOrder} by uuid. The uuid is GLOBALLY unique and a 36
     * character string. No other LabOrder ever anywhere anytime in this
     * universe will have the same string.
     *
     * @param uuid the uuid to look up
     * @return the found {@link LabOrder} object which has the given uuid
     * property
     */
    public LabOrder getLabOrderByUuid(String uuid);

    /**
     * Get {@link LabOrder}s that have the given {@link Patient}'s patient_id in
     * the patientnotes.patient_id column.
     *
     * @param patient the Patient object to look up. The
     * {@link Patient#getPatientId()} property is used to match against the
     * patientLabOrders.patient_id column
     * @return a list of {@link LabOrder} objects that the given {@link Patient}
     * has
     */
    public List<LabOrder> getLabOrdersByPatient(Patient patient);

    public LabOrder getLabOrderByOrderId(Integer id);

}
