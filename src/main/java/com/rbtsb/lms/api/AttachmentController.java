package com.rbtsb.lms.api;

import com.rbtsb.lms.dto.AttachmentDTO;
import com.rbtsb.lms.service.AttachmentService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/api/attachment")
@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    private Logger log = LoggerFactory.getLogger(AttachmentController.class);

    @PostMapping("/post")
    public ResponseEntity<?> insertAttachment(@RequestBody @Valid @NonNull AttachmentDTO attachmentDTO){
        return new ResponseEntity<>(attachmentService.insertAttachment(attachmentDTO), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllAttachment(){
        return new ResponseEntity<>(attachmentService.getAllAttachment(), HttpStatus.OK);
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<?> updateAttachmentById(@PathVariable("id") String id,
                                                  @RequestBody @Valid @NonNull AttachmentDTO attachmentDTO){
        return new ResponseEntity<>(attachmentService.updateAttachmentById(id, attachmentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAttachmentById(@PathVariable("id") String id){
        return new ResponseEntity<>(attachmentService.deleteAttachmentById(id), HttpStatus.OK);
    }

    @PostMapping("/post/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file,
                                         @RequestBody @Valid @NonNull AttachmentDTO attachmentDTO) throws IOException {
        String uploadImage = attachmentService.uploadFile(file, attachmentDTO);
        return new ResponseEntity<>(uploadImage, HttpStatus.OK);
    }

    @PostMapping("/post/download")
    public ResponseEntity<?> downloadImage(
            @RequestParam("fileName") String fileName){
        byte[] data = attachmentService.downloadFile(fileName);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                .body(data);
    }
}
