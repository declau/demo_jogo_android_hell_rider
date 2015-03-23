package br.com.declau.hellrider.game.objects;

import static br.com.declau.hellrider.config.DeviceSettings.screenHeight;
import static br.com.declau.hellrider.config.DeviceSettings.screenResolution;
import static br.com.declau.hellrider.config.DeviceSettings.screenWidth;

import java.util.Random;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;

import android.util.Log;
import br.com.declau.hellrider.R;
import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.config.Runner;
import br.com.declau.hellrider.game.interfaces.EnemyEngineDelegate;

public class Enemy extends CCSprite{
	
	//private ShootEngineDelegate delegate1;
	private EnemyEngineDelegate delegate;
	private float x, y;
	private int count;  
    private CCSprite directionEnemy;
	
	public Enemy(String image) {
		super(image);
		this.count=0;  
		x = new Random().nextInt(Math.round(screenWidth()));
		y = screenHeight();
	}
	
	public void start() {
		this.schedule("update");
	}
	
public void update(float dt) {
		
		// pause
		if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {
			y -= 1;
			//this.setPosition(screenResolution(CGPoint.ccp(x, y)));
			if(count <= 110){  
	            count++;  
	            x+=1;  
	            Log.i("Jogo", "esquerda");  
	            if(count > 110){  
	            	directionEnemy = CCSprite.sprite(Assets.ENEMY);                 
	            }  
	        } else if(count <= 220){  
	            count++;  
	            x-=1;  
	            Log.i("jogo", "Direita");  
	            if(count>220){  
	            	directionEnemy = CCSprite.sprite(Assets.ENEMY);                 
	                count=0;  
	            }  
	        }  		
			//this.addChild(directionEnemy);  
			this.setPosition(screenResolution(CGPoint.ccp(x, y)));			
		}
	}

	public void setDelegate(EnemyEngineDelegate delegate) {
		this.delegate = delegate;
}
	
	public void shoot() {
		/*if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {
			delegate1.createShoot(new Shoot(x, y));
		} */

		
		// PLay explosion
		SoundEngine.sharedEngine().playEffect(
			CCDirector.sharedDirector().getActivity(), R.raw.bang);
	}
	/*public void setDelegate(ShootEngineDelegate delegate) {
		this.delegate1 = delegate;
	}*/
	
	// hit
		public void shooted() {

			// PLay explosion
			SoundEngine.sharedEngine().playEffect(
					CCDirector.sharedDirector().getActivity(), R.raw.bang);

			// Remove from Game Array
			this.delegate.removeEnemy(this);

			// Stop Shoot
			this.unschedule("update");

			// Pop Actions
			float dt = 0.2f;
			CCScaleBy a1 = CCScaleBy.action(dt, 0.5f);
			CCFadeOut a2 = CCFadeOut.action(dt);
			CCSpawn s1 = CCSpawn.actions(a1, a2);

			// Call RemoveMe
			CCCallFunc c1 = CCCallFunc.action(this, "removeMe");

			// Run actions!
			this.runAction(CCSequence.actions(s1, c1));

		}
		
		public void removeMe() {
			this.removeFromParentAndCleanup(true);
		}
		

}
