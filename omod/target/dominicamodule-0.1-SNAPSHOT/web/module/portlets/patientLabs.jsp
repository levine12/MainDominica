<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:htmlInclude file="/scripts/jquery/jquery.min.js" />
<openmrs:htmlInclude file="/dwr/engine.js" ></openmrs:htmlInclude>
<openmrs:htmlInclude file="/dwr/util.js" ></openmrs:htmlInclude>

<script src="<openmrs:contextPath/>/dwr/interface/DWRMyConceptSetService.js"></script>
<div id="labsPortlet">


    <spring:hasBindErrors name="order">
        <openmrs:message code="fix.error"/>
        <br />
    </spring:hasBindErrors>
    <style>
        div.scroll
        {
            height:300px;
            overflow:scroll;
        }
    </style>

    <SCRIPT LANGUAGE="JavaScript">

        $j(document).ready(function() {
            $j('#fillorderdiv').hide();
            $j('#editorderdiv').hide();
            $j('#selectlabtest').show();

            $j('#selectlabtest').change(function() {
                var selectedValue = $j('#selectlabtest').val();
                if (selectedValue != "empty") {
                    document.getElementById("labsett").value = selectedValue;
                    $j('#fillorderdiv').show();
                    //alert("selected concept: " + $j('#selectlabtest').val());
                    DWRMyConceptSetService.getLabSetConceptsForConceptId($j('#selectlabtest').val(),
                            function(ret) {

                                var table = document.getElementById("labTable");
                                for (var i = table.rows.length; i > 0; i--) {
                                    document.getElementById("labTable").deleteRow(i - 1);
                                }

                                var row = 0;

                                for (var i = 0; i < ret.length; i++) {
                                    if ((i % 2) == 0) {
                                        row = table.insertRow(i / 2);
                                        var cell1 = row.insertCell(0);
                                        var cell2 = row.insertCell(1);
                                        cell1.innerHTML = "<input type='checkbox' name='labid' > ";
                                        cell2.innerHTML = ret[i].conceptName;
                                        var td = row.childNodes[0];
                                        var inp = td.childNodes[0];
                                        inp.value = ret[i].conceptId;
                                    }
                                    else {
                                        var cell3 = row.insertCell(2);
                                        var cell4 = row.insertCell(3);
                                        cell3.innerHTML = "<input type='checkbox' name='labid' > ";
                                        cell4.innerHTML = ret[i].conceptName;
                                        var td = row.childNodes[2];
                                        var inp = td.childNodes[0];
                                        inp.value = ret[i].conceptId;
                                    }
                                }

                            });
                }

            });

            $j("button[name='editOrder']").click(function() {
                var strSplit = (this.value).split("/");

                var orderId = strSplit[0];
                var instructions = strSplit[1];
                $j('#instructionsedit').val(instructions);

                DWRMyConceptSetService.getCheckBoxConceptsForOrder(orderId,
                        function(ret) {
                            if (ret === null) {
                                alert("Order is Filled - not allowed to edit");
                            }
                            else {
                                var table = document.getElementById("labTableEdit");
                                for (var i = table.rows.length; i > 0; i--) {
                                    document.getElementById("labTable").deleteRow(i - 1);
                                }

                                $j('#fillorderdiv').hide();
                                $j('#editorderdiv').show();
                                $j('#selectlabtest').hide();

                                document.getElementById("editLabset").innerHTML = ret[0].labSetName;
                                document.getElementById("sendOrderId").value = ret[0].orderId;
                                document.getElementById("labsett").value = ret[0].labSetConceptId;

                                var row = 0;

                                for (var i = 0; i < ret.length; i++) {
                                    if ((i % 2) == 0) {
                                        row = table.insertRow(i / 2);
                                        var cell1 = row.insertCell(0);
                                        var cell2 = row.insertCell(1);
                                        if (ret[i].isChecked) {
                                            cell1.innerHTML = "<input type='checkbox' name='labid' checked> ";
                                        } else {
                                            cell1.innerHTML = "<input type='checkbox' name='labid' > ";
                                        }

                                        cell2.innerHTML = ret[i].labTestConceptName;
                                        var td = row.childNodes[0];
                                        var inp = td.childNodes[0];
                                        inp.value = ret[i].labTestConceptId;
                                    }
                                    else {
                                        var cell3 = row.insertCell(2);
                                        var cell4 = row.insertCell(3);
                                        if (ret[i].isChecked) {
                                            cell3.innerHTML = "<input type='checkbox' name='labid' checked> ";
                                        } else {
                                            cell3.innerHTML = "<input type='checkbox' name='labid' > ";
                                        }

                                        cell4.innerHTML = ret[i].labTestConceptName;
                                        var td = row.childNodes[2];
                                        var inp = td.childNodes[0];
                                        inp.value = ret[i].labTestConceptId;
                                    }
                                }
                            }
                        });
            });

        });

        function showBoxesLabs(frm) {
            var checkedLabIds = " ";
            var labid = frm.form.labid;
            var len = labid.length;
            var checkedLabIds = " ";
            checkedLabIds = checkedLabIds + document.getElementById("labsett").value + ",";

            //For each checkbox see if it has been checked, record the value.
            for (i = 0; i < len; i++)
                if (labid[i].checked) {
                    checkedLabIds = checkedLabIds + labid[i].value + ",";
                }
            //labIds.value = checkedLabIds;
            frm.form.value = checkedLabIds;
            document.getElementById("submitbutton").value = checkedLabIds;
            var x = 1;
            //alert(frm.form.value);
        }

        function showBoxesLabs1(frm) {
            var checkedLabIds = " ";
            var labid = frm.form.labid;
            var len = labid.length;
            var checkedLabIds = " ";
            checkedLabIds = checkedLabIds + document.getElementById("labsett").value + ",";

            //For each checkbox see if it has been checked, record the value.
            for (i = 0; i < len; i++)
                if (labid[i].checked) {
                    checkedLabIds = checkedLabIds + labid[i].value + ",";
                }
            //labIds.value = checkedLabIds;
            frm.form.value = checkedLabIds;
            document.getElementById("labIds1").value = checkedLabIds;
            var x = 1;
            //alert(frm.form.value);
        }

        function checkAll(frm, checkedValue) {
            var labid = frm.form.labid;
            var len = labid.length;
            for (i = 0; i < len; i++) {
                var x = labid[i];
                labid[i].checked = checkedValue;
            }
        }


    </SCRIPT>


    <a href="http://localhost:8081/openmrs-standalone/module/dominicamodule/patientLabsLabTech.form">Lab Tech UI</a> 


    <span class="boxHeader">Prior Lab Orders</span>
    <div class="box">
        <div class="scroll">
            <table cellpadding="5" >
                <tr>
                    <th></th>

                    <!--            <th>Order Id </th>
                                <th>Disc</th>
                                <th>Void</th>
                    -->
                    <th>Processed</th>
                    <th> Ordering Physician</th>
                    <th> Date </th>
                    <th> Instructions </th>
                    <th> Lab Set </th>
                    <th> Lab Tests </th>
                </tr>
                <c:forEach var="laborder" items="${prior_orders}">
                    <tr>
                        <c:choose>
                            <c:when test="${laborder.isFilled == true}">
                                <td><button type="button" name="editOrder"  value="${laborder.orderId}/${laborder.orderInstructions}" disabled />Edit</button></td>
                                <td>yes</td> 
                            </c:when>

                            <c:otherwise>
                                <td><button type="button" name="editOrder"  value="${laborder.orderId}/${laborder.orderInstructions}" />Edit</button></td>
                                <td>no</td>                        
                            </c:otherwise>
                        </c:choose>
                        <td>${laborder.orderingPhysician.givenName} &nbsp; ${laborder.orderingPhysician.familyName}</td>
                        <td>${laborder.orderDate}</td>
                        <td>${laborder.orderInstructions} </td>
                        <td> <b>${laborder.labSet.name}</b></td>
                        <td>
                            <c:forEach var="labtest" items="${laborder.labTests}">
                                ${labtest.name}, &nbsp; &nbsp;
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>

    <br><br>
    <div class="box" style="border: 1px black solid">
        <select name="labtest" id="selectlabtest" >
            <option name="lset" value="empty">&Lt;Select Lab Set&Gt;</option>
            <c:forEach var="labset" items="${labsets}">
                <option name="lset" value="${labset}">${labset.name}</option>
            </c:forEach>   
        </select>
    </div>

    <input type="hidden" id="patientId" name="patient_id" value="${patient.patientId}"/>  
    <input type="hidden" id="labsett" name="labsett"/>  

    <div class="box" id="fillorderdiv">
        <form method="post" id="checkform">
            <button type="button" name="selectAll"  id="selectAll" onClick="checkAll(this, true);"/>Select All Tests</button>
            <button type="button" name="deselectAll"  id="deselectAll" onClick="checkAll(this, false);"/>Deselect All Tests</button>
            <table cellpadding="5" id="labTable">
            </table>
            <br> Lab Order Instructions: <input type='text' id="instructions" size="100" name='instructions'> 
            <br><br>

            <input type="submit" id="submitbutton" value="Fill Selected Lab Tests" name="labIds"   onClick="showBoxesLabs(this);">

        </form>
    </div>

    <div class="box" id="editorderdiv">
        <form method="post">
            <h2 id="editLabset"></h2>
            <table cellpadding="5" id="labTableEdit">
            </table>
            <br> Lab Order Instructions: <input type='text' id="instructionsedit" size="100" name='instructionsedit'> 
            <br><br><br><br>

            <input type="submit" id="labIds1" value="Fill Selected Lab Tests" name="labIds1"   onClick="showBoxesLabs1(this);">
            <br><br><br><br>
            <input type="submit" id="discontinueOrder" value="Discontinue Lab Order" name="discontinueOrder" >
            Discontinue Reason: <input type='text' id="discontinuereason" size="100" name='discontinuereason'> 
            <br><br>
            <input type="submit" id="voidOrder" value="Void Lab Order" name="voidOrder">
            Void Reason: <input type='text' id="voidreason" size="100" name='voidreason'> 

            <input type="hidden" id="sendOrderId" name="sendOrderId"/>  
        </form>
    </div>

</div>