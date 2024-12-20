package com.assetvisor.marvin.interaction.web.controllers;

import com.assetvisor.marvin.interaction.web.adapters.ForTellingHumansSseEmittingAdapter;
import jakarta.annotation.Resource;
import java.security.Principal;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin
@RestController
public class SseController {

    @Resource
    private ForTellingHumansSseEmittingAdapter sseService;

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(Principal principal) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseService.addEmitter(principal, emitter);
        return emitter;
    }
}
