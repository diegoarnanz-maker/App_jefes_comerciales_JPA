package Actividad_4_ALD.modelo.dto;

import java.time.LocalDate;

import Actividad_4_ALD.modelo.entities.Cliente;
import Actividad_4_ALD.modelo.entities.Comercial;
import Actividad_4_ALD.modelo.entities.Pedido;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDto {
    private Double importe;
    private LocalDate fecha;
    private Integer idCliente;
    private Integer idComercial;

    //al ser una conversion sencilla no se ha creado un ModelMapper
    public Pedido convertToPedido(Cliente cliente, Comercial comercial) {
        return Pedido.builder()
                .importe(this.importe)
                .fecha(this.fecha != null ? this.fecha : LocalDate.now())
                .cliente(cliente)
                .comercial(comercial)
                .build();
    }
}
