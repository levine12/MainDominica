<%@ include file="/WEB-INF/template/include.jsp"%>
<!-- include file="/WEB-INF/template/header.jsp"   -->
<%@ include file="includesPharm/headerPharm.jsp" %>



<openmrs:htmlInclude file="/scripts/jquery/jquery.min.js" />
<openmrs:htmlInclude file="/dwr/engine.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/util.js" ></openmrs:htmlInclude>


<spring:hasBindErrors name="order">
    <openmrs:message code="fix.error"/>
    <br />
</spring:hasBindErrors>


<c:if test="${fn:length(orders) == 0}">
    <!-- There are no notes in the "notes" variable -->
    <br><b>NO ORDERS</b></br>
</c:if>
    <style type="text/css">
        
        
        
    </style>

    
    <h2>Restricted Drug Orders - Need Approval</h2>    
<span class="boxHeader"><openmrs:message code="Order.drug.list.title"/></span>

<div class="box">
    <form method="post">
        <table cellpadding="5">
            <tr>
                <th></th>
                <th>Patient</th>
                <th> Drug Name</th>
                <th> <openmrs:message code="general.dateStart"/> </th>
            <th> Duration </th>
            <th> Dose </th>
            <th> <openmrs:message code="DrugOrder.units"/> </th>
            <th> <openmrs:message code="DrugOrder.frequency"/> </th>
            <th> <openmrs:message code="DrugOrder.quantity"/> </th>
            <th> <openmrs:message code="general.instructions" /> </th>
            <th> Route of Administration </th>
            <th> Drug Approval Status</th>            
            </tr>

            <c:forEach var="ord" items="${orders}" varStatus="count">
                <tr>
                    <td valign="top"><a href="portlets/approveDrugs.form?orderId=${ord.orderId}" class="drugList"> <button type="button"> Process </button>  </a></td>

                    <td>
                        ${ord.patient.givenName} ${ord.patient.familyName} <a href="../../patientDashboard.form?patientId=${ord.patient.id}" target="_blank">&lt;&lt;Info&gt;&gt;</a>
                    </td>

                    <td>${ord.concept.name}</td>
                    <td><openmrs:formatDate date="${ord.startDate}" format="dd/MM/yyyy" /></td>
                <td>${duration[count.count-1]} ${durationUnits[count.count-1]}</td>   <!-- duration e.g. 5 weeks -->
                <td>${ord.dose}</td>                        
                <td>${units[count.count-1]}</td>
                <td>${frequency[count.count-1]} </td>
                <td>${ord.quantity} </td>
                <td>${ord.instructions} </td>
                <td>${route[count.count-1]}</td>
                <td>${drugstatus[count.count-1]}</td>                
                </tr>

            </c:forEach>
        </table>
                <div id = "approveDrugsEntry">
                    
                </div>       
        <br><br>
<!--        <input type="submit" value="Fill Selected Prescriptions" id="selectedorders" name="orderIds"   onClick="showBoxes(this.form);">-->

</div>            
<script type="text/javascript">
    var $j=jQuery.noConflict();                
        $j(document).ready(function() {
            
                $j('#approveDrugsEntry').dialog({
			autoOpen: false,
			modal: true,
			position: top,
			title: 'Approve Restricted Drug Order',
			width: '60%',                                                
		});
		$j('.drugList').click(function(e) {
                         e.preventDefault();
                         var url=$j(this).attr('href');                            
                         $j('#approveDrugsEntry').load(url);
			 $j('#approveDrugsEntry').dialog('open');                                                 
		});                                                              
  });  
</script>    

<%@ include file="/WEB-INF/template/footer.jsp" %>