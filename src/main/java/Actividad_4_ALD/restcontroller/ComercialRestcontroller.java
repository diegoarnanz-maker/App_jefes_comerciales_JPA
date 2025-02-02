package Actividad_4_ALD.restcontroller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.services.IComercialService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/jefecomercial")
public class ComercialRestcontroller {

    @Autowired
    private IComercialService comercialService;

    @GetMapping
    public ResponseEntity<?> listarComerciales() {
        comercialService.findAll();
        return ResponseEntity.status(200).body(comercialService.findAll());
    }

    @GetMapping("/uno/{idComercial}")
    public ResponseEntity<?> buscarComercialPorId(@PathVariable Integer idComercial) {
        Optional<?> comercial = comercialService.read(idComercial);
        if (comercial.isPresent()) {
            return ResponseEntity.status(200).body(comercial);
        } else {
            return ResponseEntity.status(404)
                    .body(Collections.singletonMap("error", "No se ha encontrado el comercial con id " + idComercial));
        }
    }

    // @GetMapping("/bycliente/{idCliente}")
    // @GetMapping("/sinpedidos")
    // @GetMapping("/pedidos/{idComercial}")
    // @GetMapping("/totalpedidos")

    @PostMapping("/alta")
    public ResponseEntity<?> crearComercial(@RequestBody Comercial comercial) {
        try {
            Optional<Comercial> comercialCreado = comercialService.create(comercial);
            return ResponseEntity.status(HttpStatus.CREATED).body(comercialCreado);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error interno al guardar el comercial"));
        }
    }

    // @PutMapping("/{idComercial}")

    @DeleteMapping("/eliminar/{idComercial}")
    public ResponseEntity<?> eliminarComercial(@PathVariable Integer idComercial) {
        try {
            // Esta Presente?
            Optional<Comercial> comercial = comercialService.read(idComercial);
            if (comercial.isEmpty()) {
                return ResponseEntity.status(404).body(
                        Collections.singletonMap("error", "No se ha encontrado el comercial con id " + idComercial));
            }
            // Tiene pedidos?
            List<Comercial> comercialesSinPedidos = comercialService.comercialesSinPedidos();
            if (comercialesSinPedidos.contains(comercial.get())) {
                comercialService.delete(idComercial);
                return ResponseEntity.status(200)
                        .body(Collections.singletonMap("success", "Comercial eliminado correctamente"));
            } else {
                return ResponseEntity.status(409).body(Collections.singletonMap("error",
                        "El comercial con id: " + idComercial + " tiene pedidos asociados y no puede ser eliminado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

}
