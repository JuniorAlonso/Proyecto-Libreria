package com.proyecto.Libreria.entidad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase POJO para mapear la respuesta JSON de API Peru de consulta de DNI.
 * La respuesta viene en formato: {"success":true,"data":{...}}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DniResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("data")
    private DniData data;

    // Clase interna para mapear el objeto "data"
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DniData {
        @JsonProperty("numero")
        private String numero;

        @JsonProperty("nombre_completo")
        private String nombreCompleto;

        @JsonProperty("nombres")
        private String nombres;

        @JsonProperty("apellido_paterno")
        private String apellidoPaterno;

        @JsonProperty("apellido_materno")
        private String apellidoMaterno;

        @JsonProperty("codigo_verificacion")
        private Integer codigoVerificacion;

        @JsonProperty("direccion")
        private String direccion;

        @JsonProperty("direccion_completa")
        private String direccionCompleta;

        @JsonProperty("ubigeo_reniec")
        private String ubigeoReniec;

        @JsonProperty("ubigeo_sunat")
        private String ubigeoSunat;

        // Getters y Setters
        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public void setNombreCompleto(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        public String getNombres() {
            return nombres;
        }

        public void setNombres(String nombres) {
            this.nombres = nombres;
        }

        public String getApellidoPaterno() {
            return apellidoPaterno;
        }

        public void setApellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
        }

        public String getApellidoMaterno() {
            return apellidoMaterno;
        }

        public void setApellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
        }

        public Integer getCodigoVerificacion() {
            return codigoVerificacion;
        }

        public void setCodigoVerificacion(Integer codigoVerificacion) {
            this.codigoVerificacion = codigoVerificacion;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getDireccionCompleta() {
            return direccionCompleta;
        }

        public void setDireccionCompleta(String direccionCompleta) {
            this.direccionCompleta = direccionCompleta;
        }

        public String getUbigeoReniec() {
            return ubigeoReniec;
        }

        public void setUbigeoReniec(String ubigeoReniec) {
            this.ubigeoReniec = ubigeoReniec;
        }

        public String getUbigeoSunat() {
            return ubigeoSunat;
        }

        public void setUbigeoSunat(String ubigeoSunat) {
            this.ubigeoSunat = ubigeoSunat;
        }

        // Métodos de conveniencia para compatibilidad con frontend
        public String getFirst_name() {
            return nombres;
        }

        public String getFirst_last_name() {
            return apellidoPaterno;
        }

        public String getSecond_last_name() {
            return apellidoMaterno;
        }
    }

    // Constructor vacío
    public DniResponse() {
    }

    // Getters y Setters principales
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DniData getData() {
        return data;
    }

    public void setData(DniData data) {
        this.data = data;
    }

    // Métodos de conveniencia para acceder directamente a los datos
    public String getDni() {
        return data != null ? data.getNumero() : null;
    }

    public String getNombres() {
        return data != null ? data.getNombres() : null;
    }

    public String getNombreCompleto() {
        return data != null ? data.getNombreCompleto() : null;
    }

    public String getApellidoPaterno() {
        return data != null ? data.getApellidoPaterno() : null;
    }

    public String getApellidoMaterno() {
        return data != null ? data.getApellidoMaterno() : null;
    }

    public String getDireccion() {
        return data != null ? data.getDireccion() : null;
    }

    public String getDireccionCompleta() {
        return data != null ? data.getDireccionCompleta() : null;
    }

    // Métodos para compatibilidad con frontend
    public String getFirst_name() {
        return getNombres();
    }

    public String getFirst_last_name() {
        return getApellidoPaterno();
    }

    public String getSecond_last_name() {
        return getApellidoMaterno();
    }
}