/**
 * @Package com.qianwang.util.lbs
 * @author Administrator
 * @date 2016年4月22日 上午11:07:04
 * @version V1.0
 */

package com.qianwang.util.lbs;

import java.util.List;

import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * 几何拓扑工具类
  * @ClassName: GeoTopologyUtils
  * @author Administrator
  * @date 2016年4月22日 上午11:07:04
  */

public class GeoTopologyUtils {

	/**
	 * 计算lineString的buffer，生成distance范围内的缓冲区多边形
	  * @param ptList	double[0], double[1]
	  * @param distance 
	  * @return GeoJSON  buffer出的多边形的geojson
	  * @throws
	 */
	public static GeoJSON lineStringBuffer(List<Double[]> ptList, double distance) {
		if((ptList == null || ptList.size() < 1) || distance < 0) throw new IllegalArgumentException();
		
		GeometryFactory geometryFactory = new GeometryFactory();
        CoordinateSequenceFactory fac = geometryFactory.getCoordinateSequenceFactory();
    
    	int len = ptList.size();
    	Coordinate[] coords = new Coordinate[len];
    	for(int i=0; i<len; i++) {
    		Double[] p = ptList.get(i);
    		coords[i] = new Coordinate(p[0], p[1]);
    	}
        CoordinateSequence seq = fac.create(coords);
        LineString line = new LineString(seq, geometryFactory);
        Geometry geo = line.buffer(distance);
        //System.out.println(geo.toText());
        
        GeoJSONWriter writer = new GeoJSONWriter();
        GeoJSON json = writer.write(geo);
        //return json.toString();
        return json;
	}
}
