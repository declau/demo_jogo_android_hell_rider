package br.com.declau.hellrider.config;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import android.hardware.SensorManager;

public class DeviceSettings {
	
	private static SensorManager sensormanager;
	
	public static void setSensorManager(SensorManager sensorManager) {
		sensormanager = sensorManager;
	}
	
	public static CGPoint screenResolution(CGPoint gcPoint) {
		return gcPoint;
	}

	public static float screenWidth() {
		return winSize().width; 
	}

	public static float screenHeight() {
		return winSize().height; 
	}
	
	public static CGSize winSize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	public static SensorManager getSensormanager() {
		return sensormanager;
	}


}
