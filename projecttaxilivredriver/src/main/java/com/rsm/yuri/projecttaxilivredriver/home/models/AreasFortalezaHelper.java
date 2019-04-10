package com.rsm.yuri.projecttaxilivredriver.home.models;

import static java.lang.Math.abs;

/**
 * Created by yuri_ on 03/02/2018.
 */

public class AreasFortalezaHelper extends AreasHelper{

    private final static double LAT_INI_FORTALEZA = -3.668985;
    private final static double LONG_INI_FORTALEZA = -38.672476;

    public AreasFortalezaHelper() {
        setInitialLatLong(LAT_INI_FORTALEZA, LONG_INI_FORTALEZA);
    }

    /*private final static double SIZE_AREA_Y[]= {0.05, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.03, 0.03, 0.05};
    private final static double SIZE_AREA_X[]= {0.04, 0.03, 0.02, 0.02, 0.01, 0.01, 0.01, 0.01, 0.01, 0.007, 0.01, 0.01, 0.02, 0.02, 0.04 };

    public GroupAreas getGroupAreas(double latitude, double longitude){
        GroupAreas groupAreas = new GroupAreas();

        groupAreas.setMainArea(getAreaFromLatLong(latitude, longitude));

        int sideAreaY = getSideCoordenadaArea(latitude, groupAreas.getMainArea().getaY(), LAT_INI, SIZE_AREA_Y);
        int sideAreaX = getSideCoordenadaArea(longitude, groupAreas.getMainArea().getaX(), LONG_INI, SIZE_AREA_X);

        groupAreas.setAreaVerticalSide( getAreaFromCoordenadasArea( sideAreaY, groupAreas.getMainArea().getaX() ) );

        groupAreas.setAreaHorizontalSide( getAreaFromCoordenadasArea( groupAreas.getMainArea().getaY(), sideAreaX ) );

        groupAreas.setAreaDiagonal( getAreaFromCoordenadasArea( sideAreaY, sideAreaX ) );

        return groupAreas;
    }

    private Area getAreaFromLatLong(double latitude, double longitude){
        return getAreaFromCoordenadasArea(getCoordenadaArea(latitude,LAT_INI,SIZE_AREA_Y),getCoordenadaArea(longitude, LONG_INI,SIZE_AREA_X));
    }

    private int getCoordenadaArea(double coordenadaGoogleMap, double coordenadaGoogleMapIni, double sizeAreaCoordenada[] ){
        double c = 0.0;
        double coordenadaInterna = abs(abs(coordenadaGoogleMap)-abs(coordenadaGoogleMapIni));
        for(int i = 0;i <sizeAreaCoordenada.length;i++) {
            c += sizeAreaCoordenada[i];
            if (c >= coordenadaInterna) {
                return i+1;
            }
        }
        return 0;
    }

    private Area getAreaFromCoordenadasArea(int aY, int aX){
        Area area = new Area();
        area.setaY(aY);
        area.setaX(aX);
        area.setId(getAreaId(aX, aY));
        return area;
    }

    private String getAreaId(int aX, int aY) {
        int intId;
        intId = aX+(aY-1)*16;
        return "a" + intId;
    }

    private int getSideCoordenadaArea(double coordenadaGoogleMap, int coordenadaArea, double coordenadaGoogleMapIni, double[] sizeAreaCoordenada){
        int sideAreaCoordenada;
        double coordenadaGoogleMapAreaBase = coordenadaGoogleMapIni;
        for( int i = 0 ; i < coordenadaArea -1 ; i++ ){
            coordenadaGoogleMapAreaBase -= sizeAreaCoordenada[i];
        }

        double offsetInArea = abs(coordenadaGoogleMap - coordenadaGoogleMapAreaBase);

        if(offsetInArea > (sizeAreaCoordenada[coordenadaArea])/2){
            sideAreaCoordenada = coordenadaArea+1;
        }else{
            sideAreaCoordenada = coordenadaArea-1;
        }

        return sideAreaCoordenada;
    }*/

}
