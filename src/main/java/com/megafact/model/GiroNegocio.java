package com.megafact.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "giro_negocio")
public class GiroNegocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGiroNegocio;

    @Column(name = "abrivatura", nullable = false, length = 20)
    private String abrivatura;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Size(min = 4,max = 4, message = "el codCiiu debe ser Min 4 y Max 4")
    @Column(name = "cod_ciiu", nullable = false, length = 4)
    private String codCiiu;


}
