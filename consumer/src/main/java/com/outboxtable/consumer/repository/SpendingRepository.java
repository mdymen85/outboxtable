package com.outboxtable.consumer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.outboxtable.consumer.entity.Spending;

@Repository
public interface SpendingRepository extends CrudRepository<Spending, Long> {

	boolean existsByIdentity(String identify);

}