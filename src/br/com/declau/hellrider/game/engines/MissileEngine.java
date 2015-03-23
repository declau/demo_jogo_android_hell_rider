package br.com.declau.hellrider.game.engines;

import java.util.Random;

import org.cocos2d.layers.CCLayer;

import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.config.Runner;
import br.com.declau.hellrider.game.interfaces.MissileEngineDelegate;
import br.com.declau.hellrider.game.objects.Missile;


public class MissileEngine extends CCLayer {

	private MissileEngineDelegate delegate;

	public MissileEngine() {
		this.schedule("missileEngine", 1.0f / 10f);
	}

	public void missileEngine(float dt) {

		// pause
		if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {

			if (new Random().nextInt(10) == 0) {
				this.getDelegate().createMissile(
						new Missile(Assets.MISSILE));
			}

		}
	}

	public void setDelegate(MissileEngineDelegate delegate) {
		this.delegate = delegate;
	}

	public MissileEngineDelegate getDelegate() {
		return delegate;
	}

}
