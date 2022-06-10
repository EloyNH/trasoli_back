package com.megafact.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresa;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(name = "nombre_comercial", nullable = false, length = 100)
    private String nombreComercial;

    @Size(min = 11,max = 11,message = "el numero de ruc db ser minimo 11 y maximo 11")
    @Column(name = "numero_ruc", nullable = false, length = 11, unique = true)
    private String numeroRuc;

    @Column(name = "actividad", nullable = false, length = 300)
    private String actividad;

    @Column(name = "zonificacion", nullable = true, length = 100)
    private String zonificacion;

    @OneToOne
    @JoinColumn(name = "id_representante_legal", nullable = false)
    private RepresentanteLegal representanteLegal;

    @ManyToOne
    @JoinColumn(name = "id_giro_negocio", nullable = false)
    private GiroNegocio giroNegocio;

    //audit
    @Column(name = "ip_maquina", nullable = true, length = 50)
    private String ipMaquina;

    @Column(name = "idRegUsuaRegistra", nullable = true, length = 11)
    private int idRegUsuaRegistra;

    @Column(name = "idRegUsuaModifica", nullable = true, length = 11)
    private int idRegUsuaModifica;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime fechaRegistra;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime fechaModifica;

    @Column(name = "cantidadModifica", nullable = true)
    private int cantidadModifica;

}
