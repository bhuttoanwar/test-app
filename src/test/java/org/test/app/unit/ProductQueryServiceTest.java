package org.test.app.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.test.app.dto.ProductDtos;
import org.test.app.entities.Product;
import org.test.app.exceptions.BadRequestException;
import org.test.app.repo.ProductRepository;
import org.test.app.service.ProductQueryService;
import org.test.app.util.AppUtil;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	ProductRepository repo;

	@Mock
	AppUtil appUtil;

	@InjectMocks
	ProductQueryService service;

	@Test
	void getById_shouldReturnProduct_whenFound() {

		// Arrange
		Product product = Product.builder().id(1L).name("Laptop").price(BigDecimal.valueOf(1000)).deleted(false)
				.build();

		when(repo.findById(1L)).thenReturn(Optional.of(product));

		// Act
		ProductDtos.Response response = service.getById(1L);

		// Assert
		assertNotNull(response);
		assertEquals("Laptop", response.name());
		assertEquals(BigDecimal.valueOf(1000), response.price());
	}

	@Test
	void getById_shouldThrowException_whenProductNotFound() {

		// Arrange
		when(repo.findById(2L)).thenReturn(Optional.empty());

		// Act + Assert
		assertThrows(IllegalArgumentException.class, () -> service.getById(2L));
	}

}
