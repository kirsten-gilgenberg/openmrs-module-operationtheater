package org.openmrs.module.operationtheater.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.EncounterType;
import org.openmrs.GlobalProperty;
import org.openmrs.OrderType;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.operationtheater.api.model.SurgicalAppointment;
import org.openmrs.module.operationtheater.api.model.SurgicalBlock;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@org.springframework.test.context.ContextConfiguration(locations = {
        "classpath:TestingApplicationContext.xml" }, inheritLocations = true)
public class SurgicalBlockServiceTest extends BaseModuleWebContextSensitiveTest {
	
	/** Mirrors the elevated privileges in SurgicalBlockServiceImpl#createAndLinkSurgeryOrder. */
	private static final List<String> PROXIED_PRIVILEGES = Arrays.asList(PrivilegeConstants.GET_ENCOUNTER_TYPES,
	    PrivilegeConstants.ADD_ENCOUNTERS, PrivilegeConstants.GET_VISITS, PrivilegeConstants.GET_VISIT_TYPES,
	    PrivilegeConstants.ADD_VISITS, PrivilegeConstants.GET_ORDER_TYPES, PrivilegeConstants.ADD_ORDERS,
	    PrivilegeConstants.GET_CONCEPTS, PrivilegeConstants.GET_CARE_SETTINGS, PrivilegeConstants.GET_VISIT_ATTRIBUTE_TYPES,
	    PrivilegeConstants.GET_GLOBAL_PROPERTIES, "Get Admission Locations", "Get Beds");
	
	private String superUser;
	
	private String superUserPassword;
	
	private String normalUser;
	
	private String normalUserPassword;
	
	private String userWithoutPrivilege;
	
	private String userWithoutPrivilegePassword;
	
	@Autowired
	SurgicalBlockService surgicalBlockService;
	
	SimpleDateFormat simpleDateFormat;
	
	@Before
	public void setUp() throws Exception {
		superUser = "test-user";
		superUserPassword = "test";
		normalUser = "normal-user";
		normalUserPassword = "normal-password";
		userWithoutPrivilege = "user-without-privilege";
		userWithoutPrivilegePassword = "test";
		executeDataSet("SurgicalBlock.xml");
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	@Test
	public void shouldPassGetSurgicalAppointmentsIfTheUserHasManageOTSchedulesPrivileges() {
		Context.authenticate(superUser, superUserPassword);
		assertNotNull(surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d109f1"));
	}
	
	@Test
	public void shouldPassGetSurgicalAppointmentsIfTheUserHasViewOTSchedulesPrivileges() {
		Context.authenticate(normalUser, normalUserPassword);
		assertNotNull(surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d109f1"));
	}
	
	@Test(expected = APIAuthenticationException.class)
	public void shouldThrowAuthenticationExceptionIfUserDoesNotHaveSufficientPrivileges() {
		Context.authenticate(userWithoutPrivilege, userWithoutPrivilegePassword);
		assertNotNull(surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d109f1"));
	}
	
	@Test(expected = APIAuthenticationException.class)
	public void shouldThrowAuthenticationExceptionIfUserDoesNotHaveManageOTSchedulesPrivileges() {
		Context.authenticate(normalUser, normalUserPassword);
		surgicalBlockService.save(new SurgicalBlock());
	}
	
	@Test
	public void shouldPassGetSurgicalBlocksWithinDateRangeIfTheUserHasManageOTSchedulesPrivileges() {
		Context.authenticate(superUser, superUserPassword);
		assertNotNull(surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d109f1"));
	}
	
	@Test
	public void shouldPassGetSurgicalBlocksWithinDateRangeIfTheUserHasViewOTSchedulesPrivileges() {
		Context.authenticate(normalUser, normalUserPassword);
		assertNotNull(surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d109f1"));
	}
	
	@Test(expected = APIAuthenticationException.class)
	public void shouldThrowAuthenticationExceptionIfUserDoesNotHaveGetSurgicalBlocksPrivileges() {
		Context.authenticate(userWithoutPrivilege, userWithoutPrivilegePassword);
		assertNotNull(surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d109f1"));
	}
	
	@Test
	public void shouldPassGetSurgicalBlocksBetweenStartDatetimeAndEndDatetimeIfTheUserHasManageOTSchedulesPrivileges()
	        throws ParseException {
		Date startDatetime = simpleDateFormat.parse("2017-04-25 09:00:00.0");
		Date endDatetime = simpleDateFormat.parse("2017-04-25 14:45:00");
		Context.authenticate(superUser, superUserPassword);
		assertNotNull(surgicalBlockService.getSurgicalBlocksBetweenStartDatetimeAndEndDatetime(startDatetime, endDatetime,
		    false, false));
	}
	
	@Test
	public void shouldPassGetSurgicalBlocksBetweenStartDatetimeAndEndDatetimeIfTheUserHasViewOTSchedulesPrivileges()
	        throws ParseException {
		Date startDatetime = simpleDateFormat.parse("2017-04-25 09:00:00.0");
		Date endDatetime = simpleDateFormat.parse("2017-04-25 14:45:00");
		Context.authenticate(normalUser, normalUserPassword);
		assertNotNull(surgicalBlockService.getSurgicalBlocksBetweenStartDatetimeAndEndDatetime(startDatetime, endDatetime,
		    false, false));
	}
	
	@Test(expected = APIAuthenticationException.class)
	public void shouldThrowAuthenticationExceptionIfUserDoesNotHaveGetSurgicalBlocksBetweenStartDatetimeAndEndDatetimePrivileges()
	        throws ParseException {
		Date startDatetime = simpleDateFormat.parse("2017-04-25 09:00:00.0");
		Date endDatetime = simpleDateFormat.parse("2017-04-25 14:45:00");
		Context.authenticate(userWithoutPrivilege, userWithoutPrivilegePassword);
		assertNotNull(surgicalBlockService.getSurgicalBlocksBetweenStartDatetimeAndEndDatetime(startDatetime, endDatetime,
		    false, false));
	}
	
	@Test
	public void shouldCreateSurgeryOrderForNewAppointmentWithoutRequiringClinicalPrivilegesOnOTRole() throws ParseException {
		Context.authenticate("admin", "test");
		setupSurgeryOrderMetadata();
		org.openmrs.Patient patient = Context.getPatientService().getPatient(10);
		
		Context.authenticate(superUser, superUserPassword);
		
		SurgicalBlock block = surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d10921");
		SurgicalAppointment newAppointment = new SurgicalAppointment();
		newAppointment.setPatient(patient);
		newAppointment.setSurgicalBlock(block);
		block.getSurgicalAppointments().add(newAppointment);
		
		SurgicalBlock saved = surgicalBlockService.save(block);
		
		SurgicalAppointment savedAppointment = saved.getSurgicalAppointments().stream()
		        .filter(a -> a.getPatient().getId().equals(10) && !a.getVoided()).findFirst().orElse(null);
		assertNotNull("New appointment should have a linked Surgery Order", savedAppointment.getOrder());
		assertEquals("Surgery Order", savedAppointment.getOrder().getOrderType().getName());
	}
	
	@Test
	public void shouldRemoveProxyPrivilegesAfterSurgeryOrderCreationSucceeds() {
		Context.authenticate("admin", "test");
		setupSurgeryOrderMetadata();
		org.openmrs.Patient patient = Context.getPatientService().getPatient(10);
		Context.authenticate(superUser, superUserPassword);
		// Guard against a vacuous pass: the OT user must not hold these privileges on its own.
		assertProxyPrivilegesNotHeld("before save, ");
		
		SurgicalBlock saved = saveBlockWithNewAppointmentFor(patient);
		
		assertNotNull("Surgery order creation should have run", findAppointmentFor(saved, patient).getOrder());
		assertProxyPrivilegesNotHeld("after successful save, ");
	}
	
	@Test
	public void shouldRemoveProxyPrivilegesWhenSurgeryOrderCreationThrows() {
		Context.authenticate("admin", "test");
		setupSurgeryOrderMetadata();
		pointSurgeryOrderTypeAtMismatchedJavaClass();
		org.openmrs.Patient patient = Context.getPatientService().getPatient(10);
		Context.authenticate(superUser, superUserPassword);
		assertProxyPrivilegesNotHeld("before save, ");
		
		try {
			saveBlockWithNewAppointmentFor(patient);
			fail("Order creation should have thrown, otherwise this test does not exercise the exception path");
		}
		catch (APIException expected) {
			// the failure must not leave the elevated privileges on the user's context
		}
		
		assertProxyPrivilegesNotHeld("after failed save, ");
	}
	
	private SurgicalBlock saveBlockWithNewAppointmentFor(org.openmrs.Patient patient) {
		SurgicalBlock block = surgicalBlockService.getSurgicalBlockWithAppointments("5580cddd-c290-66c8-8d3a-96dc33d10921");
		SurgicalAppointment newAppointment = new SurgicalAppointment();
		newAppointment.setPatient(patient);
		newAppointment.setSurgicalBlock(block);
		block.getSurgicalAppointments().add(newAppointment);
		return surgicalBlockService.save(block);
	}
	
	private SurgicalAppointment findAppointmentFor(SurgicalBlock block, org.openmrs.Patient patient) {
		return block.getSurgicalAppointments().stream()
		        .filter(a -> a.getPatient().getId().equals(patient.getId()) && !a.getVoided()).findFirst().orElse(null);
	}
	
	/** Core rejects an Order whose class is not assignable to the order type's java class. */
	private void pointSurgeryOrderTypeAtMismatchedJavaClass() {
		OrderType mismatched = new OrderType("Mismatched Surgery Order", "Order type used to force an order save failure",
		        "org.openmrs.TestOrder");
		mismatched.setUuid("d2f4e9b3-5a8c-4b0f-86d7-e3a9f4b2c5d8");
		mismatched = Context.getOrderService().saveOrderType(mismatched);
		Context.getAdministrationService()
		        .saveGlobalProperty(new GlobalProperty("operationtheater.surgeryOrderTypeUuid", mismatched.getUuid()));
	}
	
	private void assertProxyPrivilegesNotHeld(String when) {
		for (String privilege : PROXIED_PRIVILEGES) {
			assertFalse(when + "proxy privilege should not be held: " + privilege, Context.hasPrivilege(privilege));
		}
	}
	
	private void setupSurgeryOrderMetadata() {
		EncounterType encounterType = new EncounterType("SURGERY_SCHEDULING",
		        "Encounter created during surgery scheduling to anchor the Surgery Order");
		encounterType.setUuid("a2d9f6b3-1c4e-4a7f-b8d2-e5f3c1a6d9b4");
		encounterType = Context.getEncounterService().saveEncounterType(encounterType);
		
		OrderType orderType = new OrderType("Surgery Order",
		        "Order created automatically when a surgical appointment is scheduled", "org.openmrs.Order");
		orderType.setUuid("c1e3d8a2-4f7b-4a9e-b5c6-d2f8e3a1b4c7");
		orderType = Context.getOrderService().saveOrderType(orderType);
		
		ConceptDatatype naDatatype = Context.getConceptService().getConceptDatatypeByName("N/A");
		ConceptClass miscClass = Context.getConceptService().getConceptClassByName("Misc");
		Concept concept = new Concept();
		concept.setDatatype(naDatatype);
		concept.setConceptClass(miscClass);
		concept.setUuid("c8a89784-e16e-4929-ab5c-be2f3d47f2de");
		concept.addName(new org.openmrs.ConceptName("General Surgical Procedure", java.util.Locale.ENGLISH));
		concept = Context.getConceptService().saveConcept(concept);
		
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty("operationtheater.surgerySchedulingEncounterTypeUuid", encounterType.getUuid()));
		Context.getAdministrationService()
		        .saveGlobalProperty(new GlobalProperty("operationtheater.surgeryOrderTypeUuid", orderType.getUuid()));
		Context.getAdministrationService()
		        .saveGlobalProperty(new GlobalProperty("operationtheater.surgicalOrderConceptUuid", concept.getUuid()));
		Context.getAdministrationService().saveGlobalProperty(
		    new GlobalProperty("visits.assignmentHandler", "org.openmrs.api.handler.ExistingOrNewVisitAssignmentHandler"));
	}
	
}
