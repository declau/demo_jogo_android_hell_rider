package br.com.declau.hellrider.game.interfaces;

import br.com.declau.hellrider.game.objects.Enemy;
import br.com.declau.hellrider.game.objects.Missile;

public interface EnemyEngineDelegate {
	public void createEnemy(Enemy enemy);
	public void removeEnemy(Enemy enemy);

}
