package Actividad_4_ALD.modelo.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Actividad_4_ALD.modelo.dto.PedidoDto;
import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.entities.Pedido;
import Actividad_4_ALD.repository.IComercialRepository;

@Service
public class ComercialserviceImplMy8 implements IComercialService {

    @Autowired
    private IComercialRepository comercialRepository;

    @Override
    public List<Comercial> findAll() {
        try {
            return comercialRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la lista de comerciales", e);
        }
    }

    @Override
    public Optional<Comercial> create(Comercial entity) {
        if (entity == null) {
            throw new IllegalArgumentException("El comercial no puede ser nulo"); // 400
        }
        if (entity.getIdComercial() != null && comercialRepository.existsById(entity.getIdComercial())) {
            throw new IllegalStateException("El comercial con ID " + entity.getIdComercial() + " ya existe"); // 409
        }
        return Optional.of(comercialRepository.save(entity));
    }

    @Override
    public Optional<Comercial> read(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id del comercial no puede ser nulo");
            }
            return comercialRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el comercial", e);
        }
    }

    @Override
    public Comercial update(Comercial entity) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("El comercial no puede ser nulo");
            }
            if (entity.getIdComercial() == null || !comercialRepository.existsById(entity.getIdComercial())) {
                throw new IllegalArgumentException("El id del comercial no puede ser nulo o no existe en la bbdd");
            }
            return comercialRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el comercial", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id del comercial no puede ser nulo");
            }
            if (!comercialRepository.existsById(id)) {
                throw new IllegalArgumentException("El id del comercial no existe en la bbdd");
            }
            comercialRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al borrar el comercial", e);
        }
    }

    @Override
    public List<Comercial> comercialesByCliente(int idCliente) {
        try {
            if (idCliente == 0) {
                throw new IllegalArgumentException("El id del cliente no puede ser 0");
            }
            return comercialRepository.comercialesByCliente(idCliente);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la lista de comerciales por cliente", e);
        }
    }

    @Override
    public List<Comercial> comercialesSinPedidos() {
        try {
            return comercialRepository.comercialesSinPedidos();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la lista de comerciales sin pedidos", e);
        }
    }

    @Override
    public List<PedidoDto> pedidosByComercial(int idComercial) {
        if (idComercial == 0) {
            throw new IllegalArgumentException("El id del comercial no puede ser 0");
        }

        List<Pedido> pedidos = comercialRepository.pedidosByComercial(idComercial);

        return pedidos.stream()
                .map(p -> new PedidoDto(
                        p.getImporte(),
                        p.getFecha(),
                        p.getCliente().getIdCliente(),
                        p.getComercial().getIdComercial()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> totalFacturadoPorComercial() {
        List<Pedido> pedidos = comercialRepository.obtenerTodosLosPedidos();

        return pedidos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getComercial().getNombre() + " " + p.getComercial().getApellido1() +
                                (p.getComercial().getApellido2() != null ? " " + p.getComercial().getApellido2() : ""),
                        Collectors.summingDouble(Pedido::getImporte)));
    }

}
