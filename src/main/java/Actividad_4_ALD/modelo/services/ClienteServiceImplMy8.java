package Actividad_4_ALD.modelo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Actividad_4_ALD.modelo.entities.Cliente;
import Actividad_4_ALD.repository.IClienteRepository;

@Service
public class ClienteServiceImplMy8 implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public List<Cliente> findAll() {
        return null;
    }

    @Override
    public Optional<Cliente> create(Cliente entity) {
        try{
            if (entity == null) {
                throw new IllegalArgumentException("El cliente no puede ser nulo"); // 400
            }
            if (entity.getIdCliente() != null && clienteRepository.existsById(entity.getIdCliente())) {
                throw new IllegalStateException("El cliente con ID " + entity.getIdCliente() + " ya existe"); // 409
            }
            return Optional.of(clienteRepository.save(entity));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el cliente", e);
        }
    }

    @Override
    public Optional<Cliente> read(Integer id) {
        return null;
    }

    @Override
    public Cliente update(Cliente entity) {
        return null;
    }

    @Override
    public void delete(Integer id) {
    }

}
