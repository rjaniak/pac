package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Organization;
import com.prodyna.conference.backend.model.OrganizationDTO;
import com.prodyna.conference.backend.repository.OrganizationRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Service
@Transactional
@Timed
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public Organization addOrganization(OrganizationDTO organizationDTO) {
        Organization organization = new Organization();
        organization.setName(organizationDTO.getName());
        return organizationRepository.save(organization);
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    public Organization getOrganization(Long id) {
        Optional<Organization> organization = organizationRepository.findById(id);
        if (!organization.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Organization found with id [" + id + "].");
        }
        return organization.get();
    }

    public List<Organization> getOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        Iterable<Organization> allOrganizations = organizationRepository.findAll();
        allOrganizations.forEach(organizations::add);
        return organizations;
    }

    public Organization updateOrganization(Long id, OrganizationDTO organizationDTO) {
        Optional<Organization> organizationOptional = organizationRepository.findById(id);
        if (!organizationOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Organization found with id [" + id + "].");
        }
        Organization organization = organizationOptional.get();
        organization.setName(organizationDTO.getName());
        return organizationRepository.save(organization);
    }
}
