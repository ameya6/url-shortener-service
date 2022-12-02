package org.shortner.controller;

import lombok.extern.log4j.Log4j2;
import org.data.model.request.URLInfoRequest;
import org.data.model.response.URLInfoResponse;
import org.shortner.service.URLShortnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/shortner")
@Log4j2
public class URLShortnerController {

    @Autowired
    private URLShortnerService shortnerService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<URLInfoResponse> shortURL(@RequestBody URLInfoRequest urlInfoRequest) {
        try {
            return ResponseEntity.ok(shortnerService.shortner(urlInfoRequest));
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            return ResponseEntity.ok(shortnerService.urlInfoErrorResponse("Failed to generate short url"));
        }
    }
}
