package com.mcddhub.demo02.controller;

import com.alibaba.cloud.ai.dashscope.audio.speech.AudioSpeechModels;
import com.alibaba.cloud.ai.dashscope.audio.transcription.AudioTranscriptionModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo02/audios")
public class AudioModelController {

    private static final Logger logger = LoggerFactory.getLogger(AudioModelController.class);

    @GetMapping("/speech")
    public String speech(String text) {

        logger.warn("unimplemented");

        return "unimplemented";
    }

    @GetMapping("/transcription")
    public String transcription(String text) {

        logger.warn("unimplemented");

        return "unimplemented";
    }
}
