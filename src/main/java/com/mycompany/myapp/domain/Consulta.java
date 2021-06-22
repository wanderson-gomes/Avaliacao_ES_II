package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consulta.
 */
@Entity
@Table(name = "consulta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "data_consulta")
    private LocalDate dataConsulta;

    @Column(name = "horario")
    private String horario;

    @JsonIgnoreProperties(value = { "planoDeSaudes", "administrador" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consulta id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdPaciente() {
        return this.idPaciente;
    }

    public Consulta idPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
        return this;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getDataConsulta() {
        return this.dataConsulta;
    }

    public Consulta dataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
        return this;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getHorario() {
        return this.horario;
    }

    public Consulta horario(String horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public Consulta paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consulta)) {
            return false;
        }
        return id != null && id.equals(((Consulta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consulta{" +
            "id=" + getId() +
            ", idPaciente=" + getIdPaciente() +
            ", dataConsulta='" + getDataConsulta() + "'" +
            ", horario='" + getHorario() + "'" +
            "}";
    }
}
