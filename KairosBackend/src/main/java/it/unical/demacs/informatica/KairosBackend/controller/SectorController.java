package it.unical.demacs.informatica.KairosBackend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/structures", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class SectorController
{
    // ...
}
