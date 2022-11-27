package com.cloudcomputing.service;

import com.cloudcomputing.entity.Subject;
import com.cloudcomputing.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public List<Subject> getSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> getSubjectsByContainKeyword(String keyword) {
        return subjectRepository.findAllByContainKeyword(keyword);
    }

    public Subject getSubjectById(Integer id) {
        return subjectRepository.findOneById(id);
    }

}
