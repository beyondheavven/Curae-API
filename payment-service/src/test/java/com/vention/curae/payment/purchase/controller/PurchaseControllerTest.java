package com.vention.curae.payment.purchase.controller;

import com.vention.curae.payment.purchase.dto.PurchaseReportDto;
import com.vention.curae.payment.purchase.service.PurchaseService;
import com.vention.curae.wrapper.ResponseWrapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseControllerTest {

	@Mock
	private PurchaseService purchaseService;

	@InjectMocks
	private PurchaseController controller;

	@Test
	void getReportReturnsPurchaseReport() {
		LocalDateTime from = LocalDateTime.of(2026, 1, 1, 0, 0);
		LocalDateTime to = LocalDateTime.of(2026, 2, 1, 0, 0);
		Pageable pageable = PageRequest.of(0, 20);
		PurchaseReportDto report = new PurchaseReportDto(100L, 1L, 0L, Page.empty());
		when(purchaseService.getReport(from, to, pageable)).thenReturn(report);

		ResponseWrapper<PurchaseReportDto> result = controller.getReport(from, to, pageable);

		assertThat(result.getStatus()).isEqualTo(HttpStatus.OK);
		assertThat(result.getData()).isSameAs(report);
		verify(purchaseService).getReport(from, to, pageable);
	}

}
