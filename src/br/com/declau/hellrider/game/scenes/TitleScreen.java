package br.com.declau.hellrider.game.scenes;

import static br.com.declau.hellrider.config.DeviceSettings.screenResolution;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import br.com.declau.hellrider.R;
import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.game.control.MenuButtons;
import br.com.declau.hellrider.screens.ScreenBackground;


public class TitleScreen extends CCLayer {

	private ScreenBackground background;
	
	public CCScene scene() {
		CCScene scene = CCScene.node();
		scene.addChild(this);
		return scene;
	}

	public TitleScreen() {
			
		// background
		this.background = new ScreenBackground(Assets.BACKGROUND);
		this.background.setPosition(
			screenResolution(CGPoint.ccp(
				CCDirector.sharedDirector().winSize().width / 2.0f,
				CCDirector.sharedDirector().winSize().height / 2.0f
		)));
		this.addChild(this.background);
		
		// logo
		CCSprite title = CCSprite.sprite(Assets.LOGO);
		title.setPosition(screenResolution(
				CGPoint.ccp( br.com.declau.hellrider.config.DeviceSettings.screenWidth() /2 , 
						br.com.declau.hellrider.config.DeviceSettings.screenHeight() - 400 )));
		//this.addChild(title);
		
		// Add options layer
		MenuButtons menuLayer = new MenuButtons();
		this.addChild(menuLayer);
		// add sound
		SoundEngine.sharedEngine().playSound(
				CCDirector.sharedDirector().getActivity(), R.raw.cowboys, true);
		
	}

}
