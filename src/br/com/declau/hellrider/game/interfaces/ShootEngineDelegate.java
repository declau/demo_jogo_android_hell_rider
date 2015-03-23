package br.com.declau.hellrider.game.interfaces;

import br.com.declau.hellrider.game.objects.Shoot;

public interface ShootEngineDelegate {
	public void createShoot(
			Shoot shoot);

	public void removeShoot(Shoot shoot);
}
