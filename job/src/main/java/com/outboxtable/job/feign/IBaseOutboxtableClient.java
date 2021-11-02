package com.outboxtable.job.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.outboxtable.job.dto.OutboxtableDTO;

@FeignClient(value = "baseoutboxtable", url = "localhost:8082")
public interface IBaseOutboxtableClient {
	
	@RequestMapping(method = RequestMethod.GET, value="/api/v1/outboxtable")
	List<OutboxtableDTO> getOutboxtableNotIntegrated();

}
