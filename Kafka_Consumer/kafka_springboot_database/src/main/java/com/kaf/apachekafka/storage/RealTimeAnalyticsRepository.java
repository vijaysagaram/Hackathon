package com.kaf.apachekafka.storage;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface RealTimeAnalyticsRepository extends JpaRepository<RealTimeAnalyticsEntity, Long> {
	public RealTimeAnalyticsEntity getByCustomerId(String customerId);
}