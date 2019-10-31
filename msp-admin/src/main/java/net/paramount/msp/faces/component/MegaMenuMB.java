package net.paramount.msp.faces.component;

import java.io.Serializable;

import javax.inject.Named;

import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class MegaMenuMB implements Serializable {

    private String orientation = "horizontal";
 
    public String getOrientation() {
        return orientation;
    }
 
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

}
