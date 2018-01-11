package com.kaf.apachekafka.storage;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
	public List<MessageEntity> findAll();
}
