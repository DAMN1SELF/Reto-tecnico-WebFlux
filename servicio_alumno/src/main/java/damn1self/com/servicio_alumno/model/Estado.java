package damn1self.com.servicio_alumno.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Estado { ACTIVO, INACTIVO;
    @JsonCreator
    public static Estado from(String v) {
        if (v == null) return null;
        try { return Estado.valueOf(v.trim().toUpperCase()); }
        catch (Exception e) { throw new IllegalArgumentException("estado debe ser ACTIVO o INACTIVO"); }
    }
}