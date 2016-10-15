package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Pru1App;
import com.mycompany.myapp.domain.Pais;
import com.mycompany.myapp.repository.PaisRepository;

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
 * Test class for the PaisResource REST controller.
 *
 * @see PaisResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Pru1App.class)
@WebAppConfiguration
@IntegrationTest
public class PaisResourceIntTest {


    private static final Long DEFAULT_PAIS_ID = 1L;
    private static final Long UPDATED_PAIS_ID = 2L;
    private static final String DEFAULT_PAIS_NOMBRE = "AAAA";
    private static final String UPDATED_PAIS_NOMBRE = "BBBB";

    @Inject
    private PaisRepository paisRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaisMockMvc;

    private Pais pais;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaisResource paisResource = new PaisResource();
        ReflectionTestUtils.setField(paisResource, "paisRepository", paisRepository);
        this.restPaisMockMvc = MockMvcBuilders.standaloneSetup(paisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pais = new Pais();
        pais.setPaisId(DEFAULT_PAIS_ID);
        pais.setPaisNombre(DEFAULT_PAIS_NOMBRE);
    }

    @Test
    @Transactional
    public void createPais() throws Exception {
        int databaseSizeBeforeCreate = paisRepository.findAll().size();

        // Create the Pais

        restPaisMockMvc.perform(post("/api/pais")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pais)))
                .andExpect(status().isCreated());

        // Validate the Pais in the database
        List<Pais> pais = paisRepository.findAll();
        assertThat(pais).hasSize(databaseSizeBeforeCreate + 1);
        Pais testPais = pais.get(pais.size() - 1);
        assertThat(testPais.getPaisId()).isEqualTo(DEFAULT_PAIS_ID);
        assertThat(testPais.getPaisNombre()).isEqualTo(DEFAULT_PAIS_NOMBRE);
    }

    @Test
    @Transactional
    public void checkPaisNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = paisRepository.findAll().size();
        // set the field null
        pais.setPaisNombre(null);

        // Create the Pais, which fails.

        restPaisMockMvc.perform(post("/api/pais")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pais)))
                .andExpect(status().isBadRequest());

        List<Pais> pais = paisRepository.findAll();
        assertThat(pais).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get all the pais
        restPaisMockMvc.perform(get("/api/pais?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
                .andExpect(jsonPath("$.[*].paisId").value(hasItem(DEFAULT_PAIS_ID.intValue())))
                .andExpect(jsonPath("$.[*].paisNombre").value(hasItem(DEFAULT_PAIS_NOMBRE.toString())));
    }

    @Test
    @Transactional
    public void getPais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);

        // Get the pais
        restPaisMockMvc.perform(get("/api/pais/{id}", pais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pais.getId().intValue()))
            .andExpect(jsonPath("$.paisId").value(DEFAULT_PAIS_ID.intValue()))
            .andExpect(jsonPath("$.paisNombre").value(DEFAULT_PAIS_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPais() throws Exception {
        // Get the pais
        restPaisMockMvc.perform(get("/api/pais/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);
        int databaseSizeBeforeUpdate = paisRepository.findAll().size();

        // Update the pais
        Pais updatedPais = new Pais();
        updatedPais.setId(pais.getId());
        updatedPais.setPaisId(UPDATED_PAIS_ID);
        updatedPais.setPaisNombre(UPDATED_PAIS_NOMBRE);

        restPaisMockMvc.perform(put("/api/pais")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPais)))
                .andExpect(status().isOk());

        // Validate the Pais in the database
        List<Pais> pais = paisRepository.findAll();
        assertThat(pais).hasSize(databaseSizeBeforeUpdate);
        Pais testPais = pais.get(pais.size() - 1);
        assertThat(testPais.getPaisId()).isEqualTo(UPDATED_PAIS_ID);
        assertThat(testPais.getPaisNombre()).isEqualTo(UPDATED_PAIS_NOMBRE);
    }

    @Test
    @Transactional
    public void deletePais() throws Exception {
        // Initialize the database
        paisRepository.saveAndFlush(pais);
        int databaseSizeBeforeDelete = paisRepository.findAll().size();

        // Get the pais
        restPaisMockMvc.perform(delete("/api/pais/{id}", pais.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pais> pais = paisRepository.findAll();
        assertThat(pais).hasSize(databaseSizeBeforeDelete - 1);
    }
}
