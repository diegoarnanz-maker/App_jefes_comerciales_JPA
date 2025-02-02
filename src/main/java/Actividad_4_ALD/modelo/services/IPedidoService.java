package Actividad_4_ALD.modelo.services;

import java.util.Optional;

import Actividad_4_ALD.modelo.dto.PedidoDto;
import Actividad_4_ALD.modelo.entities.Pedido;

public interface IPedidoService extends IGenericoCRUD<Pedido, Integer> {

    Optional<Pedido> createFromDto(PedidoDto dto);
}
