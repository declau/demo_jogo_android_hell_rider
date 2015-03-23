package br.com.declau.hellrider.game.interfaces;

import br.com.declau.hellrider.game.objects.Missile;

public interface MissileEngineDelegate {
	public void createMissile(Missile missile);
	public void removeMissile(Missile missile);
}
