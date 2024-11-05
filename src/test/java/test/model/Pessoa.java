package test.model;

import java.util.List;

import org.compacto.parser.model.CompactoSerializable;

public class Pessoa implements CompactoSerializable {
    public String nome;
    public Integer idade;
    public Boolean strong;
    public Long diasDeVida;
    public Float pesoEmGramas;
    public Double pesoEmQuilogramas;
    public Pessoa conjuge;
    public List<String> hobbies;
    private EstadoCivil estadoCivil;

    public Pessoa() {
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return this.idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Boolean isStrong() {
        return this.strong;
    }

    public Boolean getStrong() {
        return this.strong;
    }

    public void setStrong(Boolean strong) {
        this.strong = strong;
    }

    public Long getDiasDeVida() {
        return this.diasDeVida;
    }

    public void setDiasDeVida(Long diasDeVida) {
        this.diasDeVida = diasDeVida;
    }

    public Float getPesoEmGramas() {
        return this.pesoEmGramas;
    }

    public void setPesoEmGramas(Float pesoEmGramas) {
        this.pesoEmGramas = pesoEmGramas;
    }

    public Double getPesoEmQuilogramas() {
        return this.pesoEmQuilogramas;
    }

    public void setPesoEmQuilogramas(Double pesoEmQuilogramas) {
        this.pesoEmQuilogramas = pesoEmQuilogramas;
    }

    public Pessoa getConjuge() {
        return this.conjuge;
    }

    public void setConjuge(Pessoa conjuge) {
        this.conjuge = conjuge;
    }

    public List<String> getHobbies() {
        return this.hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public EstadoCivil getEstadoCivil() {
        return this.estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
}
