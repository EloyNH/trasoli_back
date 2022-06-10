package com.megafact.dto;

import com.megafact.model.Persona;
import com.megafact.model.RepresentanteLegal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RepresentanteLegalDTO {
    private Persona persona;
    private RepresentanteLegal representanteLegal;
}
