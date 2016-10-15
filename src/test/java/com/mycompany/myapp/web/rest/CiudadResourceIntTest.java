package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Pru1App;
import com.mycompany.myapp.domain.Ciudad;
import com.mycompany.myapp.repository.CiudadRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CiudadResource REST controller.
 *
 * @see CiudadResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Pru1App.class)
@WebAppConfiguration
@IntegrationTest
public class CiudadResourceIntTest {


    private static final Long DEFAULT_COD_CIUDAD = 1L;
    private static final Long UPDATED_COD_CIUDAD = 2L;
    private static final String DEFAULT_NOMBRE = "AAA";
    private static final String UPDATED_NOMBRE = "BBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Integer DEFAULT_ANO = 1;
    private static final Integer UPDATED_ANO = 2;

    @Inject
    private CiudadRepository ciudadRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCiudadMockMvc;

    private Ciudad ciudad;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CiudadResource ciudadResource = new CiudadResource();
        ReflectionTestUtils.setField(ciudadResource, "ciudadRepository", ciudadRepository);
        this.restCiudadMockMvc = MockMvcBuilders.standaloneSetup(ciudadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ciudad = new Ciudad();
        ciudad.setCodCiudad(DEFAULT_COD_CIUDAD);
        ciudad.setNombre(DEFAULT_NOMBRE);
        ciudad.setCantidad(DEFAULT_CANTIDAD);
        ciudad.setAno(DEFAULT_ANO);
    }

    @Test
    @Transactional
    public void createCiudad() throws Exception {
        int databaseSizeBeforeCreate = ciudadRepository.findAll().size();

        // Create the Ciudad

        restCiudadMockMvc.perform(post("/api/ciudads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ciudad)))
                .andExpect(status().isCreated());

        // Validate the Ciudad in the database
        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeCreate + 1);
        Ciudad testCiudad = ciudads.get(ciudads.size() - 1);
        assertThat(testCiudad.getCodCiudad()).isEqualTo(DEFAULT_COD_CIUDAD);
        assertThat(testCiudad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCiudad.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testCiudad.getAno()).isEqualTo(DEFAULT_ANO);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = ciudadRepository.findAll().size();
        // set the field null
        ciudad.setNombre(null);

        // Create the Ciudad, which fails.

        restCiudadMockMvc.perform(post("/api/ciudads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ciudad)))
                .andExpect(status().isBadRequest());

        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCiudads() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);

        // Get all the ciudads
        restCiudadMockMvc.perform(get("/api/ciudads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ciudad.getId().intValue())))
                .andExpect(jsonPath("$.[*].codCiudad").value(hasItem(DEFAULT_COD_CIUDAD.intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
                .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)));
    }

    @Test
    @Transactional
    public void getCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);

        // Get the ciudad
        restCiudadMockMvc.perform(get("/api/ciudads/{id}", ciudad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ciudad.getId().intValue()))
            .andExpect(jsonPath("$.codCiudad").value(DEFAULT_COD_CIUDAD.intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO));
    }

    @Test
    @Transactional
    public void getNonExistingCiudad() throws Exception {
        // Get the ciudad
        restCiudadMockMvc.perform(get("/api/ciudads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);
        int databaseSizeBeforeUpdate = ciudadRepository.findAll().size();

        // Update the ciudad
        Ciudad updatedCiudad = new Ciudad();
        updatedCiudad.setId(ciudad.getId());
        updatedCiudad.setCodCiudad(UPDATED_COD_CIUDAD);
        updatedCiudad.setNombre(UPDATED_NOMBRE);
        updatedCiudad.setCantidad(UPDATED_CANTIDAD);
        updatedCiudad.setAno(UPDATED_ANO);

        restCiudadMockMvc.perform(put("/api/ciudads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCiudad)))
                .andExpect(status().isOk());

        // Validate the Ciudad in the database
        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeUpdate);
        Ciudad testCiudad = ciudads.get(ciudads.size() - 1);
        assertThat(testCiudad.getCodCiudad()).isEqualTo(UPDATED_COD_CIUDAD);
        assertThat(testCiudad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCiudad.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testCiudad.getAno()).isEqualTo(UPDATED_ANO);
    }

    @Test
    @Transactional
    public void deleteCiudad() throws Exception {
        // Initialize the database
        ciudadRepository.saveAndFlush(ciudad);
        int databaseSizeBeforeDelete = ciudadRepository.findAll().size();

        // Get the ciudad
        restCiudadMockMvc.perform(delete("/api/ciudads/{id}", ciudad.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ciudad> ciudads = ciudadRepository.findAll();
        assertThat(ciudads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
