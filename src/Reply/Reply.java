/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reply;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ademir
 */
public class Reply implements Serializable {
    
    private static final long serialVersionUID = 1L;    
    private String nome;
    private byte[] conteudo;
    private Date dataHoraUpload;
    private String obs;
    private long userId;
    private String login;
    private String senha;
    private List<String> paths;
    
    public Reply() {
    }

    public Reply(String obs) {
        this.obs = obs;
    }

    public Reply(String nome, byte[] conteudo, Date dataHoraUpload) {
        this.nome = nome;
        this.conteudo = conteudo;
        this.dataHoraUpload = dataHoraUpload;
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

    public Date getDataHoraUpload() {
        return dataHoraUpload;
    }

    public void setDataHoraUpload(Date dataHoraUpload) {
        this.dataHoraUpload = dataHoraUpload;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = new ArrayList<String>(paths);
    }
    
    
}