import { Graphics } from './graphics';
import { KeyHandler } from './keyHandler';
import { Socket } from './socket';
import { UserInterface } from './userInterface';

export class App {
  public gfx: Graphics;
  public socket: Socket;
  public keyHandler: KeyHandler;
  public ui: UserInterface;

  constructor() {
    this.ui = new UserInterface(this);
    this.socket = new Socket(this);
    this.keyHandler = new KeyHandler(this);
    this.gfx = new Graphics();
  }
}

(() => {
  // tslint:disable-next-line:no-unused-expression
  new App();
})();
