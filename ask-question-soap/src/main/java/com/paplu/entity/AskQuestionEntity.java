package com.paplu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "QUESTION_ANSWER")
public class AskQuestionEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "QUESTIONS")
	private String question;
	@Column(name = "ANSWERS")
	private String answer;
	@Column(name = "CATEGORY_TYPE")
	private String categoryType;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
}
