package com.pointproject.pointproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aengus on 26.09.2017.
 */

public class Values {
    private Values(){}

    public static Map<PolygonOptions, String> areas = new HashMap<>();

    static{
        areas.put(new PolygonOptions().add(
                new LatLng(46.322682, 30.680523),
                new LatLng(46.363211, 30.721035),
                new LatLng(46.360605, 30.664387),
                new LatLng(46.351601, 30.665760),
                new LatLng(46.350653, 30.646877),
                new LatLng(46.338092, 30.663700),
                new LatLng(46.327661, 30.664387)
                ).fillColor(0x3F00FF00).strokeColor(0x7F00FF00),
                "area1");
        areas.put(new PolygonOptions().add(
                new LatLng(46.363211, 30.721035),
                new LatLng(46.363448, 30.700436),
                new LatLng(46.396607, 30.704212),
                new LatLng(46.402526, 30.757771),
                new LatLng(46.374345, 30.752277)
                ).fillColor(0x3FE0FF00).strokeColor(0x7FE0FF00),
                "area2");
        areas.put(new PolygonOptions().add(
                new LatLng(46.402526, 30.757771),
                new LatLng(46.397080, 30.708675),
                new LatLng(46.429036, 30.729275),
                new LatLng(46.441576, 30.772533)
                ).fillColor(0x3FFF0088).strokeColor(0x7FFF0088),
                "area3");
        areas.put(new PolygonOptions().add(
                new LatLng(46.397080, 30.708675),
                new LatLng(46.429542, 30.728674),
                new LatLng(46.454823, 30.682583),
                new LatLng(46.440866, 30.625248)
                ).fillColor(0x3FC100FF).strokeColor(0x7FC100FF),
                "area4");
        areas.put(new PolygonOptions().add(
                new LatLng(46.429036, 30.729275),
                new LatLng(46.471613, 30.742321),
                new LatLng(46.469722, 30.764294),
                new LatLng(46.441576, 30.772533)
                ).fillColor(0x3F5C00FF).strokeColor(0x7F5C00FF),
                "area5");
        areas.put(new PolygonOptions().add(
                new LatLng(46.429036, 30.729275),
                new LatLng(46.471613, 30.742321),
                new LatLng(46.475160, 30.707302),
                new LatLng(46.463573, 30.706959),
                new LatLng(46.454823, 30.682583)
                ).fillColor(0x3F007EFF).strokeColor(0x7F007EFF),
                "area6");
        areas.put(new PolygonOptions().add(
                new LatLng(46.449856, 30.652370),
                new LatLng(46.454823, 30.682583),
                new LatLng(46.463573, 30.706959),
                new LatLng(46.475160, 30.707302),
                new LatLng(46.507306, 30.627995)
                ).fillColor(0x3F00BD4D).strokeColor(0x700BD4D),
                "area7");
                areas.put(new PolygonOptions().add(
                new LatLng(46.507306, 30.627995),
                new LatLng(46.527623, 30.680866),
                new LatLng(46.497617, 30.721722),
                new LatLng(46.490053, 30.761204),
                new LatLng(46.469722, 30.764294),
                new LatLng(46.475160, 30.707302)
                ).fillColor(0x3F00A3BD).strokeColor(0x7F00A3BD),
                "area8");
        areas.put(new PolygonOptions().add(
                new LatLng(46.496199, 30.736485),
                new LatLng(46.497617, 30.721722),
                new LatLng(46.527623, 30.680866),
                new LatLng(46.541322, 30.671940),
                new LatLng(46.558322, 30.703526),
                new LatLng(46.542739, 30.750561)
                ).fillColor(0x3FBD0400).strokeColor(0x7FBD0400),
                "area9");
        areas.put(new PolygonOptions().add(
                new LatLng(46.542739, 30.750561),
                new LatLng(46.558322, 30.703526),
                new LatLng(46.567764, 30.760860),
                new LatLng(46.590182, 30.768757),
                new LatLng(46.595609, 30.802402),
                new LatLng(46.580272, 30.810986),
                new LatLng(46.558086, 30.798969)
                ).fillColor(0x3FEF8601).strokeColor(0x7FEF8601),
                "area10");
    }
}
