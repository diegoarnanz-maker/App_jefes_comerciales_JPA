package Actividad_4_ALD.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import Actividad_4_ALD.modelo.entities.Pedido;

public interface IPedidoRepository extends JpaRepository<Pedido, Integer> {
}
