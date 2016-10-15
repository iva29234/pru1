package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Ciudad;
import com.mycompany.myapp.repository.CiudadRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ciudad.
 */
@RestController
@RequestMapping("/api")
public class CiudadResource {

    private final Logger log = LoggerFactory.getLogger(CiudadResource.class);
        
    @Inject
    private CiudadRepository ciudadRepository;
    
    /**
     * POST  /ciudads : Create a new ciudad.
     *
     * @param ciudad the ciudad to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ciudad, or with status 400 (Bad Request) if the ciudad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> createCiudad(@Valid @RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to save Ciudad : {}", ciudad);
        if (ciudad.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ciudad", "idexists", "A new ciudad cannot already have an ID")).body(null);
        }
        Ciudad result = ciudadRepository.save(ciudad);
        return ResponseEntity.created(new URI("/api/ciudads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ciudad", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ciudads : Updates an existing ciudad.
     *
     * @param ciudad the ciudad to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ciudad,
     * or with status 400 (Bad Request) if the ciudad is not valid,
     * or with status 500 (Internal Server Error) if the ciudad couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> updateCiudad(@Valid @RequestBody Ciudad ciudad) throws URISyntaxException {
        log.debug("REST request to update Ciudad : {}", ciudad);
        if (ciudad.getId() == null) {
            return createCiudad(ciudad);
        }
        Ciudad result = ciudadRepository.save(ciudad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ciudad", ciudad.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ciudads : get all the ciudads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ciudads in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ciudads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ciudad>> getAllCiudads(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ciudads");
        Page<Ciudad> page = ciudadRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ciudads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ciudads/:id : get the "id" ciudad.
     *
     * @param id the id of the ciudad to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ciudad, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ciudads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ciudad> getCiudad(@PathVariable Long id) {
        log.debug("REST request to get Ciudad : {}", id);
        Ciudad ciudad = ciudadRepository.findOne(id);
        return Optional.ofNullable(ciudad)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ciudads/:id : delete the "id" ciudad.
     *
     * @param id the id of the ciudad to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ciudads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCiudad(@PathVariable Long id) {
        log.debug("REST request to delete Ciudad : {}", id);
        ciudadRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ciudad", id.toString())).build();
    }

}
