package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;

@Entity
public class SessionMeasurements {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String generalData_Weight;
    private String generalData_BMI;
    private String generalData_BodyFatPercentage;
    private String generalData_TotalBodyFatLb;
    private String generalData_LeanMass;
    private String generalData_BloodPressure;
    private String generalData_RangeOfMotion;
    private String generalData_RestingHR;

    private String skinFoldTests_Abdominal;
    private String skinFoldTests_Chest;
    private String skinFoldTests_Midaxillary;
    private String skinFoldTests_Subscapular;
    private String skinFoldTests_Suprailiac;
    private String skinFoldTests_Thigh;
    private String skinFoldTests_Tricep;

    private String girthMeasurements_Abdominal;
    private String girthMeasurements_Biceps;
    private String girthMeasurements_Calf;
    private String girthMeasurements_Chest;
    private String girthMeasurements_Hip;
    private String girthMeasurements_Shoulders;
    private String girthMeasurements_Thigh;
    private String girthMeasurements_Waist;
    private String girthMeasurements_TotalInchesLost;

    private String sixMinuteTreadmillTest_Distance;
    private String sixMinuteTreadmillTest_Speed;
    private String sixMinuteTreadmillTest_HR;
    private String sixMinuteTreadmillTest_BR;

    public SessionMeasurements() {
    }

    public String getGeneralData_Weight() {
        return generalData_Weight;
    }

    public void setGeneralData_Weight(String generalData_Weight) {
        this.generalData_Weight = generalData_Weight;
    }

    public String getGeneralData_BMI() {
        return generalData_BMI;
    }

    public void setGeneralData_BMI(String generalData_BMI) {
        this.generalData_BMI = generalData_BMI;
    }

    public String getGeneralData_BodyFatPercentage() {
        return generalData_BodyFatPercentage;
    }

    public void setGeneralData_BodyFatPercentage(String generalData_BodyFatPercentage) {
        this.generalData_BodyFatPercentage = generalData_BodyFatPercentage;
    }

    public String getGeneralData_TotalBodyFatLb() {
        return generalData_TotalBodyFatLb;
    }

    public void setGeneralData_TotalBodyFatLb(String generalData_TotalBodyFatLb) {
        this.generalData_TotalBodyFatLb = generalData_TotalBodyFatLb;
    }

    public String getGeneralData_LeanMass() {
        return generalData_LeanMass;
    }

    public void setGeneralData_LeanMass(String generalData_LeanMass) {
        this.generalData_LeanMass = generalData_LeanMass;
    }

    public String getGeneralData_BloodPressure() {
        return generalData_BloodPressure;
    }

    public void setGeneralData_BloodPressure(String generalData_BloodPressure) {
        this.generalData_BloodPressure = generalData_BloodPressure;
    }

    public String getGeneralData_RangeOfMotion() {
        return generalData_RangeOfMotion;
    }

    public void setGeneralData_RangeOfMotion(String generalData_RangeOfMotion) {
        this.generalData_RangeOfMotion = generalData_RangeOfMotion;
    }

    public String getGeneralData_RestingHR() {
        return generalData_RestingHR;
    }

    public void setGeneralData_RestingHR(String generalData_RestingHR) {
        this.generalData_RestingHR = generalData_RestingHR;
    }

    public String getSkinFoldTests_Abdominal() {
        return skinFoldTests_Abdominal;
    }

    public void setSkinFoldTests_Abdominal(String skinFoldTests_Abdominal) {
        this.skinFoldTests_Abdominal = skinFoldTests_Abdominal;
    }

    public String getSkinFoldTests_Chest() {
        return skinFoldTests_Chest;
    }

    public void setSkinFoldTests_Chest(String skinFoldTests_Chest) {
        this.skinFoldTests_Chest = skinFoldTests_Chest;
    }

    public String getSkinFoldTests_Midaxillary() {
        return skinFoldTests_Midaxillary;
    }

    public void setSkinFoldTests_Midaxillary(String skinFoldTests_Midaxillary) {
        this.skinFoldTests_Midaxillary = skinFoldTests_Midaxillary;
    }

    public String getSkinFoldTests_Subscapular() {
        return skinFoldTests_Subscapular;
    }

    public void setSkinFoldTests_Subscapular(String skinFoldTests_Subscapular) {
        this.skinFoldTests_Subscapular = skinFoldTests_Subscapular;
    }

    public String getSkinFoldTests_Suprailiac() {
        return skinFoldTests_Suprailiac;
    }

    public void setSkinFoldTests_Suprailiac(String skinFoldTests_Suprailiac) {
        this.skinFoldTests_Suprailiac = skinFoldTests_Suprailiac;
    }

    public String getSkinFoldTests_Thigh() {
        return skinFoldTests_Thigh;
    }

    public void setSkinFoldTests_Thigh(String skinFoldTests_Thigh) {
        this.skinFoldTests_Thigh = skinFoldTests_Thigh;
    }

    public String getSkinFoldTests_Tricep() {
        return skinFoldTests_Tricep;
    }

    public void setSkinFoldTests_Tricep(String skinFoldTests_Tricep) {
        this.skinFoldTests_Tricep = skinFoldTests_Tricep;
    }

    public String getGirthMeasurements_Abdominal() {
        return girthMeasurements_Abdominal;
    }

    public void setGirthMeasurements_Abdominal(String girthMeasurements_Abdominal) {
        this.girthMeasurements_Abdominal = girthMeasurements_Abdominal;
    }

    public String getGirthMeasurements_Biceps() {
        return girthMeasurements_Biceps;
    }

    public void setGirthMeasurements_Biceps(String girthMeasurements_Biceps) {
        this.girthMeasurements_Biceps = girthMeasurements_Biceps;
    }

    public String getGirthMeasurements_Calf() {
        return girthMeasurements_Calf;
    }

    public void setGirthMeasurements_Calf(String girthMeasurements_Calf) {
        this.girthMeasurements_Calf = girthMeasurements_Calf;
    }

    public String getGirthMeasurements_Chest() {
        return girthMeasurements_Chest;
    }

    public void setGirthMeasurements_Chest(String girthMeasurements_Chest) {
        this.girthMeasurements_Chest = girthMeasurements_Chest;
    }

    public String getGirthMeasurements_Hip() {
        return girthMeasurements_Hip;
    }

    public void setGirthMeasurements_Hip(String girthMeasurements_Hip) {
        this.girthMeasurements_Hip = girthMeasurements_Hip;
    }

    public String getGirthMeasurements_Shoulders() {
        return girthMeasurements_Shoulders;
    }

    public void setGirthMeasurements_Shoulders(String girthMeasurements_Shoulders) {
        this.girthMeasurements_Shoulders = girthMeasurements_Shoulders;
    }

    public String getGirthMeasurements_Thigh() {
        return girthMeasurements_Thigh;
    }

    public void setGirthMeasurements_Thigh(String girthMeasurements_Thigh) {
        this.girthMeasurements_Thigh = girthMeasurements_Thigh;
    }

    public String getGirthMeasurements_Waist() {
        return girthMeasurements_Waist;
    }

    public void setGirthMeasurements_Waist(String girthMeasurements_Waist) {
        this.girthMeasurements_Waist = girthMeasurements_Waist;
    }

    public String getGirthMeasurements_TotalInchesLost() {
        return girthMeasurements_TotalInchesLost;
    }

    public void setGirthMeasurements_TotalInchesLost(String girthMeasurements_TotalInchesLost) {
        this.girthMeasurements_TotalInchesLost = girthMeasurements_TotalInchesLost;
    }

    public String getSixMinuteTreadmillTest_Distance() {
        return sixMinuteTreadmillTest_Distance;
    }

    public void setSixMinuteTreadmillTest_Distance(String sixMinuteTreadmillTest_Distance) {
        this.sixMinuteTreadmillTest_Distance = sixMinuteTreadmillTest_Distance;
    }

    public String getSixMinuteTreadmillTest_Speed() {
        return sixMinuteTreadmillTest_Speed;
    }

    public void setSixMinuteTreadmillTest_Speed(String sixMinuteTreadmillTest_Speed) {
        this.sixMinuteTreadmillTest_Speed = sixMinuteTreadmillTest_Speed;
    }

    public String getSixMinuteTreadmillTest_HR() {
        return sixMinuteTreadmillTest_HR;
    }

    public void setSixMinuteTreadmillTest_HR(String sixMinuteTreadmillTest_HR) {
        this.sixMinuteTreadmillTest_HR = sixMinuteTreadmillTest_HR;
    }

    public String getSixMinuteTreadmillTest_BR() {
        return sixMinuteTreadmillTest_BR;
    }

    public void setSixMinuteTreadmillTest_BR(String sixMinuteTreadmillTest_BR) {
        this.sixMinuteTreadmillTest_BR = sixMinuteTreadmillTest_BR;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
