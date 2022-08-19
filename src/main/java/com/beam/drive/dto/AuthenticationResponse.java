package com.beam.drive.dto;

import com.beam.drive.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthenticationResponse {

    /*
    *  1 -> user not found
    *  0 -> ok
    *  -1 -> password incorrect
    * */

    private int code;
    private User user;

}
