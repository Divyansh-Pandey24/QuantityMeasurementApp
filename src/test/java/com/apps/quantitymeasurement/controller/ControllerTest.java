package com.apps.quantitymeasurement.controller;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.apps.quantitymeasurement.dto.QuantityDTO;
import com.apps.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import com.apps.quantitymeasurement.service.QuantityMeasurementServiceImpl;

public class ControllerTest {

	private QuantityMeasurementController controller;

	@BeforeEach
	void setup() {

		IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(
				new QuantityMeasurementCacheRepository());

		controller = new QuantityMeasurementController(service);
	}

	@Test
	void givenLengthDTO_WhenAdded_ShouldReturnCorrectValue() {

		QuantityDTO q1 = new QuantityDTO(5, "FEET", "LENGTH");
		QuantityDTO q2 = new QuantityDTO(24, "INCHES", "LENGTH");

		QuantityDTO result = controller.performAddition(q1, q2);

		assertEquals(7, result.getValue());
	}
}