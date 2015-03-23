package br.com.declau.hellrider.game.engines;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.config.Runner;
import br.com.declau.hellrider.game.interfaces.EnemyEngineDelegate;
import br.com.declau.hellrider.game.objects.Enemy;
import br.com.declau.hellrider.game.objects.Score;
import br.com.declau.hellrider.game.objects.Shoot;
import br.com.declau.hellrider.game.scenes.GameScene;



public class EnemyEngine extends CCLayer {
	
	private EnemyEngineDelegate delegate;
	
	public EnemyEngine() {
		this.schedule("enemyEngine", 2.0f / 10f);
	}

	
	public void enemyEngine(float dt){
		
		// pause
				if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {

					if (new Random().nextInt(10) == 0) {
						this.getDelegate().createEnemy(
								new Enemy(Assets.ENEMY));
					}

				}
		
	}
	
	public void setDelegate(EnemyEngineDelegate delegate) {
		this.delegate = delegate;
	}
	
	public EnemyEngineDelegate getDelegate() {
		return delegate;
	}


	
}
