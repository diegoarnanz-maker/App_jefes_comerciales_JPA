package Actividad_4_ALD.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Actividad_4_ALD.modelo.entities.Cliente;

public interface IClienteRepository extends JpaRepository<Cliente, Integer> {

}
