package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Relatorio.
 */
@Entity
@Table(name = "relatorio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Relatorio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_medico")
    private Integer idMedico;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "diagnostico")
    private String diagnostico;

    @ManyToOne
    @JsonIgnoreProperties(value = { "relatorios", "administrador" }, allowSetters = true)
    private Medico medico;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Relatorio id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdMedico() {
        return this.idMedico;
    }

    public Relatorio idMedico(Integer idMedico) {
        this.idMedico = idMedico;
        return this;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Relatorio descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiagnostico() {
        return this.diagnostico;
    }

    public Relatorio diagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
        return this;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public Relatorio medico(Medico medico) {
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
        if (!(o instanceof Relatorio)) {
            return false;
        }
        return id != null && id.equals(((Relatorio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Relatorio{" +
            "id=" + getId() +
            ", idMedico=" + getIdMedico() +
            ", descricao='" + getDescricao() + "'" +
            ", diagnostico='" + getDiagnostico() + "'" +
            "}";
    }
}
