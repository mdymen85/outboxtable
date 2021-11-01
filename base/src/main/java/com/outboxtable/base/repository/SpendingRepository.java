package com.outboxtable.base.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.outboxtable.base.entity.Spending;

@Repository
public interface SpendingRepository extends CrudRepository<Spending, Long> {

}
