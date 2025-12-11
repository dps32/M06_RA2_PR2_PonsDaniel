package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ciutats")
public class Ciutat implements Serializable {

    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ciutatId", unique=true, nullable=false)
    private long ciutatId;

    private String nom;
    private String pais;
    private int poblacio;
    
    // Relació OneToMany amb Ciutada
    @OneToMany(mappedBy = "ciutat", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.EAGER)
    private Set<Ciutada> ciutadans = new HashSet<>();

    public Ciutat() {}


    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }


    // Getters y Setters
    public long getCiutatId() { return ciutatId; }
    public String getNom() { return nom; }
    public String getPais() { return pais; }
    public int getPoblacio() { return poblacio; }
    public Set<Ciutada> getCiutadans() { return ciutadans; }
    
    public void setCiutatId(long ciutatId) { this.ciutatId = ciutatId; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPais(String pais) { this.pais = pais; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }
    public void setCiutadans(Set<Ciutada> ciutadans) { this.ciutadans = ciutadans; }


    @Override
    public String toString() {
        String ciutadansStr = "[]";

        if (ciutadans != null && !ciutadans.isEmpty()) {
            ciutadansStr = "[";
            boolean first = true;

            for (Ciutada c : ciutadans) {
                if (!first) ciutadansStr += " | ";
                ciutadansStr += c.getNom() + " " + c.getCognom();
                first = false;
            }

            ciutadansStr += "]";
        }
        
        return this.getCiutatId() + ": " + this.getNom() + " (" + this.getPais() + "), Població: " + this.getPoblacio() + ", Ciutadans: " + ciutadansStr;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Ciutat ciutat = (Ciutat) o;
        return ciutatId == ciutat.ciutatId;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(ciutatId);
    }
    
}
