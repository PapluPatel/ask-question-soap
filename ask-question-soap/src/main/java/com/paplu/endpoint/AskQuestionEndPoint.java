package com.paplu.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.paplu.askquestion.AskQuestionRequest;
import com.paplu.askquestion.AskQuestionResponse;
import com.paplu.service.AskQuestionService;

@Endpoint
public class AskQuestionEndPoint {

	@Autowired
	private AskQuestionService askQuestionService;
	
	@PayloadRoot(namespace ="http://paplu.com/askquestion",
			localPart = "askQuestionRequest")
	@ResponsePayload
	public AskQuestionResponse askQuestionRequest(@RequestPayload AskQuestionRequest request) {
		return askQuestionService.processRequest(request);
	}
}
