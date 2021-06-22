package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Retorno.
 */
@Entity
@Table(name = "retorno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Retorno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_medico")
    private Integer idMedico;

    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "data_retorno")
    private LocalDate dataRetorno;

    @JsonIgnoreProperties(value = { "relatorios", "administrador" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Medico medico;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Retorno id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdMedico() {
        return this.idMedico;
    }

    public Retorno idMedico(Integer idMedico) {
        this.idMedico = idMedico;
        return this;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public Integer getIdPaciente() {
        return this.idPaciente;
    }

    public Retorno idPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
        return this;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public LocalDate getDataRetorno() {
        return this.dataRetorno;
    }

    public Retorno dataRetorno(LocalDate dataRetorno) {
        this.dataRetorno = dataRetorno;
        return this;
    }

    public void setDataRetorno(LocalDate dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public Retorno medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Retorno)) {
            return false;
        }
        return id != null && id.equals(((Retorno) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Retorno{" +
            "id=" + getId() +
            ", idMedico=" + getIdMedico() +
            ", idPaciente=" + getIdPaciente() +
            ", dataRetorno='" + getDataRetorno() + "'" +
            "}";
    }
}
