package com.vention.curae.feedback.clinic.mapper;

import com.vention.curae.clinic.model.Clinic;
import com.vention.curae.feedback.clinic.dto.ClinicFeedbackCreateDto;
import com.vention.curae.feedback.clinic.dto.ClinicFeedbackDto;
import com.vention.curae.feedback.clinic.model.ClinicFeedback;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClinicFeedbackMapperImplTest {

	private final ClinicFeedbackMapperImpl mapper = new ClinicFeedbackMapperImpl();

	@Test
	void toDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toDto(null));

		Clinic clinic = Mockito.mock(Clinic.class);
		LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 12, 0);
		ClinicFeedback entity = Mockito.mock(ClinicFeedback.class);
		Mockito.when(entity.getPatientId()).thenReturn(1L);
		Mockito.when(entity.getClinic()).thenReturn(clinic);
		Mockito.when(clinic.getId()).thenReturn(2L);
		Mockito.when(entity.getRating()).thenReturn(5);
		Mockito.when(entity.getSummary()).thenReturn("Great");
		Mockito.when(entity.getCreatedAt()).thenReturn(createdAt);

		ClinicFeedbackDto dto = mapper.toDto(entity);

		Assertions.assertEquals(1L, dto.patientId());
		Assertions.assertEquals(2L, dto.clinicId());
		Assertions.assertEquals(5, dto.rating());
		Assertions.assertEquals("Great", dto.summary());
		Assertions.assertEquals(createdAt, dto.createdAt());

		ClinicFeedback emptyEntity = Mockito.mock(ClinicFeedback.class);
		Mockito.when(emptyEntity.getPatientId()).thenReturn(null);
		Mockito.when(emptyEntity.getRating()).thenReturn(null);

		ClinicFeedbackDto emptyDto = mapper.toDto(emptyEntity);

		Assertions.assertEquals(0L, emptyDto.patientId());
		Assertions.assertEquals(0L, emptyDto.clinicId());
		Assertions.assertEquals(0, emptyDto.rating());
		Assertions.assertNull(emptyDto.summary());
		Assertions.assertNull(emptyDto.createdAt());
	}

	@Test
	void toEntityHandlesNullAndFields() {
		Assertions.assertNull(mapper.toEntity(null, null, null));

		ClinicFeedbackCreateDto createDto = Mockito.mock(ClinicFeedbackCreateDto.class);
		Mockito.when(createDto.rating()).thenReturn(5);
		Mockito.when(createDto.summary()).thenReturn("Good");
		Clinic clinic = Mockito.mock(Clinic.class);

		ClinicFeedback entity = mapper.toEntity(createDto, clinic, 1L);

		Assertions.assertEquals(Integer.valueOf(5), entity.getRating());
		Assertions.assertEquals("Good", entity.getSummary());
		Assertions.assertSame(clinic, entity.getClinic());
		Assertions.assertEquals(Long.valueOf(1L), entity.getPatientId());

		ClinicFeedback entityWithoutDto = mapper.toEntity(null, null, 1L);

		Assertions.assertNull(entityWithoutDto.getRating());
		Assertions.assertNull(entityWithoutDto.getSummary());
		Assertions.assertNull(entityWithoutDto.getClinic());
		Assertions.assertEquals(Long.valueOf(1L), entityWithoutDto.getPatientId());
	}

}
