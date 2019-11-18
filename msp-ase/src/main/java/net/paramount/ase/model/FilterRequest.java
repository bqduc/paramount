package net.paramount.ase.model;

import lombok.Data;

@Data
public class FilterRequest {
    private Boolean active;
    private String zipFilter;

    public FilterRequest() { }
}
