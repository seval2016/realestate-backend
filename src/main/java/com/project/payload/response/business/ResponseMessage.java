package com.project.payload.response.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD
@Builder(toBuilder = true)

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<E> {
=======
@Builder(toBuilder=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage <E>{
>>>>>>> main

    private E object;
    private String message;
    private HttpStatus httpStatus;
<<<<<<< HEAD


=======
>>>>>>> main
}
