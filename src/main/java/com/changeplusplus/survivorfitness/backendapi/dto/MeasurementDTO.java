package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.entity.Measurement;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementDTO {
    private Integer id;
    private String name;
    private String value;
    private String category;
    private String unit;

    public MeasurementDTO(Measurement measurementEntity) {
        this.id = measurementEntity.getId();
        this.name = measurementEntity.getName();
        this.value = measurementEntity.getValue();
        this.category = measurementEntity.getCategory();
        this.unit = measurementEntity.getUnit();
    }

    public MeasurementDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
