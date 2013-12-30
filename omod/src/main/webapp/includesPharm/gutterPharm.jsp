<ul class="navList">	

    <openmrs:hasPrivilege privilege="View Patients">
        <openmrs:hasPrivilege privilege="Pharm add prescription">
            <li id="findPatientNavLink">
                <b>Dispense Prescriptions</b>
            </li>
            <li id="adminNavLink">
                <a href="${pageContext.request.contextPath}/admin">OpemMRS Admin</a>
            </li>
        </openmrs:hasPrivilege>
    </openmrs:hasPrivilege>


</ul>