package com.bhavesh.resume.repository;

import com.bhavesh.resume.entity.ScreeningResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScreeningResultRepository extends CrudRepository<ScreeningResult, Long> {

    List<ScreeningResult> findByCandidateId(Long candidateId);

    List<ScreeningResult> findByJobDescriptionId(Long jobDescriptionId);
}
