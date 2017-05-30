package br.ufms.mechsmasher;

/**
 * Um objeto que responde a entradas do usuário
 */
public interface Controller {
    /**
     * Acionado quando o jogador pressiona alguma tecla.
     */
    void press(int key);
    /**
     * Acionado quando o jogador solta alguma tecla.
     */
    void release(int key);
}
