package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Organization;
import com.prodyna.conference.backend.model.OrganizationDTO;
import com.prodyna.conference.backend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping
    public List<Organization> getOrganizations() {
        return organizationService.getOrganizations();
    }

    @GetMapping("{id}")
    public Organization getOrganization(@PathVariable String id) {
        return organizationService.getOrganization(id);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public Organization addOrganization(@Valid @RequestBody OrganizationDTO organizationDTO, Errors errors) {
        validate(organizationDTO, errors);
        return organizationService.addOrganization(organizationDTO);
    }

    @PutMapping("{id}")
    @Secured("ROLE_USER")
    public Organization updateOrganization(@PathVariable String id, @Valid @RequestBody OrganizationDTO organizationDTO, Errors errors) {
        validate(organizationDTO, errors);
        return organizationService.updateOrganization(id, organizationDTO);
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_USER")
    public void deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
    }

    private void validate(OrganizationDTO organizationDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
