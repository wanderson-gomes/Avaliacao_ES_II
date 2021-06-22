package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medico.
 */
@Entity
@Table(name = "medico")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_medico")
    private Integer idMedico;

    @Column(name = "nome")
    private String nome;

    @Column(name = "crm")
    private Integer crm;

    @Column(name = "cpf")
    private Integer cpf;

    @Column(name = "rg")
    private Integer rg;

    @Column(name = "especialidade")
    private String especialidade;

    @Column(name = "contato")
    private Integer contato;

    @OneToMany(mappedBy = "medico")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medico" }, allowSetters = true)
    private Set<Relatorio> relatorios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "pacientes", "medicos" }, allowSetters = true)
    private Administrador administrador;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medico id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdMedico() {
        return this.idMedico;
    }

    public Medico idMedico(Integer idMedico) {
        this.idMedico = idMedico;
        return this;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public String getNome() {
        return this.nome;
    }

    public Medico nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCrm() {
        return this.crm;
    }

    public Medico crm(Integer crm) {
        this.crm = crm;
        return this;
    }

    public void setCrm(Integer crm) {
        this.crm = crm;
    }

    public Integer getCpf() {
        return this.cpf;
    }

    public Medico cpf(Integer cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public Integer getRg() {
        return this.rg;
    }

    public Medico rg(Integer rg) {
        this.rg = rg;
        return this;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public String getEspecialidade() {
        return this.especialidade;
    }

    public Medico especialidade(String especialidade) {
        this.especialidade = especialidade;
        return this;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Integer getContato() {
        return this.contato;
    }

    public Medico contato(Integer contato) {
        this.contato = contato;
        return this;
    }

    public void setContato(Integer contato) {
        this.contato = contato;
    }

    public Set<Relatorio> getRelatorios() {
        return this.relatorios;
    }

    public Medico relatorios(Set<Relatorio> relatorios) {
        this.setRelatorios(relatorios);
        return this;
    }

    public Medico addRelatorio(Relatorio relatorio) {
        this.relatorios.add(relatorio);
        relatorio.setMedico(this);
        return this;
    }

    public Medico removeRelatorio(Relatorio relatorio) {
        this.relatorios.remove(relatorio);
        relatorio.setMedico(null);
        return this;
    }

    public void setRelatorios(Set<Relatorio> relatorios) {
        if (this.relatorios != null) {
            this.relatorios.forEach(i -> i.setMedico(null));
        }
        if (relatorios != null) {
            relatorios.forEach(i -> i.setMedico(this));
        }
        this.relatorios = relatorios;
    }

    public Administrador getAdministrador() {
        return this.administrador;
    }

    public Medico administrador(Administrador administrador) {
        this.setAdministrador(administrador);
        return this;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return id != null && id.equals(((Medico) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", idMedico=" + getIdMedico() +
            ", nome='" + getNome() + "'" +
            ", crm=" + getCrm() +
            ", cpf=" + getCpf() +
            ", rg=" + getRg() +
            ", especialidade='" + getEspecialidade() + "'" +
            ", contato=" + getContato() +
            "}";
    }
}
