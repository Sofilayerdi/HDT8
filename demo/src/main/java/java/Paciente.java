import java.util.Objects;

public class Paciente implements Comparable<Paciente> {
    private String nombre;
    private String sintoma;
    private char codigoEmergencia;

    public Paciente(String nombre, String sintoma, char codigoEmergencia) {
        this.nombre = nombre;
        this.sintoma = sintoma;
        this.codigoEmergencia = Character.toUpperCase(codigoEmergencia);
    }

    @Override
    public int compareTo(Paciente otro) {
        return Character.compare(this.codigoEmergencia, otro.codigoEmergencia);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paciente paciente = (Paciente) o;
        return codigoEmergencia == paciente.codigoEmergencia &&
               Objects.equals(nombre, paciente.nombre) &&
               Objects.equals(sintoma, paciente.sintoma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, sintoma, codigoEmergencia);
    }

    @Override
    public String toString() {
        return nombre + ", " + sintoma + ", " + codigoEmergencia;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getSintoma() { return sintoma; }
    public char getCodigoEmergencia() { return codigoEmergencia; }
}