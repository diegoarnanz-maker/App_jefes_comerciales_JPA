package Actividad_4_ALD.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import Actividad_4_ALD.modelo.services.IComercialService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ComercialRestcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IComercialService comercialService;

    @Test
    public void testListarComerciales() throws Exception {

        // Caso exito
        mockMvc.perform(get("/api/jefecomercial"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].idComercial").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Daniel"))
            .andExpect(jsonPath("$[0].apellido1").value("Sáez"))
            .andExpect(jsonPath("$[0].apellido2").value("Vega"))
            .andExpect(jsonPath("$[0].comision").value(0.15));
    }

}
