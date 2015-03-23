package br.com.declau.hellrider.game.control;

import static br.com.declau.hellrider.config.DeviceSettings.screenHeight;
import static br.com.declau.hellrider.config.DeviceSettings.screenResolution;
import static br.com.declau.hellrider.config.DeviceSettings.screenWidth;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.types.CGPoint;

import br.com.declau.hellrider.config.Assets;
import br.com.declau.hellrider.game.scenes.GameScene1;

public class GameButtons1  extends CCLayer implements ButtonDelegate{
	
	private Button leftControl;
	private Button rightControl;
	protected Button shootButton;
	private Button pauseButton;
	private GameScene1 delegate;

	
	public static GameButtons1 gameButtons() {
		return new GameButtons1();
	}

	public GameButtons1() {

		// Enable Touch
		this.setIsTouchEnabled(true);
		
		// Create Buttons
		this.leftControl 	= new Button(Assets.LEFTCONTROL);
		this.rightControl 	= new Button(Assets.RIGHTCONTROL);
		this.shootButton 	= new Button(Assets.SHOOTBUTTON);
		this.pauseButton 	= new Button(Assets.PAUSE);
		
		// Set Buttons Delegates
		this.leftControl.setDelegate(this);
		this.rightControl.setDelegate(this);
		this.shootButton.setDelegate(this);
		this.pauseButton.setDelegate(this);
				
		// set position
		setButtonspPosition();

		// Add Buttons to Screen
		//addChild(leftControl);
		//addChild(rightControl);
		addChild(shootButton);
		addChild(pauseButton);
		
	}
	
	private void setButtonspPosition() {

		// Buttons Positions
		leftControl.setPosition(screenResolution(CGPoint.ccp( 40  , 40 ))) ;
		rightControl.setPosition(screenResolution(CGPoint.ccp( 100 , 40 ))) ;
		shootButton.setPosition(screenResolution(CGPoint.ccp( screenWidth() -40 , 40 ))) ;
		
		pauseButton.setPosition(screenResolution(CGPoint.ccp( 40  , screenHeight() - 30 ))) ;
	}

	@Override
	public void buttonClicked(Button sender) {
		
		if (sender.equals(this.leftControl)) {
			System.out.println("Button clicked: Left");
			this.delegate.moveLeft();
		}
		
		if (sender.equals(this.rightControl)) {
			System.out.println("Button clicked: Right");
			this.delegate.moveRight();
		}

		if (sender.equals(this.shootButton)) {
			System.out.println("Button clicked: Shooting!");
			this.delegate.shoot();
		}
		
				
		if (sender.equals(this.pauseButton)) {
			System.out.println("Button clicked: pause");
			this.delegate.pauseGameAndShowLayer();
		}

	}

	public void setDelegate(GameScene1 gameScene1) {
		this.delegate = gameScene1;
		
	}

	

}
