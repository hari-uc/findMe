package com.example.myapplication.Model.DirectionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionRouteModel {
    @SerializedName("legs")
    @Expose
    private List<DirectionLegModel> legs;

    @SerializedName("overview polyline")
    @Expose
    private DirectionPolyLineModel polyLineModel;

    @SerializedName("summary")
    @Expose
    private String summary;

    public List<DirectionLegModel> getLegs() {
        return legs;
    }

    public void setLegs(List<DirectionLegModel> legs) {
        this.legs = legs;
    }

    public DirectionPolyLineModel getPolyLineModel() {
        return polyLineModel;
    }

    public void setPolyLineModel(DirectionPolyLineModel polyLineModel) {
        this.polyLineModel = polyLineModel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
