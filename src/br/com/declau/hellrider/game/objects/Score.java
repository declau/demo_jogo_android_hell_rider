package br.com.declau.hellrider.game.objects;

import static br.com.declau.hellrider.config.DeviceSettings.screenHeight;
import static br.com.declau.hellrider.config.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.transitions.CCFadeTransition;

import br.com.declau.hellrider.game.scenes.FinalScreen;
import br.com.declau.hellrider.game.scenes.GameScene;
import br.com.declau.hellrider.game.scenes.GameScene1;

public class Score  extends CCLayer {
	
	private int score;
	private CCBitmapFontAtlas text;
	
	private GameScene delegate;
	private GameScene1 delegate1;
	
	public void setDelegate(GameScene delegate) {
		this.delegate = delegate;
	}

	public int getScore() {
		return score;
	}

	public Score(){
		this.score = 0;
		
		this.text = CCBitmapFontAtlas.bitmapFontAtlas(
				String.valueOf(this.score),
				"UniSansSemiBold_Numbers_240.fnt");
		
		this.text.setScale((float) 240 / 240);
		
		this.setPosition(screenWidth()-50, screenHeight()-50);
		this.addChild(this.text);
	}
	
	public void increase() {
		score++;		
		this.text.setString(String.valueOf(this.score));
		
		if(score == 5){
			//CCDirector.sharedDirector().replaceScene(new GameScene1().scene());
			CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(1.0f,GameScene1.createGame()));	
		} 
		
		/*if (score == 8) {
			CCDirector.sharedDirector().replaceScene((CCFadeTransition.transition(1.0f, new FinalScreen().scene())));
		}*/

					
				
			
		
		
	}

	public void setDelegate1(GameScene1 gameScene1) {
		
	}

	
}
