package org.chatta.Entities;

import java.math.BigDecimal;
import java.time.Instant;

public class HuellaDTO {
    private Integer id;
    private String nombreActividad;
    private BigDecimal valor;
    private String unidad;
    private Instant fecha; // ✅ Se cambió de "ultimaFecha" a "fecha" para diferenciarlo de HabitoDTO

    public HuellaDTO(Integer id, String nombreActividad, BigDecimal valor, String unidad, Instant fecha) {
        this.id = id;
        this.nombreActividad = nombreActividad;
        this.valor = valor;
        this.unidad = unidad;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public Instant getFecha() {
        return fecha;
    }
}
