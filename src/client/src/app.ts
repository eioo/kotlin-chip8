import { Graphics } from './graphics';
import { Socket } from './socket';
import { IRomList, IState } from './types';
import { UserInterface } from './userInterface';

export class App {
  public gfx: Graphics;
  public socket: Socket;
  public ui: UserInterface;

  constructor() {
    this.socket = new Socket(this);
    this.gfx = new Graphics();
    this.ui = new UserInterface(this);
  }
}

(() => {
  // tslint:disable-next-line:no-unused-expression
  new App();
})();
