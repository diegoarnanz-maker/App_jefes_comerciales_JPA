package Actividad_4_ALD.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.entities.Pedido;

public interface IComercialRepository extends JpaRepository<Comercial, Integer> {

    // Devolver la lista de los comerciales que han atendido pedidos del cliente que
    // coincida con ese id.
    @Query("SELECT DISTINCT p.comercial FROM Pedido p WHERE p.cliente.idCliente = :idCliente")
    List<Comercial> comercialesByCliente(@Param("idCliente") int idCliente);

    // Devolver la lista de comerciales que no han atendido ningún pedido
    @Query("SELECT c FROM Comercial c WHERE c.idComercial NOT IN (SELECT DISTINCT p.comercial.idComercial FROM Pedido p)")
    List<Comercial> comercialesSinPedidos();

    // Devolver la lista de pedidos gestionados por el comercial que coincida con
    // ese id.
    @Query("SELECT p FROM Pedido p WHERE p.comercial.idComercial = :idComercial")
    List<Pedido> pedidosByComercial(@Param("idComercial") int idComercial);

    // Obtener la suma de los importes de los pedidos gestionados por cada
    // comercial.
    @Query("SELECT CONCAT(p.comercial.nombre, ' ', p.comercial.apellido1, ' ', COALESCE(p.comercial.apellido2, '')), SUM(p.importe) "
            +
            "FROM Pedido p GROUP BY p.comercial.idComercial, p.comercial.nombre, p.comercial.apellido1, p.comercial.apellido2")
    List<Object[]> totalFacturadoPorComercial();

    @Query("SELECT p FROM Pedido p")
    List<Pedido> obtenerTodosLosPedidos();

}
