package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private Integer cpf;

    @Column(name = "rg")
    private Integer rg;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "contato")
    private Integer contato;

    @OneToMany(mappedBy = "paciente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    private Set<PlanoDeSaude> planoDeSaudes = new HashSet<>();

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

    public Paciente id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public Paciente idUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return this.nome;
    }

    public Paciente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCpf() {
        return this.cpf;
    }

    public Paciente cpf(Integer cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public Integer getRg() {
        return this.rg;
    }

    public Paciente rg(Integer rg) {
        this.rg = rg;
        return this;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Paciente dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCidade() {
        return this.cidade;
    }

    public Paciente cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Paciente bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getContato() {
        return this.contato;
    }

    public Paciente contato(Integer contato) {
        this.contato = contato;
        return this;
    }

    public void setContato(Integer contato) {
        this.contato = contato;
    }

    public Set<PlanoDeSaude> getPlanoDeSaudes() {
        return this.planoDeSaudes;
    }

    public Paciente planoDeSaudes(Set<PlanoDeSaude> planoDeSaudes) {
        this.setPlanoDeSaudes(planoDeSaudes);
        return this;
    }

    public Paciente addPlanoDeSaude(PlanoDeSaude planoDeSaude) {
        this.planoDeSaudes.add(planoDeSaude);
        planoDeSaude.setPaciente(this);
        return this;
    }

    public Paciente removePlanoDeSaude(PlanoDeSaude planoDeSaude) {
        this.planoDeSaudes.remove(planoDeSaude);
        planoDeSaude.setPaciente(null);
        return this;
    }

    public void setPlanoDeSaudes(Set<PlanoDeSaude> planoDeSaudes) {
        if (this.planoDeSaudes != null) {
            this.planoDeSaudes.forEach(i -> i.setPaciente(null));
        }
        if (planoDeSaudes != null) {
            planoDeSaudes.forEach(i -> i.setPaciente(this));
        }
        this.planoDeSaudes = planoDeSaudes;
    }

    public Administrador getAdministrador() {
        return this.administrador;
    }

    public Paciente administrador(Administrador administrador) {
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
        if (!(o instanceof Paciente)) {
            return false;
        }
        return id != null && id.equals(((Paciente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", nome='" + getNome() + "'" +
            ", cpf=" + getCpf() +
            ", rg=" + getRg() +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", cidade='" + getCidade() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", contato=" + getContato() +
            "}";
    }
}
