package com.childlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserDto {

    private String fullName;
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

}
