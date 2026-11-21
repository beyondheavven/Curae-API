package com.vention.curae.feedback.doctor.mapper;

import com.vention.curae.doctor.model.Doctor;
import com.vention.curae.feedback.doctor.dto.DoctorFeedbackCreateDto;
import com.vention.curae.feedback.doctor.dto.DoctorFeedbackDto;
import com.vention.curae.feedback.doctor.model.DoctorFeedback;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DoctorFeedbackMapperImplTest {

	private final DoctorFeedbackMapperImpl mapper = new DoctorFeedbackMapperImpl();

	@Test
	void toDtoHandlesNullAndFields() {
		Assertions.assertNull(mapper.toDto(null));

		Doctor doctor = Mockito.mock(Doctor.class);
		LocalDateTime createdAt = LocalDateTime.of(2026, 1, 1, 12, 0);
		DoctorFeedback entity = Mockito.mock(DoctorFeedback.class);
		Mockito.when(entity.getPatientId()).thenReturn(1L);
		Mockito.when(entity.getDoctor()).thenReturn(doctor);
		Mockito.when(doctor.getId()).thenReturn(2L);
		Mockito.when(entity.getRating()).thenReturn(5);
		Mockito.when(entity.getSummary()).thenReturn("Great");
		Mockito.when(entity.getCreatedAt()).thenReturn(createdAt);

		DoctorFeedbackDto dto = mapper.toDto(entity);

		Assertions.assertEquals(1L, dto.patientId());
		Assertions.assertEquals(2L, dto.doctorId());
		Assertions.assertEquals(5, dto.rating());
		Assertions.assertEquals("Great", dto.summary());
		Assertions.assertEquals(createdAt, dto.createdAt());

		DoctorFeedback emptyEntity = Mockito.mock(DoctorFeedback.class);
		Mockito.when(emptyEntity.getPatientId()).thenReturn(null);
		Mockito.when(emptyEntity.getRating()).thenReturn(null);

		DoctorFeedbackDto emptyDto = mapper.toDto(emptyEntity);

		Assertions.assertEquals(0L, emptyDto.patientId());
		Assertions.assertEquals(0L, emptyDto.doctorId());
		Assertions.assertEquals(0, emptyDto.rating());
		Assertions.assertNull(emptyDto.summary());
		Assertions.assertNull(emptyDto.createdAt());
	}

	@Test
	void toEntityHandlesNullAndFields() {
		Assertions.assertNull(mapper.toEntity(null, null, null));

		DoctorFeedbackCreateDto createDto = Mockito.mock(DoctorFeedbackCreateDto.class);
		Mockito.when(createDto.rating()).thenReturn(5);
		Mockito.when(createDto.summary()).thenReturn("Good");
		Doctor doctor = Mockito.mock(Doctor.class);

		DoctorFeedback entity = mapper.toEntity(createDto, doctor, 1L);

		Assertions.assertEquals(Integer.valueOf(5), entity.getRating());
		Assertions.assertEquals("Good", entity.getSummary());
		Assertions.assertSame(doctor, entity.getDoctor());
		Assertions.assertEquals(Long.valueOf(1L), entity.getPatientId());

		DoctorFeedback entityWithoutDto = mapper.toEntity(null, null, 1L);

		Assertions.assertNull(entityWithoutDto.getRating());
		Assertions.assertNull(entityWithoutDto.getSummary());
		Assertions.assertNull(entityWithoutDto.getDoctor());
		Assertions.assertEquals(Long.valueOf(1L), entityWithoutDto.getPatientId());
	}

}
