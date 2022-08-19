package com.beam.drive.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@TypeAlias("User")
@Document(collection = "User")
public class User extends Base {
    private String name;
    private String surname;
    private String email;
    private String password;
}
