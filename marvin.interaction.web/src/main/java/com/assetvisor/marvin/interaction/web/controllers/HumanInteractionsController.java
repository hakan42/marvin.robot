package com.assetvisor.marvin.interaction.web.controllers;

import com.assetvisor.marvin.interaction.web.AudioBuffer;
import com.assetvisor.marvin.robot.application.ListenUseCase;
import com.assetvisor.marvin.robot.domain.communication.Message;
import jakarta.annotation.Resource;
import java.security.Principal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HumanInteractionsController {

    @Resource
    private ListenUseCase listenUseCase;
    @Resource
    private AudioBuffer audioBuffer;

    @PostMapping("/message")
    public void message(Principal principal, @RequestParam(value = "message") String message) {
        listenUseCase.listenTo(new Message("User", message));
    }

    @PostMapping(path = "/speech", consumes = "application/octet-stream")
    public void speech(Principal principal, @RequestBody byte[] message) {
        listenUseCase.listenTo("User", message);
    }

    @GetMapping("/speech")
    public ResponseEntity<byte[]> speech(Principal principal) {
        byte[] audioData = audioBuffer.get();

        if (audioData == null || audioData.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=chat.mp3");
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");

        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }
}
