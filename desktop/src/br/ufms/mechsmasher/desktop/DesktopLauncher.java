package br.ufms.mechsmasher.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import br.ufms.mechsmasher.MechSmasher;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// O tamanho da tela é fixo e widescreen (16:9)
		config.width = 960;
		config.height = 540;
		new LwjglApplication(new MechSmasher(), config);
	}
}
