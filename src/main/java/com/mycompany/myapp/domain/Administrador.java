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
 * A Administrador.
 */
@Entity
@Table(name = "administrador")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Administrador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "id_administrador")
    private Integer idAdministrador;

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

    @OneToMany(mappedBy = "administrador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "planoDeSaudes", "administrador" }, allowSetters = true)
    private Set<Paciente> pacientes = new HashSet<>();

    @OneToMany(mappedBy = "administrador")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "relatorios", "administrador" }, allowSetters = true)
    private Set<Medico> medicos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Administrador id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdAdministrador() {
        return this.idAdministrador;
    }

    public Administrador idAdministrador(Integer idAdministrador) {
        this.idAdministrador = idAdministrador;
        return this;
    }

    public void setIdAdministrador(Integer idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getNome() {
        return this.nome;
    }

    public Administrador nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCpf() {
        return this.cpf;
    }

    public Administrador cpf(Integer cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public Integer getRg() {
        return this.rg;
    }

    public Administrador rg(Integer rg) {
        this.rg = rg;
        return this;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public LocalDate getDataNascimento() {
        return this.dataNascimento;
    }

    public Administrador dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCidade() {
        return this.cidade;
    }

    public Administrador cidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Administrador bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getContato() {
        return this.contato;
    }

    public Administrador contato(Integer contato) {
        this.contato = contato;
        return this;
    }

    public void setContato(Integer contato) {
        this.contato = contato;
    }

    public Set<Paciente> getPacientes() {
        return this.pacientes;
    }

    public Administrador pacientes(Set<Paciente> pacientes) {
        this.setPacientes(pacientes);
        return this;
    }

    public Administrador addPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
        paciente.setAdministrador(this);
        return this;
    }

    public Administrador removePaciente(Paciente paciente) {
        this.pacientes.remove(paciente);
        paciente.setAdministrador(null);
        return this;
    }

    public void setPacientes(Set<Paciente> pacientes) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.setAdministrador(null));
        }
        if (pacientes != null) {
            pacientes.forEach(i -> i.setAdministrador(this));
        }
        this.pacientes = pacientes;
    }

    public Set<Medico> getMedicos() {
        return this.medicos;
    }

    public Administrador medicos(Set<Medico> medicos) {
        this.setMedicos(medicos);
        return this;
    }

    public Administrador addMedico(Medico medico) {
        this.medicos.add(medico);
        medico.setAdministrador(this);
        return this;
    }

    public Administrador removeMedico(Medico medico) {
        this.medicos.remove(medico);
        medico.setAdministrador(null);
        return this;
    }

    public void setMedicos(Set<Medico> medicos) {
        if (this.medicos != null) {
            this.medicos.forEach(i -> i.setAdministrador(null));
        }
        if (medicos != null) {
            medicos.forEach(i -> i.setAdministrador(this));
        }
        this.medicos = medicos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Administrador)) {
            return false;
        }
        return id != null && id.equals(((Administrador) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Administrador{" +
            "id=" + getId() +
            ", idAdministrador=" + getIdAdministrador() +
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
