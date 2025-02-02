package Actividad_4_ALD.modelo.services;

import java.util.List;
import java.util.Map;

import Actividad_4_ALD.modelo.dto.PedidoDto;
import Actividad_4_ALD.modelo.entities.Comercial;

public interface IComercialService extends IGenericoCRUD<Comercial, Integer> {

    List<Comercial> comercialesByCliente(int idCliente);
    List<Comercial> comercialesSinPedidos();
    List<PedidoDto> pedidosByComercial(int idComercial);
    //Si se quiere obtener el total facturado por un comercial
    // Map<String, Double> totalFacturadoByComercial(int idComercial);

    Map<String, Double> totalFacturadoPorComercial();
}
