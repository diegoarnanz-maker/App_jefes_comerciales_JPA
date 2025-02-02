package Actividad_4_ALD.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import Actividad_4_ALD.modelo.dto.PedidoDto;
import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.services.IComercialService;
import Actividad_4_ALD.modelo.services.IPedidoService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ComercialRestcontrollerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IComercialService comercialService;

    @Autowired
    private IPedidoService pedidoService;

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

    @Test
    public void testBuscarComercialPorId() throws Exception {

        // Caso exito
        mockMvc.perform(get("/api/jefecomercial/uno/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idComercial").value(1))
                .andExpect(jsonPath("$.nombre").value("Daniel"))
                .andExpect(jsonPath("$.apellido1").value("Sáez"))
                .andExpect(jsonPath("$.apellido2").value("Vega"))
                .andExpect(jsonPath("$.comision").value(0.15));

        // Caso error
        mockMvc.perform(get("/api/jefecomercial/uno/1000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se ha encontrado el comercial con id 1000"));
    }

    @Test
    public void testCrearComercial() throws Exception {

        Comercial newComercial = Comercial.builder()
                .nombre("Tomas")
                .apellido1("Escudero")
                .apellido2("Delgado")
                .comision(0.10)
                .build();

        comercialService.create(newComercial);

        // Caso exito
        mockMvc.perform(post("/api/jefecomercial/alta")
                .contentType("application/json")
                .content(
                        "{ \"nombre\": \"Tomas\", \"apellido1\": \"Escudero\", \"apellido2\": \"Delgado\", \"comision\": 0.10 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Tomas"))
                .andExpect(jsonPath("$.apellido1").value("Escudero"))
                .andExpect(jsonPath("$.apellido2").value("Delgado"))
                .andExpect(jsonPath("$.comision").value(0.10));

        // Caso error
        Comercial comercialExistente = comercialService.create(
                Comercial.builder()
                        .nombre("Pepito")
                        .apellido1("Pepon")
                        .apellido2("Pepon")
                        .comision(0.12)
                        .build())
                .get();

        mockMvc.perform(post("/api/jefecomercial/alta")
                .contentType("application/json")
                .content("{ \"idComercial\": " + comercialExistente.getIdComercial() +
                        ", \"nombre\": \"Pedro\", \"apellido1\": \"Gómez\", \"apellido2\": \"Fernández\", \"comision\": 0.12 }"))
                .andExpect(status().isConflict()) // 409
                .andExpect(jsonPath("$.error")
                        .value("El comercial con ID " + comercialExistente.getIdComercial() + " ya existe"));
    }

    @Test
    public void testEliminarcomercial() throws Exception {

        // Caso exito
        Comercial comercialSinPedidos = comercialService.create(
                Comercial.builder()
                        .nombre("Tomas")
                        .apellido1("Escudero")
                        .apellido2("Delgado")
                        .comision(0.10)
                        .build())
                .get();

        mockMvc.perform(delete("/api/jefecomercial/eliminar/{idcomercial}", comercialSinPedidos.getIdComercial()))
                .andExpect(status().isOk());

        // Caso error (comercial con pedidos)
        Comercial comercialConPedidos = comercialService.create(
                Comercial.builder()
                        .nombre("Tomas")
                        .apellido1("Escudero")
                        .apellido2("Delgado")
                        .comision(0.10)
                        .build())
                .get();
                
        PedidoDto pedidoPrueba = PedidoDto.builder()
                .importe(500.00)
                .fecha(LocalDate.now())
                .idCliente(1)
                .idComercial(comercialConPedidos.getIdComercial())
                .build();

        pedidoService.createFromDto(pedidoPrueba);

        mockMvc.perform(delete("/api/jefecomercial/eliminar/{idcomercial}", comercialConPedidos.getIdComercial()))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error").value(
                "El comercial con id: " + comercialConPedidos.getIdComercial() + " tiene pedidos asociados y no puede ser eliminado"));
}

}
