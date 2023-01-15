package com.childlearn.restcontroller;

import com.childlearn.dto.TeacherSuggestDto;
import com.childlearn.dto.TeacherSuggestResponseDto;
import com.childlearn.entity.TeacherSuggest;
import com.childlearn.service.TeacherSuggestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class TeacherSuggestRestController {

    @Autowired
    private TeacherSuggestService service;

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        List<TeacherSuggestResponseDto> teacherSuggests = service.findAll().stream().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/find/")
                    .path(dbFile.getId().toString())
                    .toUriString();

            return new TeacherSuggestResponseDto(dbFile.getId(), dbFile.getCaption(), fileDownloadUri);

        }).collect(Collectors.toList());

        if (!teacherSuggests.isEmpty()) {
            return new ResponseEntity<>(teacherSuggests, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Teacher Suggest is empty", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<TeacherSuggest> teacherSuggest = service.findById(id);
        if (teacherSuggest.isPresent()) {
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "\"").body(teacherSuggest.get().getFile());
//            return new ResponseEntity<>(teacherSuggest.get().getFile(), HttpStatus.OK);
            byte[] bytes = teacherSuggest.get().getFile();
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Disposition", String.format("attachment; filename=file.png"));
            return ResponseEntity.ok().headers(httpHeaders).contentLength(bytes.length).body(resource);
        } else {
            return new ResponseEntity<>("Teacher Suggest with id " + id.toString() + " not found", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTeacherSuggest(@RequestParam("caption") String caption, @RequestParam("file") MultipartFile file) throws IOException {
        TeacherSuggestDto teacherSuggest = new TeacherSuggestDto(caption, file);
        try {
            service.createTeacherSuggest(teacherSuggest);
            return new ResponseEntity<>("Success upload file with name " + file.getOriginalFilename(), HttpStatus.CREATED);
        } catch (Exception e) {
            log.warn("Error upload file: " + e.getMessage());
            return new ResponseEntity<>("Could not upload file with name " + file.getOriginalFilename(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTeacherSuggest(@RequestBody TeacherSuggest teacherSuggest) {
        if (service.updateTeacherSuggest(teacherSuggest)) {
            return new ResponseEntity<>(teacherSuggest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Teacher Suggest with id " + teacherSuggest.getId() + " not found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTeacherSuggest(@PathVariable Long id) {
        if (service.deleteTeacherSuggest(id)) {
            return new ResponseEntity<>("Teacher Suggest with id " + id.toString() + " successfully deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Teacher Suggest with id " + id.toString() + " not found", HttpStatus.BAD_REQUEST);
        }
    }

}
