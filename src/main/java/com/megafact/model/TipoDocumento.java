package com.megafact.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@ToString
@Table(name = "tipo_documento")
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idTipoDocumento;

    @Size(min = 1,max = 1,message = "Codigo de tipo documento debe tener minimo 1 y maximo 1 caracter")
    @Column(name = "codTipoDocumento", nullable = true, length = 20)
    private String codTipoDocumento;

    @Column(name = "abreviatura", nullable = true, length = 20)
    private String abreviatura;

    @Column(name = "denominacion", nullable = true, length = 50)
    private String denominacion;

    //AUDT
    @Column(name = "ip_maquina", nullable = true, length = 50)
    private String ipMaquina;

    //fechaRegistro , fechaModifica

}
