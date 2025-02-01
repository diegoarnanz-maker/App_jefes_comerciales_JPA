package Actividad_4_ALD.modelo.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comerciales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comercial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comercial")
    private Integer idComercial;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private Double comision;

}
