package com.hanaset.luke.web.rest;

import com.hanaset.luke.service.LukeFileService;
import com.hanaset.luke.web.rest.support.LukeApiRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class LukeFileRest extends LukeApiRestSupport {

    private final LukeFileService lukeFileService;

    public LukeFileRest(LukeFileService lukeFileService) {
        this.lukeFileService = lukeFileService;
    }

    @PostMapping()
    public ResponseEntity uploadCSVFile(@RequestParam("file") MultipartFile file) {

        return success(lukeFileService.uploadFileData(file));
    }
}
