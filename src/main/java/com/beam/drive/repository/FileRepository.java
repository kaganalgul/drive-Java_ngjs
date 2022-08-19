package com.beam.drive.repository;

import com.beam.drive.model.File;
import com.beam.drive.model.Status;
import com.beam.drive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
    public List<File> findAllByOwner(User user);

    public List<File> findBySharedListEmail(String user);

    public List<File> findByOwnerIdAndStatus(String id, Status status);
}
