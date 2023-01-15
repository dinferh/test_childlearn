package com.childlearn.service;

import com.childlearn.dto.CredentialDto;
import com.childlearn.entity.User;
import com.childlearn.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User createUser(User user) {
        String generatedUsername = generateUsername(user.getFullName());
        String generatedPassword = generatePassword(user.getDob());

        user.setUsername(generatedUsername);
        user.setPassword(generatedPassword);

//        return user;
        return repository.save(user);
    }

    public Optional<User> validateCredential(CredentialDto credentialDto) throws UserPrincipalNotFoundException {
        Optional<User> user = repository.findByUsernameAndPassword(credentialDto.getUsername(), credentialDto.getPassword());
        return user;
    }

    private String generateUsername(String fullName) {
        fullName = fullName.toLowerCase();
        List<String> names = List.of(fullName.split(" "));
        String firstName = names.get(0);
        String lastName = "";
        if (names.size() > 1) {
            lastName = names.get(names.size() - 1);
        }
        String thisYear = Year.now().toString();
        String twoYearsLater = Year.now().plusYears(2).toString();

        String entryYear = thisYear.substring(thisYear.length() - 2);
        String graduationYear = twoYearsLater.substring(twoYearsLater.length() - 2);

        String generatedUsername = firstName + lastName + entryYear + graduationYear;

        Integer counter = 1;

        while(repository.findByUsername(generatedUsername).isPresent()) {
            generatedUsername = firstName + lastName + counter.toString() + entryYear + graduationYear;
            if (repository.findByUsername(generatedUsername).isEmpty()) {
                return generatedUsername;
            } else {
                counter++;
            }
        }

        return generatedUsername;
    }

    private String generatePassword(Date dob) {
        log.info("DOB: " + dob.toString());
        String pattern = "MM-dd-yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String generatedPassword = simpleDateFormat.format(dob);

        generatedPassword = generatedPassword.replaceAll("-", "");

        return generatedPassword;
    }
}
