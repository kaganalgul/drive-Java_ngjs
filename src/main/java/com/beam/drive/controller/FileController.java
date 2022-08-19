package com.beam.drive.controller;

import com.beam.drive.dto.ShareRequest;
import com.beam.drive.model.File;
import com.beam.drive.model.User;
import com.beam.drive.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.beam.drive.service.UserService.SESSION_ACCOUNT;

@RequiredArgsConstructor
@RestController
@RequestMapping("file")
public class FileController {

    private final FileService fileService;

    @PostMapping("upload")
    public void upload(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        fileService.upload(file, user);
    }

    @GetMapping("list")
    public List<File> list(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        return fileService.list(user);
    }

    @GetMapping("share-list/{id}")
    public List<User> shareList(@PathVariable String id) {
        return fileService.shareList(id);
    }

    @PostMapping("delete-main/{id}")
    public void deleteMain(@PathVariable("id") String id) {
        fileService.deleteMain(id);
    }

    @PostMapping("delete-shared-me/{id}")
    public void deleteSharedMe(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        fileService.deleteShareMe(id, user);
    }

    @PostMapping("delete-from-others/{id}")
    public void deleteFromOthers(@RequestBody String fileId, @PathVariable("id") String id) {
        fileService.deleteFromOthers(id, fileId);
    }

    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().build());
            return new ResponseEntity(fileService.read(filename), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.of(null);
        }
    }

    @GetMapping("/shared-files-others")
    public List<File> sharedFilesOthers(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        return fileService.sharedFilesOthers(user.getId());
    }

    @GetMapping("/shared-files-me")
    public List<File> sharedFilesMe(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        return fileService.sharedfilesMe(user.getEmail());
    }

    @PostMapping("/share/{id}")
    public File share(@RequestBody ShareRequest request, @PathVariable String id, HttpSession session){
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        String username = user.getEmail();
        return fileService.share(id, request.getUsers(), username);
    }
}
