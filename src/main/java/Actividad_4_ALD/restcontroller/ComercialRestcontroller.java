package Actividad_4_ALD.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
