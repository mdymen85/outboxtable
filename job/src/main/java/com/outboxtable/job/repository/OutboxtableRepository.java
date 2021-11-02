package com.outboxtable.job.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.outboxtable.job.entity.Outboxtable;

@Repository
public interface OutboxtableRepository extends CrudRepository<Outboxtable, Long> {
	
	List<Outboxtable> findAll();

}