package org.shortner.controller;

import lombok.extern.log4j.Log4j2;
import org.shortner.service.URLAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
@Log4j2
public class URLAccessController {

    @Autowired
    private URLAccessService accessService;

    @GetMapping(value = "/{shortCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView shortURL(@PathVariable String shortCode) {
        RedirectView redirectView = new RedirectView();
        try {
            redirectView.setUrl(accessService.redirect(shortCode));
            return redirectView;
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            return redirectView;
        }
    }
}
