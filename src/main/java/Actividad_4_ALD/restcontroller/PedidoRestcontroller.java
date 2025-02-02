package Actividad_4_ALD.restcontroller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Actividad_4_ALD.modelo.dto.PedidoDto;
import Actividad_4_ALD.modelo.entities.Pedido;
import Actividad_4_ALD.modelo.services.IPedidoService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pedido")
public class PedidoRestcontroller {

    @Autowired
    private IPedidoService pedidoService;

    @PostMapping
public ResponseEntity<?> crearPedido(@RequestBody PedidoDto pedidoDto) {
    try {
        Optional<Pedido> pedidoCreado = pedidoService.createFromDto(pedidoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", e.getMessage()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Error interno al guardar el pedido"));
    }
}



}
