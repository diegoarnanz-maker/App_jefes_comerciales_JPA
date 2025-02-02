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
import Actividad_4_ALD.modelo.entities.Cliente;
import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.services.IClienteService;
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

        @Autowired
        private IClienteService clienteService;

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
                                                .value("El comercial con ID " + comercialExistente.getIdComercial()
                                                                + " ya existe"));
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

                mockMvc.perform(delete("/api/jefecomercial/eliminar/{idcomercial}",
                                comercialSinPedidos.getIdComercial()))
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

                mockMvc.perform(delete("/api/jefecomercial/eliminar/{idcomercial}",
                                comercialConPedidos.getIdComercial()))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.error").value(
                                                "El comercial con id: " + comercialConPedidos.getIdComercial()
                                                                + " tiene pedidos asociados y no puede ser eliminado"));
        }

        @Test
        public void testListarComercialesSinPedidos() throws Exception {

                // Caso exito
                Comercial comercialSinPedidos = comercialService.create(
                                Comercial.builder()
                                                .nombre("Tomas")
                                                .apellido1("Escudero")
                                                .apellido2("Delgado")
                                                .comision(0.10)
                                                .build())
                                .get();

                Comercial comercialConPedidos = comercialService.create(
                                Comercial.builder()
                                                .nombre("Diego")
                                                .apellido1("Arnanz")
                                                .apellido2("Lozano")
                                                .comision(0.50)
                                                .build())
                                .get();

                PedidoDto pedidoPrueba = PedidoDto.builder()
                                .importe(500.00)
                                .fecha(LocalDate.now())
                                .idCliente(1)
                                .idComercial(comercialConPedidos.getIdComercial())
                                .build();

                pedidoService.createFromDto(pedidoPrueba);

                mockMvc.perform(get("/api/jefecomercial/sinpedidos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[*].idComercial").value(
                                                org.hamcrest.Matchers.hasItem(comercialSinPedidos.getIdComercial())))
                                .andExpect(jsonPath("$[*].nombre").value(org.hamcrest.Matchers.hasItem("Tomas")))
                                .andExpect(jsonPath("$[*].apellido1").value(org.hamcrest.Matchers.hasItem("Escudero")))
                                .andExpect(jsonPath("$[*].apellido2").value(org.hamcrest.Matchers.hasItem("Delgado")))
                                .andExpect(jsonPath("$[*].comision").value(org.hamcrest.Matchers.hasItem(0.10)));
        }

        @Test
        public void testListarPedidosPorComercial() throws Exception {

                Comercial comercialConPedidos = comercialService.create(
                                Comercial.builder()
                                                .nombre("Tomas")
                                                .apellido1("Escudero")
                                                .apellido2("Delgado")
                                                .comision(0.15)
                                                .build())
                                .get();

                PedidoDto pedido1 = PedidoDto.builder()
                                .importe(2000.00)
                                .fecha(LocalDate.now())
                                .idCliente(1)
                                .idComercial(comercialConPedidos.getIdComercial())
                                .build();

                PedidoDto pedido2 = PedidoDto.builder()
                                .importe(2000.00)
                                .fecha(LocalDate.now())
                                .idCliente(2)
                                .idComercial(comercialConPedidos.getIdComercial())
                                .build();

                pedidoService.createFromDto(pedido1);
                pedidoService.createFromDto(pedido2);

                mockMvc.perform(get("/api/jefecomercial/pedidos/{idComercial}", comercialConPedidos.getIdComercial()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[*].importe")
                                                .value(org.hamcrest.Matchers.hasItems(2000.00, 2000.00)))
                                .andExpect(jsonPath("$[*].idCliente").value(org.hamcrest.Matchers.hasItems(1, 2)))
                                .andExpect(jsonPath("$[*].idComercial").value(
                                                org.hamcrest.Matchers.hasItems(comercialConPedidos.getIdComercial())));

        }

        @Test
        public void testTotalFacturadoPorComercial() throws Exception {

                Comercial comercial1 = comercialService.create(
                                Comercial.builder()
                                                .nombre("Tomas")
                                                .apellido1("Escudero")
                                                .apellido2("Delgado")
                                                .comision(0.05)
                                                .build())
                                .get();

                Comercial comercial2 = comercialService.create(
                                Comercial.builder()
                                                .nombre("Diego")
                                                .apellido1("Arnanz")
                                                .apellido2("Lozano")
                                                .comision(0.10)
                                                .build())
                                .get();

                PedidoDto pedido1 = PedidoDto.builder()
                                .importe(5000.00)
                                .fecha(LocalDate.now())
                                .idCliente(1)
                                .idComercial(comercial1.getIdComercial())
                                .build();

                PedidoDto pedido2 = PedidoDto.builder()
                                .importe(4000.00)
                                .fecha(LocalDate.now())
                                .idCliente(2)
                                .idComercial(comercial1.getIdComercial())
                                .build();

                PedidoDto pedido3 = PedidoDto.builder()
                                .importe(370.00)
                                .fecha(LocalDate.now())
                                .idCliente(3)
                                .idComercial(comercial2.getIdComercial())
                                .build();

                pedidoService.createFromDto(pedido1);
                pedidoService.createFromDto(pedido2);
                pedidoService.createFromDto(pedido3);

                mockMvc.perform(get("/api/jefecomercial/totalpedidos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.['Tomas Escudero Delgado']").value(9000.00))
                                .andExpect(jsonPath("$.['Diego Arnanz Lozano']").value(370.00));
        }

        @Test
        public void testListarComercialesPorCliente() throws Exception {

                Cliente cliente = clienteService.create(
                                Cliente.builder()
                                                .nombre("Carlos")
                                                .apellido1("González")
                                                .apellido2("Ruiz")
                                                .ciudad("Madrid")
                                                .categoria(200)
                                                .build())
                                .get();

                Comercial comercial1 = comercialService.create(
                                Comercial.builder()
                                                .nombre("Tomas")
                                                .apellido1("Escudero")
                                                .apellido2("Delgado")
                                                .comision(0.5)
                                                .build())
                                .get();

                Comercial comercial2 = comercialService.create(
                                Comercial.builder()
                                                .nombre("Diego")
                                                .apellido1("Arnanz")
                                                .apellido2("Lozano")
                                                .comision(0.1)
                                                .build())
                                .get();

                PedidoDto pedido1 = PedidoDto.builder()
                                .importe(5000.00)
                                .fecha(LocalDate.now())
                                .idCliente(cliente.getIdCliente())
                                .idComercial(comercial1.getIdComercial())
                                .build();

                PedidoDto pedido2 = PedidoDto.builder()
                                .importe(4000.00)
                                .fecha(LocalDate.now())
                                .idCliente(cliente.getIdCliente())
                                .idComercial(comercial2.getIdComercial())
                                .build();

                pedidoService.createFromDto(pedido1);
                pedidoService.createFromDto(pedido2);

                // Caso exito
                mockMvc.perform(get("/api/jefecomercial/bycliente/{idCliente}", cliente.getIdCliente()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[*].nombre")
                                                .value(org.hamcrest.Matchers.hasItems("Tomas", "Diego")))
                                .andExpect(jsonPath("$[*].apellido1")
                                                .value(org.hamcrest.Matchers.hasItems("Escudero", "Arnanz")))
                                .andExpect(jsonPath("$[*].apellido2")
                                                .value(org.hamcrest.Matchers.hasItems("Delgado", "Lozano")));

                // Caso error
                mockMvc.perform(get("/api/jefecomercial/bycliente/{idCliente}", 9999))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("No se encontraron comerciales para el cliente con id 9999"));
        }
}