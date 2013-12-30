/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openmrs.module.dominicamodule.web.controller;

/**
 *
 * @author levine
 */
public class ConceptIdAndName {
    private int conceptId;
    private String conceptName;
    
    ConceptIdAndName(int id, String name) {
        conceptId = id; conceptName = name;
    }
    
    public int getConceptId() { return conceptId; }
    
    public String getConceptName() { return conceptName; }
    
}
