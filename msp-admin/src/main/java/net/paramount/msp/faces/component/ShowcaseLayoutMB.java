/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.paramount.msp.faces.component;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;

/**
 *
 * @author rmpestano
 */
@Specializes
@SessionScoped
public class ShowcaseLayoutMB extends com.github.adminfaces.template.bean.LayoutMB {
    
    private boolean flat;
    
    private boolean materialButtons;
    
    @PostConstruct
    public void init() {
        super.init();
        flat = false;
    }

    public boolean isFlat() {
        return flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public boolean isMaterialButtons() {
        return materialButtons;
    }

    public void setMaterialButtons(boolean materialButtons) {
        this.materialButtons = materialButtons;
    }
    
    
    
    
    
}
