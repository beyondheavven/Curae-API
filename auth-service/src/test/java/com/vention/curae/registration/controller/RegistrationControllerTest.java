package com.vention.curae.registration.controller;

import com.vention.curae.registration.dto.DoctorRegistrationRequestDto;
import com.vention.curae.registration.dto.PatientRegistrationRequestDto;
import com.vention.curae.registration.service.RegistrationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

	@Mock
	private RegistrationService registrationService;

	@InjectMocks
	private RegistrationController registrationController;

	@Test
	void registerPatientDelegatesToService() {
		PatientRegistrationRequestDto request = mock(PatientRegistrationRequestDto.class);

		registrationController.registerPatient(request);

		verify(registrationService).registerPatient(request);
	}

	@Test
	void registerDoctorDelegatesToService() {
		DoctorRegistrationRequestDto request = mock(DoctorRegistrationRequestDto.class);

		registrationController.registerDoctor(request);

		verify(registrationService).registerDoctor(request);
	}

}
