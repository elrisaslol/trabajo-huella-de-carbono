package org.chatta.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @Column(name = "id_categoria", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "factor_emision", nullable = false, precision = 10, scale = 3)
    private BigDecimal factorEmision;

    @Column(name = "unidad", nullable = false, length = 10)
    private String unidad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getFactorEmision() {
        return factorEmision;
    }

    public void setFactorEmision(BigDecimal factorEmision) {
        this.factorEmision = factorEmision;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

}