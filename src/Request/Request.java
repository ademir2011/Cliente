/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ademir
 */
public class Request implements Serializable { 
  
    private static final long serialVersionUID = 1L;
    private String nome;
    private byte[] conteudo;
    private String diretorioDestino;
    private Date dataHoraUpload;
    private String login;
    private String senha;
    private String operacao;
    private Map<String, String> paths;
    private String obs;
    private long lastModified;
    private boolean directory;

    public Request() {
    }
    
    public Request(String login, String senha, String operacao) {
        this.login = login;
        this.senha = senha;
        this.operacao = operacao;
    }

    public Request(String nome, byte[] conteudo, String diretorioDestino, Date dataHoraUpload, String login, String senha, String operacao) {
        this.nome = nome;
        this.conteudo = conteudo;
        this.diretorioDestino = diretorioDestino;
        this.dataHoraUpload = dataHoraUpload;
        this.login = login;
        this.senha = senha;
        this.operacao = operacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    public String getDiretorioDestino() {
        return diretorioDestino;
    }

    public void setDiretorioDestino(String diretorioDestino) {
        this.diretorioDestino = diretorioDestino;
    }

    public Date getDataHoraUpload() {
        return dataHoraUpload;
    }

    public void setDataHoraUpload(Date dataHoraUpload) {
        this.dataHoraUpload = dataHoraUpload;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public Map<String, String> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, String> paths) {
        this.paths = paths;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }
    
    
    
    
}


