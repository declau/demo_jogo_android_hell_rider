package br.com.declau.hellrider.game.scenes;

import static br.com.declau.hellrider.config.DeviceSettings.screenHeight;
import static br.com.declau.hellrider.config.DeviceSettings.screenResolution;
import static br.com.declau.hellrider.config.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import br.com.declau.hellrider.R;
import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.game.control.Button;
import br.com.declau.hellrider.game.control.ButtonDelegate;
import br.com.declau.hellrider.screens.ScreenBackground;


public class GameOverScreen extends CCLayer implements ButtonDelegate{

	private ScreenBackground background;
	private Button beginButton;
	
	public CCScene scene() {
		CCScene scene = CCScene.node();
		scene.addChild(this);
		return scene;
	}
	 
	public GameOverScreen() {
		
		// background
		this.background = new ScreenBackground(Assets.BACKGROUND02);
		this.background.setPosition(screenResolution(CGPoint.ccp(screenWidth() / 2.0f, screenHeight() / 2.0f)));
		this.addChild(this.background);
				
		// image
		CCSprite title = CCSprite.sprite(Assets.GAMEOVER);
		title.setPosition(screenResolution(CGPoint.ccp( screenWidth() /2 , screenHeight() - 150 ))) ;
		this.addChild(title);
		
		// Enable Touch
		this.setIsTouchEnabled(true);
		this.beginButton = new Button(Assets.PLAY);
		this.beginButton.setPosition(screenResolution(CGPoint.ccp( screenWidth() /2 , screenHeight() - 250 ))) ;
		this.beginButton.setDelegate(this);
		addChild(this.beginButton);
		
		// add sound
				SoundEngine.sharedEngine().playSound(
						CCDirector.sharedDirector().getActivity(), R.raw.hollow, true);
	}

	@Override
	public void buttonClicked(Button sender) {
		if (sender.equals(this.beginButton)) {
			SoundEngine.sharedEngine().pauseSound();
			CCDirector.sharedDirector().replaceScene(new TitleScreen().scene());
		}		
	}	
	
}