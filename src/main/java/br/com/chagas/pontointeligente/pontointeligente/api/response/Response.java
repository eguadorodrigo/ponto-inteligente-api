package br.com.chagas.pontointeligente.pontointeligente.api.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Response<T> {
    
    private T data;
    
    private List<String> errors;

    /**
     * Conversão incluída somente para controle da lista caso seja nula
     * @return List<String>
     */
    public List<String> getErrors(){
        if (this.errors == null) {
            this.errors = new ArrayList<String>();            
        }
        return errors;
    } 
}