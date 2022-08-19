package com.beam.drive.service;

import com.beam.drive.model.File;
import com.beam.drive.model.Status;
import com.beam.drive.model.User;
import com.beam.drive.repository.FileRepository;
import com.beam.drive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    // Dosyayı bilgisayardaki bir konumdan server tarafına yükler.
    public void upload(MultipartFile file, User user) throws IllegalStateException, IOException {

        File newFile = new File()
                .setOwner(user)
                .setName(file.getOriginalFilename())
                .setSize(file.getSize());

        file.transferTo(new java.io.File("C:\\Users\\kagan.algul\\Desktop\\drive-v4-master\\drive-v4-master\\uploadFiles\\" + file.getOriginalFilename()));

        fileRepository.save(newFile);
    }

    // Kullanıcının dosyalarını listeler
    public List<File> list(User user) {

        List<File> fileList = fileRepository.findAllByOwner(user);

        return fileList;
    }

    // Kullanıcının kendi dosyalarını siler
    public void deleteMain(String id) {
        fileRepository.deleteById(id);
    }

    // Kullanıcının kendisiyle paylaşılan dosyalarını siler.
    public void deleteShareMe(String fileId, User user) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalFile.isPresent() && optionalUser.isPresent()) {
            User userDeneme = optionalUser.get();
            File file = optionalFile.get();
            file.getSharedList().remove(userDeneme);
            file.setSharedCount(file.getSharedCount() - 1);
            if (file.getSharedList().size() == 0) {
                file.setStatus(Status.UPLOAD);
            }
            fileRepository.save(file);
        }
    }

    // Kullanıcının diğer kullanıcılarla dosya paylaşmasını sağlar.
    public File share(String fileId, List<String> users, String username) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        File file = optionalFile.get();
        if (users.contains(username)) {
            users.remove(username);
        } else {
            for (String n : users) {
                Optional<User> optUser = userRepository.findByEmail(n);
                if (optUser.isPresent()) {
                    file.getSharedList().add(optUser.get());
                    file.setStatus(Status.SHARE);
                    file.setShareDate(new Date());
                    file.setSharedCount(file.getSharedCount() + 1);
                    fileRepository.save(file);
                }
            }
        }

        return file;
    }

    // Dosyaları byte şeklinde bir path'ten çekip indirir.
    public byte[] read(String filename) throws IOException {
        Path path = Paths.get("C:\\Users\\kagan.algul\\Desktop\\drive-v4-master\\drive-v4-master\\uploadFiles\\" + filename);
        if (path.toFile().exists()) {
            return Files.readAllBytes(path);
        } else {
            return null;
        }
    }

    // Kullanıcıyla paylaşılan dosyaları listeler.
    public List<File> sharedfilesMe(String email) {

        List<File> files = fileRepository.findBySharedListEmail(email);
        return files;
    }

    // Kullanıcının diğer kullanıcılarla paylaştığı dosyaları listeler.
    public List<File> sharedFilesOthers(String userId) {
        List<File> files = fileRepository.findByOwnerIdAndStatus(userId, Status.SHARE);
        return files;
    }

    // Kullanıcının dosya paylaştığı kişilerden paylaştığı dosyayı siler.
    public void deleteFromOthers(String id, String fileId) {
        Optional<User> optionalUser = userRepository.findById(id);
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalUser.isPresent() && optionalFile.isPresent()) {
            User user = optionalUser.get();
            File file = optionalFile.get();

           file.getSharedList().remove(user);
            file.setSharedCount(file.getSharedCount() - 1);
            fileRepository.save(file);
            if (file.getSharedCount() == 0) {
                file.setStatus(Status.UPLOAD);
            }
        }
    }

    public List<User> shareList(String id) {
        Optional<File> optionaFile = fileRepository.findById(id);
        if (optionaFile.isPresent()) {

            File file = optionaFile.get();
            List<User> users = file.getSharedList();
            return users;
        }
    return null;
    }
}
