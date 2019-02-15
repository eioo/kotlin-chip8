import { Graphics } from './graphics';
import { Socket } from './socket';

export interface IState {
  pc: number;
  i: number;
  sp: number;
  dt: number;
  st: number;
  stack: number[];
  v: number[];
}

const pcEl = document.querySelector('#pc') as HTMLInputElement;
const iEl = document.querySelector('#i') as HTMLInputElement;
const spEl = document.querySelector('#sp') as HTMLInputElement;
const dtEl = document.querySelector('#dt') as HTMLInputElement;
const stEl = document.querySelector('#st') as HTMLInputElement;

export class App {
  public gfx: Graphics;
  private s: Socket;

  constructor() {
    this.s = new Socket(this);
    this.gfx = new Graphics();
  }

  public updateState(data: string) {
    const vars = data.split(';');
    const state: IState = {
      pc: parseInt(vars[0], 10),
      i: parseInt(vars[1], 10),
      sp: parseInt(vars[2], 10),
      dt: parseInt(vars[3], 10),
      st: parseInt(vars[4], 10),
      stack: vars[5].split(',').map(x => parseInt(x, 10)),
      v: vars[6].split(',').map(x => parseInt(x, 10)),
    };

    for (let i = 0; i < 16; i++) {
      const regEl = document.querySelector('#v' + i) as HTMLInputElement;
      regEl.value = state.v[i].toString(16);
    }

    for (let i = 0; i < 16; i++) {
      const stackEl = document.querySelector('#s' + i) as HTMLInputElement;
      stackEl.value = state.stack[i].toString(16);
    }

    pcEl.value = state.pc.toString(16);
    iEl.value = state.i.toString(16);
    spEl.value = state.sp.toString(16);
    dtEl.value = state.dt.toString(16);
    stEl.value = state.st.toString(16);
  }
}

(() => {
  const _ = new App();
})();
