package br.com.declau.hellrider.game.scenes;

import static br.com.declau.hellrider.config.DeviceSettings.screenHeight;
import static br.com.declau.hellrider.config.DeviceSettings.screenResolution;
import static br.com.declau.hellrider.config.DeviceSettings.screenWidth;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

import br.com.declau.hellrider.R;
import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.config.Runner;
import br.com.declau.hellrider.game.control.GameButtons;
import br.com.declau.hellrider.game.engines.EnemyEngine;
import br.com.declau.hellrider.game.engines.MissileEngine;
import br.com.declau.hellrider.game.interfaces.EnemyEngineDelegate;
import br.com.declau.hellrider.game.interfaces.MissileEngineDelegate;
import br.com.declau.hellrider.game.interfaces.PauseDelegate;
import br.com.declau.hellrider.game.interfaces.ShootEngineDelegate;
import br.com.declau.hellrider.game.objects.Enemy;
import br.com.declau.hellrider.game.objects.Missile;
import br.com.declau.hellrider.game.objects.Player;
import br.com.declau.hellrider.game.objects.Score;
import br.com.declau.hellrider.game.objects.Shoot;
import br.com.declau.hellrider.screens.PauseScreen;
import br.com.declau.hellrider.screens.ScreenBackground;

public class GameScene extends CCLayer implements MissileEngineDelegate,
		ShootEngineDelegate, PauseDelegate {     //, EnemyEngineDelegate

	// Layers
	private CCLayer missilesLayer;
	//private CCLayer enemyLayer;
	private CCLayer scoreLayer;
	private CCLayer playerLayer;
	private CCLayer shootsLayer;
	private CCLayer layerTop;

	// Engines
	private MissileEngine missileEngine;
	//private EnemyEngine enemyEngine;
	

	// Arrays
	@SuppressWarnings("rawtypes")
	private ArrayList missilesArray;
	@SuppressWarnings("rawtypes")
	private ArrayList playersArray;
	@SuppressWarnings("rawtypes")
	private ArrayList shootsArray;
	@SuppressWarnings("rawtypes")
	//private ArrayList enemyArray;

	// Screens
	private PauseScreen pauseScreen;

	// Game Objects
	private Player player;
	private Score score;
	private boolean autoCalibration;
	private ScreenBackground background;
	//private Enemy enemy;
	

	public static CCScene createGame() {

		// Create Scene
		GameScene layer = new GameScene();
		CCScene scene = CCScene.node();
		scene.addChild(layer);

		return scene;
	}

	GameScene() {

		// Background
		this.background = new ScreenBackground(Assets.BACKGROUND01);
		this.background.setPosition(screenResolution(CGPoint.ccp(
				screenWidth() / 2.0f, screenHeight() / 2.0f)));
		this.addChild(this.background);

		GameButtons gameButtonsLayer = GameButtons.gameButtons();
		gameButtonsLayer.setDelegate(this);
		this.addChild(gameButtonsLayer);
		

		// Create Layers
		this.missilesLayer = CCLayer.node();
		this.playerLayer = CCLayer.node();
		this.scoreLayer = CCLayer.node();
		//this.enemyLayer = CCLayer.node();

		this.addGameObjects();

		this.shootsLayer = CCLayer.node();
		this.layerTop = CCLayer.node();

		// Add Layers
		//this.addChild(this.enemyLayer);
		this.addChild(this.missilesLayer);
		this.addChild(this.playerLayer);
		this.addChild(this.scoreLayer);
		this.addChild(this.shootsLayer);
		this.addChild(this.layerTop);

		this.setIsTouchEnabled(true);

		// sons
		// adicione musica ao jogo
		SoundEngine.sharedEngine().playSound(
				CCDirector.sharedDirector().getActivity(), R.raw.music, true);

		preloadCache();
	}

	private void preloadCache() {
		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.shoot);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.bang);

		SoundEngine.sharedEngine().preloadEffect(
				CCDirector.sharedDirector().getActivity(), R.raw.over);
	}

	private void addGameObjects() {
		this.missilesArray = new ArrayList();
		this.missileEngine = new MissileEngine();
		
		//this.enemyArray = new ArrayList();
		//this.enemyEngine = new EnemyEngine();
		
        
		// adicione o player ao jogo
		this.player = new Player();
		this.playerLayer.addChild(this.player);
		

		// score
		this.score = new Score();
		this.score.setDelegate(this);
		this.scoreLayer.addChild(this.score);

		// startgame
		this.playersArray = new ArrayList();
		this.playersArray.add(this.player);
		
		// Shoots
		this.shootsArray = new ArrayList();
		this.player.setDelegate(this);
		
	}

	public void startGame() {

		// Set Game Status
		// PAUSE
		Runner.check().setGamePlaying(true);
		Runner.check().setGamePaused(false);

		// Catch Accelerometer
		// Habilite o acelerometro
		player.catchAccelerometer();

		// pause
		SoundEngine.sharedEngine().setEffectsVolume(1f);
		SoundEngine.sharedEngine().setSoundVolume(1f);

		// startgame
		this.schedule("checkHits");
		this.schedule("checkHits2");

		this.startEngines();
	}

	@Override
	public void onEnter() {
		super.onEnter();
		this.schedule("checkHits");
		this.schedule("checkHits2");
		this.startEngines();

		// Start Game when transition did finish
		if (!this.autoCalibration) {
			this.startGame();
		}
	}

	// startgame
	public void checkHits(float dt) {

		this.checkRadiusHitsOfArray(this.missilesArray, 
				this.shootsArray, this, "missileHit");

		this.checkRadiusHitsOfArray(this.missilesArray,
				this.playersArray, this, "playerHit");
	}
	

	private boolean checkRadiusHitsOfArray(List<? extends CCSprite> array1,
				List<? extends CCSprite> array2,
			GameScene gameScene, String hit) {

		boolean result = false;

		for (int i = 0; i < array1.size(); i++) {
			// Get Object from First Array
			CGRect rect1 = getBoarders(array1.get(i));

			for (int j = 0; j < array2.size(); j++) {
				// Get Object from Second Array
				CGRect rect2 = getBoarders(array2.get(j));
				
				// Check Hit!
				
				if (CGRect.intersects(rect1, rect2)) {
					System.out.println("Colision Detected: " + hit);
					result = true;

					Method method;
					try {
						method = GameScene.class.getMethod(hit, CCSprite.class,
								CCSprite.class);

						method.invoke(gameScene, array1.get(i), array2.get(j));

					} catch (SecurityException e1) {
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	public CGRect getBoarders(CCSprite object) {
		CGRect rect = object.getBoundingBox();
		CGPoint GLpoint = rect.origin;
		CGRect GLrect = CGRect.make(GLpoint.x, GLpoint.y, rect.size.width,
				rect.size.height);

		return GLrect;
	}
	//-----------------------------------------------------------------
	/*public void checkHits2(float dt) {
		
		
		this.checkRadiusHitsOfArray2(this.enemyArray,
				this.shootsArray, this, "enemyHit");

		this.checkRadiusHitsOfArray2(this.enemyArray,
				this.playersArray, this, "playerHit");

	}
	
	private boolean checkRadiusHitsOfArray2(List<? extends CCSprite> array3, List<? extends CCSprite> array4,
		GameScene gameScene, String hit1) {

	boolean result = false;
			
		for (int k = 0; k < array3.size(); k++) {
			// Get Object from First Array
			CGRect rect3 = getBoarders(array3.get(k));

			for (int l = 0; l < array4.size(); l++) {
				// Get Object from Second Array
				CGRect rect4 = getBoarders(array4.get(l));

			// Check Hit!
			
			if (CGRect.intersects(rect3, rect4)) {
				System.out.println("Colision Detected: " + hit1);
				result = true;

				Method method1;
				try {
					method1 = GameScene.class.getMethod(hit1, CCSprite.class,
							CCSprite.class);
					

					method1.invoke(gameScene, array3.get(k), array4.get(l));

				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	return result;
}
		
		*/
		//------------------------------------------------------------------

	private void startEngines() {
		this.addChild(this.missileEngine);
		this.missileEngine.setDelegate(this);
		
		//this.addChild(this.enemyEngine);
		//this.enemyEngine.setDelegate(this);
	}

	@Override
	public void createMissile(Missile missile) {

		this.missilesLayer.addChild(missile);
		missile.setDelegate(this);
		missile.start();
		this.missilesArray.add(missile);

	}

	public boolean shoot() {
		player.shoot();
		//enemy.shoot();
		return true;
	}

	@Override
	public void createShoot(Shoot shoot) {

		this.shootsLayer.addChild(shoot);
		shoot.setDelegate(this);
		shoot.start();
		this.shootsArray.add(shoot);

	}

	public void moveLeft() {
		player.moveLeft();
	}

	public void moveRight() {
		player.moveRight();
	}

	public void missileHit(CCSprite missile, CCSprite shoot) {
		((Missile) missile).shooted();
		((Shoot) shoot).explode();
		this.score.increase();
	}

	@Override
	public void removeMissile(Missile missile) {
		this.missilesArray.remove(missile);

	}
	
	/*@Override
	public void createEnemy(Enemy enemy) {
		this.enemyLayer.addChild(enemy);
		enemy.setDelegate(this);
		enemy.start();
		this.enemyArray.add(enemy);
		
	}
	
	public void enemyHit(CCSprite enemy, CCSprite shoot) {
		((Enemy) enemy).shooted();
		((Shoot) shoot).explode();
		this.score.increase();
	}

	@Override
	public void removeEnemy(Enemy enemy) {
		this.enemyArray.remove(enemy);		
	}*/

	@Override
	public void removeShoot(Shoot shoot) {
		this.shootsArray.remove(shoot);

	}

	public void playerHit(CCSprite missile, CCSprite player /*,CCSprite enemy*/) {
		((Missile) missile).shooted();
		((Player) player).explode();
		//((Enemy) enemy).shooted();
		CCDirector.sharedDirector().replaceScene(new GameOverScreen().scene());
	}

	// PAUSE
	// ===========
	public void pauseGameAndShowLayer() {

		if (Runner.check().isGamePlaying() && !Runner.check().isGamePaused()) {
			this.pauseGame();
		}

		if (Runner.check().isGamePaused() && Runner.check().isGamePlaying()
				&& this.pauseScreen == null) {

			this.pauseScreen = new PauseScreen();
			this.layerTop.addChild(this.pauseScreen);
			this.pauseScreen.setDelegate(this);
		}

	}

	private void pauseGame() {
		if (!Runner.check().isGamePaused() && Runner.check().isGamePlaying()) {
			Runner.setGamePaused(true);
		}
	}

	@Override
	public void resumeGame() {
		if (Runner.check().isGamePaused() || !Runner.check().isGamePlaying()) {

			// Resume game
			this.pauseScreen = null;
			Runner.setGamePaused(false);
			this.setIsTouchEnabled(true);
		}
	}

	@Override
	public void quitGame() {
		SoundEngine.sharedEngine().setEffectsVolume(0f);
		SoundEngine.sharedEngine().setSoundVolume(0f);

		CCDirector.sharedDirector().replaceScene(new TitleScreen().scene());

	}

	public void startFinalScreen() {
		CCDirector.sharedDirector().replaceScene(new FinalScreen().scene());
	}



}
