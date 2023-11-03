package io.github.fgabrielbraga.goservicedev.domain.enums;

public enum Perfil {
    ADMIN("Administrador"),
    PRESTADOR("Prestador"),
    CLIENTE("Cliente");

    private final String descricao;

    private Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
