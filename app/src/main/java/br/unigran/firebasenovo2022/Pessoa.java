package br.unigran.firebasenovo2022;

public class Pessoa {
    public String nome;
    public String cidade;

    @Override
    public String toString() {
        return  nome +" - "+cidade;
    }
}
