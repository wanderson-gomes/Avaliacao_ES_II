package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.FormaDePag;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlanoDeSaude.
 */
@Entity
@Table(name = "plano_de_saude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlanoDeSaude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_plano")
    private Integer idPlano;

    @Column(name = "nome_plano")
    private String nomePlano;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "cnpj")
    private Integer cnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_de_pag")
    private FormaDePag formaDePag;

    @ManyToOne
    @JsonIgnoreProperties(value = { "planoDeSaudes", "administrador" }, allowSetters = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanoDeSaude id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdPlano() {
        return this.idPlano;
    }

    public PlanoDeSaude idPlano(Integer idPlano) {
        this.idPlano = idPlano;
        return this;
    }

    public void setIdPlano(Integer idPlano) {
        this.idPlano = idPlano;
    }

    public String getNomePlano() {
        return this.nomePlano;
    }

    public PlanoDeSaude nomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
        return this;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public PlanoDeSaude ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getCnpj() {
        return this.cnpj;
    }

    public PlanoDeSaude cnpj(Integer cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(Integer cnpj) {
        this.cnpj = cnpj;
    }

    public FormaDePag getFormaDePag() {
        return this.formaDePag;
    }

    public PlanoDeSaude formaDePag(FormaDePag formaDePag) {
        this.formaDePag = formaDePag;
        return this;
    }

    public void setFormaDePag(FormaDePag formaDePag) {
        this.formaDePag = formaDePag;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public PlanoDeSaude paciente(Paciente paciente) {
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
        if (!(o instanceof PlanoDeSaude)) {
            return false;
        }
        return id != null && id.equals(((PlanoDeSaude) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanoDeSaude{" +
            "id=" + getId() +
            ", idPlano=" + getIdPlano() +
            ", nomePlano='" + getNomePlano() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", cnpj=" + getCnpj() +
            ", formaDePag='" + getFormaDePag() + "'" +
            "}";
    }
}
