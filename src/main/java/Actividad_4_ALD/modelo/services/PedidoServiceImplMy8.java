package Actividad_4_ALD.modelo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Actividad_4_ALD.modelo.dto.PedidoDto;
import Actividad_4_ALD.modelo.entities.Cliente;
import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.entities.Pedido;
import Actividad_4_ALD.repository.IClienteRepository;
import Actividad_4_ALD.repository.IComercialRepository;
import Actividad_4_ALD.repository.IPedidoRepository;

@Service
public class PedidoServiceImplMy8 implements IPedidoService {

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private IComercialRepository comercialRepository;

    @Override
    public List<Pedido> findAll() {
        try {
            return pedidoRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la lista de pedidos", e);
        }
    }

    // Fallo al usar la IGenericoCRUD pues usa un create de Pedido y quiero usar un
    // create de PedidoDto
    @Override
    public Optional<Pedido> create(Pedido entity) {
        if (entity == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo"); // 400 BAD REQUEST
        }
        if (entity.getIdPedido() != null && pedidoRepository.existsById(entity.getIdPedido())) {
            throw new IllegalStateException("El pedido con ID " + entity.getIdPedido() + " ya existe"); // 409 CONFLICT
        }
        return Optional.of(pedidoRepository.save(entity));
    }

    // creo otro metodo en la interfaz para poder usar el create de PedidoDto
    @Override
    public Optional<Pedido> createFromDto(PedidoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }
        if (dto.getIdCliente() == null || dto.getIdComercial() == null) {
            throw new IllegalArgumentException("El pedido debe tener un cliente y un comercial");
        }

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("No existe un cliente con id " + dto.getIdCliente()));

        Comercial comercial = comercialRepository.findById(dto.getIdComercial())
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe un comercial con id " + dto.getIdComercial()));

        Pedido pedido = dto.convertToPedido(cliente, comercial);

        return create(pedido);
    }

    @Override
    public Optional<Pedido> read(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id del pedido no puede ser nulo");
            }
            return pedidoRepository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener el pedido", e);
        }
    }

    @Override
    public Pedido update(Pedido entity) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("El pedido no puede ser nulo");
            }
            if (entity.getIdPedido() == null || !pedidoRepository.existsById(entity.getIdPedido())) {
                throw new IllegalArgumentException(
                        "El id del pedido no puede ser nulo o no existe en la base de datos");
            }
            return pedidoRepository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el pedido", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("El id del pedido no puede ser nulo");
            }
            if (!pedidoRepository.existsById(id)) {
                throw new IllegalArgumentException("El id del pedido no existe en la base de datos");
            }
            pedidoRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al borrar el pedido", e);
        }
    }

}
