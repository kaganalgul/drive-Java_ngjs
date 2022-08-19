package com.beam.drive.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@TypeAlias("File")
@Accessors(chain = true)
@Document(collection = "File")
public class File extends Base {
    private String name;
    @DBRef
    private User owner;
    private Date createDate = new Date();
    private Date shareDate;
    private int sharedCount = 0;
    private Status status = Status.UPLOAD;
    private List<User> sharedList = new ArrayList<>();
    private long size;
}
