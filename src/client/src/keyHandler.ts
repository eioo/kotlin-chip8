import keycode from 'keycode';
import { App } from './app';

/*
  Key layout:

  On browser   CHIP-8 equivalent 
   1 2 3 4        1 2 3 C
   Q W E R  --->  4 5 6 D
   A S D F        7 8 9 E
   Z X C V        A 0 B F
*/

const keys = '1234qwerasdfzxcv';

export class KeyHandler {
  private pressed = new Array(16).fill(0);

  constructor(private app: App) {
    this.handleKeyDown();
    this.handleKeyUp();
  }

  public handleKeyDown() {
    document.body.addEventListener('keydown', (e: Event) => {
      const keyIndex = keys.indexOf(keycode(e));

      if (keyIndex === -1) {
        return;
      }

      if (!this.pressed[keyIndex]) {
        this.pressed[keyIndex] = 1;
        this.app.socket.ws.send(`keydown ${keyIndex}`);
      }
    });
  }

  public handleKeyUp() {
    document.body.addEventListener('keyup', (e: Event) => {
      const keyIndex = keys.indexOf(keycode(e));

      if (keyIndex === -1) {
        return;
      }

      if (this.pressed[keyIndex]) {
        this.pressed[keyIndex] = 0;
        this.app.socket.ws.send(`keyup ${keyIndex}`);
      }
    });
  }
}
