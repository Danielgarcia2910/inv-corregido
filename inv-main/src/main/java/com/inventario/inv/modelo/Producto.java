package com.inventario.inv.modelo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Producto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "[A-Za-z0-9\\-]{3,20}",
             message = "Código con formato no válido (letras, números y guiones)")
    private String codigo;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 100000, message = "El stock no puede pasar de 100000")
    private Integer stock;

    private boolean disponible = true;

    // Nombre del archivo de la portada (paso 17). null = sin portada.
    private String portada;

    public Producto() {
    }

    public Producto(Long id, String nombre, String codigo, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.stock = stock;
    }

    public Long getId() {
         return id; }

    public void setId(Long id) {
         this.id = id; }

    public String getNombre() {
         return nombre; }
    public void setNombre(String nombre) {
         this.nombre = nombre; }

    public String getCodigo() {
         return codigo; }
    public void setCodigo(String codigo) { 
        this.codigo = codigo; }

    public Integer getStock(){
         return stock; }
    public void setStock(Integer stock) {
         this.stock = stock; }

    public boolean isDisponible(){
         return disponible; }

    public void setDisponible(boolean disponible) {
         this.disponible = disponible; }

    public String getPortada() {
         return portada; }

    public void setPortada(String portada){
         this.portada = portada; }
}
