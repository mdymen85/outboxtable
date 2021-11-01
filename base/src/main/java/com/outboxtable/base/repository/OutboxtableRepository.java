package com.outboxtable.base.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.outboxtable.base.entity.Outboxtable;

@Repository
public interface OutboxtableRepository extends CrudRepository<Outboxtable, Long> {

	List<Outboxtable> findByIntegrated(boolean b);

}
