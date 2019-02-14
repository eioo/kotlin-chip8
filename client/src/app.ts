import { Graphics } from './graphics';
import { Socket } from './socket';

export class App {
  public gfx: Graphics;
  private s: Socket;

  constructor() {
    this.s = new Socket(this);
    this.gfx = new Graphics();
  }
}

(() => {
  const _ = new App();
})();
