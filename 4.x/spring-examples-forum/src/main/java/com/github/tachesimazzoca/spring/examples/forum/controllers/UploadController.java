package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.helpers.FileHelper;
import com.github.tachesimazzoca.spring.examples.forum.helpers.TempFileHelper;
import com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/upload")
public class UploadController extends AbstractUserController {
    private static final Map<String, List<String>> SUPPORTED_TYPES =
            ParameterUtils.<String, List<String>>map(
                    "image/jpeg", ParameterUtils.list("jpg", "jpeg"),
                    "image/gif", ParameterUtils.list("gif"),
                    "image/png", ParameterUtils.list("png"));

    @Autowired
    TempFileHelper tempFileHelper;

    @Autowired
    @Qualifier("profileIconHelper")
    FileHelper profileIconHelper;

    private Optional<String> detectContentType(String filename) {
        String ext = FilenameUtils.getExtension(filename);
        for (Map.Entry<String, List<String>> format : SUPPORTED_TYPES.entrySet()) {
            if (format.getValue().contains(ext)) {
                return Optional.of(format.getKey());
            }
        }
        return Optional.empty();
    }

    @RequestMapping(value = "/temp/{name:.+}", method = RequestMethod.GET)
    public ResponseEntity<?> temp(@PathVariable("name") String name)
            throws IOException {
        String contentType = detectContentType(name).orElse(null);
        if (null == contentType)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        File tempFile = tempFileHelper.read(name).orElse(null);
        if (null == tempFile)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        FileSystemResource resource = new FileSystemResource(tempFile);
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(resource.getInputStream()));
    }

    @RequestMapping(value = "/temp", method = RequestMethod.POST)
    public ResponseEntity<?> postTemp(@RequestParam("file") MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (!SUPPORTED_TYPES.containsKey(contentType))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        String suffix = SUPPORTED_TYPES.get(contentType).get(0);
        File tempFile = tempFileHelper.create(file.getInputStream(), "tmp-", "." + suffix);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(tempFile.getName());
    }

    @RequestMapping(value = "/profile/icon/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> profileIcon(@PathVariable("id") String id)
            throws IOException {

        FileHelper.Result result = profileIconHelper.find(id).orElse(null);
        if (null == result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        FileSystemResource resource = new FileSystemResource(result.getFile());
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType(result.getMimeType()))
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
