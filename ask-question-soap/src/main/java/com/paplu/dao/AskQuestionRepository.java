package com.paplu.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paplu.entity.AskQuestionEntity;

/**
 * @author Paplu Patel(M1048008)
 *
 */
@Repository
public interface AskQuestionRepository extends JpaRepository<AskQuestionEntity, Integer>{
	
	public List<AskQuestionEntity> findByCategoryType(String categoryType);
	
	@Query("SELECT a.question FROM AskQuestionEntity a WHERE a.question LIKE %:searchQuestion%")
	public List<Object[]> searchRelatedQuestion(@Param("searchQuestion") String  searchQuestion);
	
	public List<AskQuestionEntity> findByQuestion(String question);

}
