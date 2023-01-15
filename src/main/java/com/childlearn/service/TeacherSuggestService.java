package com.childlearn.service;

import com.childlearn.dto.TeacherSuggestDto;
import com.childlearn.entity.TeacherSuggest;
import com.childlearn.repository.TeacherSuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TeacherSuggestService {

    @Autowired
    private TeacherSuggestRepository teacherSuggestRepository;

    public List<TeacherSuggest> findAll() {
        return teacherSuggestRepository.findAll();
    }

    public Optional<TeacherSuggest> findById(Long id) {
        return teacherSuggestRepository.findById(id);
    }

    public void createTeacherSuggest(TeacherSuggestDto teacherSuggestDto) throws IOException {

        MultipartFile file = teacherSuggestDto.getFile();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        TeacherSuggest teacherSuggest = new TeacherSuggest();
        teacherSuggest.setCaption(teacherSuggestDto.getCaption());
        teacherSuggest.setFile(file.getBytes());
        teacherSuggest.setType(file.getContentType());

        teacherSuggestRepository.save(teacherSuggest);
    }

    public boolean updateTeacherSuggest(TeacherSuggest teacherSuggest) {
        if (teacherSuggestRepository.findById(teacherSuggest.getId()).isPresent()) {
            teacherSuggestRepository.save(teacherSuggest);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteTeacherSuggest(Long id) {
        if (teacherSuggestRepository.findById(id).isPresent()) {
            teacherSuggestRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
