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

import br.com.declau.hellrider.R;
import br.com.declau.hellrider.config.Runner;
import br.com.declau.hellrider.game.interfaces.MissileEngineDelegate;


public class Missile extends CCSprite {

	private MissileEngineDelegate delegate;
	private float x, y;

	public Missile(String image) {
		super(image);
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
			this.setPosition(screenResolution(CGPoint.ccp(x, y)));
		}
	}

	public void setDelegate(MissileEngineDelegate delegate) {
		this.delegate = delegate;
	}
	
	public void shoot(){
		// PLay explosion
		SoundEngine.sharedEngine().playEffect(
			CCDirector.sharedDirector().getActivity(), R.raw.bang);
	}

	// hit
	public void shooted() {

		// PLay explosion
		SoundEngine.sharedEngine().playEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		// Remove from Game Array
		this.delegate.removeMissile(this);

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
