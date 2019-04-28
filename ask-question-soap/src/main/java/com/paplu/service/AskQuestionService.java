package com.paplu.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paplu.askquestion.Answer;
import com.paplu.askquestion.AskQuestionRequest;
import com.paplu.askquestion.AskQuestionResponse;
import com.paplu.askquestion.Questions;
import com.paplu.askquestion.Status;
import com.paplu.dao.AskQuestionRepository;
import com.paplu.entity.AskQuestionEntity;

@Service
public class AskQuestionService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AskQuestionRepository askQuestionRepository;
	
	
	public AskQuestionResponse processRequest(AskQuestionRequest request) {

		if (request != null) {
			if (request.getQuestions() != null && request.getQuestions().size() > 0) {
				return getAnswer(request.getQuestions()); // Call getAnswer method to get the answer for the question.
			} else if (request.getQuesAns() != null && request.getQuesAns().size() > 0) {
				return addQuestionAnswer(request.getQuesAns());// Call addQuestionAnswer method to save question and
																// Answer into the DB
			} else if (request.getType() != null) {
				return getSpecificAnswer(request.getType());// Call getSpecificAnswer method to get answer based upon
															// the type.
			}
		}
		return null;
	}
	

	private AskQuestionResponse getAnswer(List<Questions> questionList) {

		AskQuestionResponse response = new AskQuestionResponse();
		Status status = new Status();
		if (questionList != null && questionList.size() > 0) {

			for (Questions ques : questionList) {
				String question = ques.getQuestion().trim().replaceAll(" +", " ");
				List<AskQuestionEntity> questionAnswerList = askQuestionRepository.findByQuestion(question);
				if (questionAnswerList != null && questionAnswerList.size() > 0) {

					questionAnswerList.forEach(quesAns -> {
						Answer answer = new Answer();
						answer.setCategoryType(quesAns.getCategoryType());
						answer.setQuestion(quesAns.getQuestion());
						answer.setAnswer(quesAns.getAnswer());
						response.getAnswer().add(answer);
					});

					String[] words = question.split("\\s");
					for (String word : words) {
						if (word.length() > 3) {
							List<Object[]> relatedQuestions = askQuestionRepository.searchRelatedQuestion(word);
							relatedQuestions.forEach(relQues -> {
								Boolean ind = questionList.stream()
										.anyMatch(quest -> quest.getQuestion().equals(relQues[0].toString()));
								if (!ind) {
									Questions relatedQuestion = new Questions();
									relatedQuestion.setQuestion(relQues[0].toString());
									Boolean flag = response.getRelatedQuestion().stream().anyMatch(
											quest -> quest.getQuestion().equalsIgnoreCase(relQues[0].toString()));
									if (!flag) {
										response.getRelatedQuestion().add(relatedQuestion);
									}
								}
							});
						}
					}

				}
			}
		}
		if (response.getAnswer().size() == 0) {
			status.setStatus("Question not available.");
			response.setStatus(status);
		}
		return response;
	}

	private AskQuestionResponse getSpecificAnswer(String type) {

		List<AskQuestionEntity> quesAnsList = askQuestionRepository.findByCategoryType(type);

		if (quesAnsList != null && quesAnsList.size() > 0) {
			AskQuestionResponse response = new AskQuestionResponse();
			quesAnsList.forEach(quesAns -> {
				Answer answer = new Answer();
				answer.setQuestion(quesAns.getQuestion());
				answer.setAnswer(quesAns.getAnswer());
				answer.setCategoryType(quesAns.getCategoryType());
				response.getAnswer().add(answer);
			});
			return response;
		}
		return null;
	}

	private AskQuestionResponse addQuestionAnswer(List<Answer> quesAnsList) {

		try {
			AskQuestionResponse response = new AskQuestionResponse();
			Status status = new Status();

			quesAnsList.forEach(quesAns -> {
				AskQuestionEntity askQueEntity = new AskQuestionEntity();
				askQueEntity.setQuestion(quesAns.getQuestion());
				askQueEntity.setAnswer(quesAns.getAnswer());
				askQueEntity.setCategoryType(quesAns.getCategoryType());

				askQuestionRepository.save(askQueEntity);
			});

			status.setStatus("Added Successfully.");
			response.setStatus(status);
			return response;

		} catch (Exception ex) {
			logger.error("Exception in AskQuestionServiceImpl->addQuestionAnswer>> " + ex);
		}
		return null;
	}
}
