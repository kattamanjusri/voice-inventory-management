package com.voiceinventory.backend.controller;

import com.voiceinventory.backend.dto.VoiceRequest;
import com.voiceinventory.backend.dto.VoiceResponse;
import com.voiceinventory.backend.service.VoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voice")
public class VoiceController {

    private final VoiceService voiceService;

    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    @PostMapping("/process")
    public ResponseEntity<VoiceResponse> processVoice(
            @RequestBody VoiceRequest request) {

        VoiceResponse response =
                voiceService.processVoice(request.getText());

        return ResponseEntity.ok(response);
    }
}
