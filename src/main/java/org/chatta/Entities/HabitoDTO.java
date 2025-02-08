package org.chatta.Entities;

import java.time.Instant;

public class HabitoDTO {
    private Integer idActividad;
    private String nombreActividad;
    private Integer frecuencia;
    private String tipo;
    private Instant ultimaFecha; // ✅ Se corrigió de "fecha" a "ultimaFecha"

    public HabitoDTO(Integer idActividad, String nombreActividad, Integer frecuencia, String tipo, Instant ultimaFecha) {
        this.idActividad = idActividad;
        this.nombreActividad = nombreActividad;
        this.frecuencia = frecuencia;
        this.tipo = tipo;
        this.ultimaFecha = ultimaFecha;
    }

    public Integer getIdActividad() {
        return idActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public String getTipo() {
        return tipo;
    }

    public Instant getUltimaFecha() { // ✅ Getter corregido
        return ultimaFecha;
    }
}
