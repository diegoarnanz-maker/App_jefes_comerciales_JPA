package Actividad_4_ALD.modelo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String ciudad;
    private Integer categoria;

}
