package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.entity.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {

	void save(String key, QuantityMeasurementEntity entity);

	QuantityMeasurementEntity find(String key);
}